package lingvo.movie.security;

import lingvo.movie.dao.AccountRepository;
import lingvo.movie.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by yaroslav on 28.08.16.
 */
@Component
public class AccountUserDetailsService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findOneByEmail(username);
        if(account == null){
            account = accountRepository.findOneByName(username);
        }
        if(account==null)
            throw new UsernameNotFoundException("Could not find the user '" + username + "'");
        return account;
    }
}
