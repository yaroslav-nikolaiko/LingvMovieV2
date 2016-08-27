package lingvo.movie.security;

import lingvo.movie.entity.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by yaroslav on 17.08.16.
 */
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers("/login**").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public AccountPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
                if("admin".equals(username)){
                    Account account = new Account();
                    account.setName(username);
                    account.setEmail("admin@gmail.com");
                    account.setId(1L);

                    return new AccountPrincipal(account, username, "admin", AuthorityUtils.createAuthorityList("ADMIN", "USER"));
                }else if("user".equals(username)){
                    Account account = new Account();
                    account.setName(username);
                    account.setEmail("user@gmail.com");
                    account.setId(1L);

                    return new AccountPrincipal(account, username, "user", AuthorityUtils.createAuthorityList("USER"));
                }
                else throw new UsernameNotFoundException("could not find the user '" + username + "'");
            }
        });
    }



}
