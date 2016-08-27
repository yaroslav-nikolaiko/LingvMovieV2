package lingvo.movie.security;

import lingvo.movie.entity.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by yaroslav on 27.08.16.
 */
public class AccountPrincipal extends User {
    @Getter
    Account account;
    public AccountPrincipal(Account account, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.account = account;
    }
}
