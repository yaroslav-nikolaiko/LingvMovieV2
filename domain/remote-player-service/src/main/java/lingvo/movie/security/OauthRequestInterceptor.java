package lingvo.movie.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;

/**
 * Created by yaroslav on 16.10.16.
 */
@Component
public class OauthRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        //template.header("Content-Type", "application/json");
        String token = token();
        if(token!=null)
            template.header("Authorization", token());
    }

    private String token(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            String tokenValue = details.getTokenValue();
            String tokenType = details.getTokenType();
            return tokenType+" "+tokenValue;
        }
        return null;
    }
}


