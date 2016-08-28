package lingvo.movie.security.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

/**
 * Created by yaroslav on 28.08.16.
 */
@Component
public class FacebookAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    AuthorizationServerTokenServices tokenServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2Request storedRequest = new OAuth2Request(Collections.singletonMap("grant_type", "custom"),"any", authentication.getAuthorities(), true, Collections.singleton("trust"),null, null, null, null );
        OAuth2AccessToken accessToken = tokenServices.createAccessToken(new OAuth2Authentication(storedRequest, authentication));

        ObjectMapper mapper = new ObjectMapper();
        String jsonInStringBase64Encoded = new String(Base64.getEncoder().encode(mapper.writeValueAsBytes(accessToken)));

        Cookie auth_token = new Cookie("auth_token", jsonInStringBase64Encoded);
        auth_token.setMaxAge(5);
        auth_token.setPath("/");
        response.addCookie(auth_token);

        String redirectURI = "/";
        if(request.getHeader("referer") != null)
            redirectURI = request.getHeader("referer");
        response.sendRedirect(redirectURI);
    }
}
