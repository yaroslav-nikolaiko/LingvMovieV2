package lingvo.movie.security;

import lingvo.movie.entity.Account;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by yaroslav on 28.08.16.
 */
@Component
public class AccountUserDetailsService implements UserDetailsService {
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
}
