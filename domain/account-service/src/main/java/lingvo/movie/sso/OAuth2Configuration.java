package lingvo.movie.sso;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lingvo.movie.entity.Account;
import lingvo.movie.security.AccountPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.*;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Created by yaroslav on 17.08.16.
 */
@Configuration
@EnableAuthorizationServer
@EnableOAuth2Client
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("any")
                .scopes("trust")
                .autoApprove(true)
                .authorities("USER", "ADMIN");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(jwtTokenEnhancer())
                .authenticationManager(authenticationManager);
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
        converter.setAccessTokenConverter(tokenConverter);

        tokenConverter.setUserTokenConverter(new DefaultUserAuthenticationConverter(){
            @Override
            public Map<String, ?> convertUserAuthentication(Authentication authentication) {
                Map<String, Object> response = new LinkedHashMap<String, Object>();
                AccountPrincipal principal = (AccountPrincipal) authentication.getPrincipal();
                response.put(USERNAME, principal.getUsername());
                response.put("account", principal.getAccount());
                if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                    response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
                }
                return response;
            }
        });
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
        return converter;
    }

/*    @Bean
    @Scope("prototype")
    @Primary
    OAuth2ClientContext oauth2ClientContext(){
        return new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
    }*/

    @Configuration
    @EnableResourceServer
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class SecurityConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    //.antMatchers("*//**//**").authenticated()
                    .antMatchers(HttpMethod.GET, "/hello").hasAuthority("USER")
                    .and()
                    .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            //.antMatchers(HttpMethod.POST, "/foo").hasAuthority("FOO_WRITE");
            //you can implement it like this, but I show method invocation security on write
        }

        @Autowired
        OAuth2ClientContext oauth2ClientContext;

        @Bean
        public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(filter);
            registration.setOrder(-100);
            return registration;
        }

        @Bean
        @ConfigurationProperties("facebook")
        ClientResources facebook() {
            return new ClientResources();
        }

        private Filter ssoFilter() {
            CompositeFilter filter = new CompositeFilter();
            List<Filter> filters = new ArrayList<>();
            filters.add(ssoFilter(facebook(), "/login/facebook"));
            filter.setFilters(filters);
            return filter;
        }

        @Autowired
        AuthorizationServerTokenServices tokenServices;


        private Filter ssoFilter(ClientResources client, String path) {
            OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
                    path);
            OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
            filter.setRestTemplate(template);
            UserInfoTokenServices tokenServices = new UserInfoTokenServices(
                    client.getResource().getUserInfoUri(), client.getClient().getClientId()) {
                @Override
                protected Object getPrincipal(Map<String, Object> map) {
                    Account account = new Account();
                    String name = (String) map.get("name");
                    account.setName(name);
                    return new AccountPrincipal(account, name, "", AuthorityUtils.createAuthorityList("USER"));
                }
            };
            tokenServices.setAuthoritiesExtractor(map -> AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
            filter.setTokenServices(tokenServices);
            filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                    OAuth2Request storedRequest = new OAuth2Request(Collections.singletonMap("grant_type", "custom"),"any", authentication.getAuthorities(), true, Collections.singleton("trust"),null, null, null, null );
                    OAuth2AccessToken accessToken = SecurityConfiguration.this.tokenServices.createAccessToken(new OAuth2Authentication(storedRequest, authentication));

                    ObjectMapper mapper = new ObjectMapper();
                    String jsonInStringBase64Encoded = new String(Base64.getEncoder().encode(mapper.writeValueAsBytes(accessToken)));

                    Cookie auth_token = new Cookie("auth_token", jsonInStringBase64Encoded);
                    auth_token.setMaxAge(5);
                    auth_token.setPath("/");
                    response.addCookie(auth_token);

                    String redirectURI = "/";
                    if(request.getHeader("referer") != null)
                        redirectURI = request.getHeader("referer");
                    response.sendRedirect(redirectURI);
                }
            });
            return filter;
        }


        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId("foo").tokenStore(tokenStore);
        }



        @Autowired
        TokenStore tokenStore;
    }
}

class ClientResources {
    private OAuth2ProtectedResourceDetails client = new AuthorizationCodeResourceDetails();
    private ResourceServerProperties resource = new ResourceServerProperties();

    public OAuth2ProtectedResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }
}
