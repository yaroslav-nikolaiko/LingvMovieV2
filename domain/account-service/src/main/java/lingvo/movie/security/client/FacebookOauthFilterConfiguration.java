package lingvo.movie.security.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.*;

/**
 * Created by yaroslav on 28.08.16.
 */
@Configuration
public class FacebookOauthFilterConfiguration {
    @Autowired
    OAuth2ClientContext oauth2ClientContext;
    @Autowired
    FacebookAuthenticationSuccessHandler facebookAuthenticationSuccessHandler;


    @Bean(name = "facebookFilter")
    Filter facebookFilter(){
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(facebook(), "/login/facebook"));
        filter.setFilters(filters);
        return filter;
    }

    @Bean
    @ConfigurationProperties("facebook")
    ClientResources facebook() {
        return new ClientResources();
    }

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
                path);

        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        UserInfoTokenServices tokenServices = new FacebookUserInfoTokenServices(
                client.getResource().getUserInfoUri(),
                client.getClient().getClientId());

        filter.setRestTemplate(template);
        filter.setTokenServices(tokenServices);
        filter.setAuthenticationSuccessHandler(facebookAuthenticationSuccessHandler);
        return filter;
    }

}
