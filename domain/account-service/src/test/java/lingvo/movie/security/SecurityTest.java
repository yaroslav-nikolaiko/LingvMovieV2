package lingvo.movie.security;

import lingvo.movie.AccountServiceApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Map;

import static lingvo.movie.utils.SimpleMatcher.matcher;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by yaroslav on 18.09.16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = AccountServiceApplication.class)
@ActiveProfiles("test")
public class SecurityTest {
    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void unauthorizedStatus() throws Exception {
        mockMvc.perform(get("/hello")
                .contentType(contentType))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void passwordGrantFlowShouldReturnToken() throws Exception {
        mockMvc.perform(tokenRequestAdminCred())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", notNullValue()));
    }

    @Test
    public void passwordGrantFlowBadCredentials() throws Exception {
        mockMvc.perform(post("/oauth/token")
                .header("Authorization","Basic "+ new String(encodeBase64("any:".getBytes())))
                .param("grant_type", "password")
                .param("username", "admin")
                .param("password", "wrong_password"))
                .andExpect(status().isUnauthorized());
    }

    @Autowired
    TokenStore tokenStore;

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

    MockHttpServletRequestBuilder tokenRequestAdminCred(){
        return post("/oauth/token")
                .header("Authorization", "Basic " + new String(encodeBase64("any:".getBytes())))
                .param("grant_type", "password")
                .param("username", "admin")
                .param("password", "admin");
    }
}