package lingvo.movie.security;

import lingvo.movie.dao.AuthorityRepository;
import lingvo.movie.entity.Account;
import lingvo.movie.entity.Authority;
import lingvo.movie.security.client.FacebookService;
import lingvo.movie.security.client.FacebookUser;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.List;
import java.util.Map;

/**
 * Created by yaroslav on 28.08.16.
 */
public class FacebookAccountTokenGranter extends AbstractTokenGranter {
    public static final String GRANT_TYPE="facebook_token";

    @Setter
    FacebookService facebookService;

    @Setter
    AuthorityRepository authorityRepository;

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
        if(facebookUser==null )
            throw new InvalidTokenException("Failed to retrieve facebook user by access token");
        if(! userID.equals(facebookUser.getId()))
            throw new InvalidTokenException("User Id does not match");
        Account account = new Account();
        account.setId(1L);
        account.setName(facebookUser.getName());
        List<Authority> authorities = authorityRepository.getAuthorities("USER");
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
