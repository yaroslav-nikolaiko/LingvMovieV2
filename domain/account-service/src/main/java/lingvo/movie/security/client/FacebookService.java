package lingvo.movie.security.client;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

/**
 * Created by yaroslav on 28.08.16.
 */
@Service
public class FacebookService {
    RestTemplate restTemplate = new RestTemplate();
    String uriTemplate;

    @Autowired
    void buildUri(FacebookResources facebookResources) {
        String userInfoUri = facebookResources.getResource().getUserInfoUri();
        String accessTokenName = facebookResources.getClient().getTokenName();
        this.uriTemplate = UriComponentsBuilder.fromHttpUrl(userInfoUri)
                .queryParam(accessTokenName, "{accessToken}").build().toString();
    }


    public FacebookUser getFacebookUser(String accessToken) {
        return restTemplate.getForObject(
                uriTemplate,
                FacebookUser.class,
                Collections.singletonMap("accessToken", accessToken));
    }

    @ConfigurationProperties("facebook")
    @Component
    @Getter
    static class FacebookResources {
        private OAuth2ProtectedResourceDetails client = new AuthorizationCodeResourceDetails();
        private ResourceServerProperties resource = new ResourceServerProperties();
    }

}



