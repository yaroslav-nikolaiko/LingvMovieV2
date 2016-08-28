package lingvo.movie.security.client;

import lingvo.movie.entity.Account;
import lingvo.movie.security.AccountPrincipal;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by yaroslav on 28.08.16.
 */
public class FacebookUserInfoTokenServices  extends UserInfoTokenServices {
    public FacebookUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        super(userInfoEndpointUrl, clientId);
        setAuthoritiesExtractor(map -> authorities());
    }


    @Override
    protected Object getPrincipal(Map<String, Object> map) {
        Account account = new Account();
        String name = (String) map.get("name");
        account.setName(name);
        return new AccountPrincipal(account, name, "", authorities());
    }

    private List<GrantedAuthority> authorities() {
        return AuthorityUtils.createAuthorityList("USER");
    }
}
