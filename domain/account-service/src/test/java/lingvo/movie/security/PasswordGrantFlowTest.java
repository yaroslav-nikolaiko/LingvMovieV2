package lingvo.movie.security;

import org.junit.Test;

import java.util.Map;

import static lingvo.movie.utils.SimpleMatcher.matcher;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by yaroslav on 18.09.16.
 */
public class PasswordGrantFlowTest extends AbstractSecurityTest {
    @Test
    public void unauthorizedStatus() throws Exception {
        mockMvc.perform(get("/hello")
                .contentType(contentType))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnToken() throws Exception {
        mockMvc.perform(tokenRequestAdminCred())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", notNullValue()));
    }

    @Test
    public void badCredentialsShouldReturn401() throws Exception {
        mockMvc.perform(post("/oauth/token")
                .header("Authorization","Basic "+ new String(encodeBase64("any:".getBytes())))
                .param("grant_type", "password")
                .param("username", "admin")
                .param("password", "wrong_password"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void tokenShouldContainUserName() throws Exception {
        mockMvc.perform(tokenRequestAdminCred())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", matcher(item -> {
                    Object name = tokenStore.readAccessToken(item.toString()).getAdditionalInformation()
                            .get("user_name");
                    return "admin".equals(name);
                })));
    }

    @Test
    public void tokenShouldContainId() throws Exception {
        mockMvc.perform(tokenRequestAdminCred())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", matcher(item -> {
                    Number id = (Number) tokenStore.readAccessToken(item.toString()).getAdditionalInformation()
                            .get("id");
                    return id.longValue() == adminID;
                })));
    }

    @Test
    public void tokenShouldContainAccountObjectMap() throws Exception {
        mockMvc.perform(tokenRequestAdminCred())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", matcher(item -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> accountMap = (Map<String, Object>)tokenStore.readAccessToken(item.toString()).getAdditionalInformation()
                            .get("account");
                    assertEquals("admin",accountMap.get("name"));
                    assertEquals("admin@gmail.com",accountMap.get("email"));
                    return true;
                })));
    }

    @Test
    public void tokenShouldNotContainPassword() throws Exception {
        mockMvc.perform(tokenRequestAdminCred())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", matcher(item -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> accountMap = (Map<String, Object>)tokenStore.readAccessToken(item.toString()).getAdditionalInformation()
                            .get("account");
                    assertNull(accountMap.get("password"));
                    return true;
                })));
    }
}