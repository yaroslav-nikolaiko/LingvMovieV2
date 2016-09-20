package lingvo.movie.security;

import lingvo.movie.entity.Account;
import lingvo.movie.entity.Authority;
import lingvo.movie.security.client.FacebookService;
import lingvo.movie.security.client.FacebookUser;
import lombok.Setter;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.List;
import java.util.Map;

import static lingvo.movie.entity.Authority.createAuthorityList;

/**
 * Created by yaroslav on 28.08.16.
 */
public class FacebookAccountTokenGranter extends AbstractTokenGranter {
    public static final String GRANT_TYPE="facebook_token";

    @Setter
    FacebookService facebookService;

    FacebookAccountTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> params = tokenRequest.getRequestParameters();
        String accessToken = params.get("accessToken");
        String userID = params.get("userID");
        FacebookUser facebookUser = facebookService.getFacebookUser(accessToken);
        if( ! facebookUser.getId().equals(userID))
            throw new InvalidTokenException("User Id does not match");
        Account account = new Account();
        account.setId(1L);
        account.setName(facebookUser.getName());
        List<Authority> authorities = createAuthorityList("USER");
        account.setAuthorities(authorities);

        Authentication user = new AbstractAuthenticationToken(authorities) {
            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return account;
            }
        };
        return new OAuth2Authentication(tokenRequest.createOAuth2Request(client), user);
    }
}