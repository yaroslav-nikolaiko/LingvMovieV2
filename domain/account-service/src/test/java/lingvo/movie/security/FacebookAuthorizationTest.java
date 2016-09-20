package lingvo.movie.security;

import lingvo.movie.security.client.FacebookService;
import lingvo.movie.security.client.FacebookUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static lingvo.movie.utils.SimpleMatcher.matcher;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by yaroslav on 21.09.16.
 */
public class FacebookAuthorizationTest extends AbstractSecurityTest {
    private boolean facebookServiceMockInitialized = false;
    @MockBean
    private FacebookService facebookService;

    @Before
    public void initFacebookService(){
        if(facebookService!=null && ! facebookServiceMockInitialized){
            FacebookUser facebookUser = new FacebookUser();
            facebookUser.setName("FacebookUserName");
            facebookUser.setId("123456");
            Mockito.when(facebookService.getFacebookUser("facebook_token_value")).thenReturn(facebookUser);
            facebookServiceMockInitialized = true;
        }
    }

    @Test
    public void shouldReturnToken() throws Exception {
        mockMvc.perform(post("/oauth/token")
                .header("Authorization", "Basic " + new String(encodeBase64("any:".getBytes())))
                .param("grant_type", "facebook_token")
                .param("accessToken", "facebook_token_value")
                .param("userID", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", notNullValue()))
                .andExpect(jsonPath("$.access_token", matcher(item -> {
                    Object name = tokenStore.readAccessToken(item.toString()).getAdditionalInformation()
                            .get("user_name");
                    return "FacebookUserName".equals(name);
                })));
    }

    @Test
    public void wrongUserIdShouldReturn401() throws Exception {
        mockMvc.perform(post("/oauth/token")
                .header("Authorization", "Basic " + new String(encodeBase64("any:".getBytes())))
                .param("grant_type", "facebook_token")
                .param("accessToken", "facebook_token_value")
                .param("userID", "WRONG USER ID"))
                .andExpect(status().isUnauthorized());
    }
}
