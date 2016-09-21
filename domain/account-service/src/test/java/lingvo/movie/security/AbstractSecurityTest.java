package lingvo.movie.security;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.filter.ValueNode;
import lingvo.movie.AbstractTest;
import lingvo.movie.AccountServiceApplication;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by yaroslav on 21.09.16.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AccountServiceApplication.class)
public class AbstractSecurityTest extends AbstractTest {
    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    TokenStore tokenStore;

    protected MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    protected MockHttpServletRequestBuilder tokenRequestAdminCred() {
        return post("/oauth/token")
                .header("Authorization", "Basic " + new String(encodeBase64("any:".getBytes())))
                .param("grant_type", "password")
                .param("username", "admin")
                .param("password", "admin");
    }

    protected MockHttpServletRequestBuilder tokenRequestUserCred() {
        return post("/oauth/token")
                .header("Authorization", "Basic " + new String(encodeBase64("any:".getBytes())))
                .param("grant_type", "password")
                .param("username", "user")
                .param("password", "user");
    }

    protected String getTokenAdmin() throws Exception {
        String response = mockMvc.perform(tokenRequestAdminCred())
                .andReturn()
                .getResponse().getContentAsString();
        Object token = JsonPath.compile("$.access_token").read(response);
        return token.toString();
    }

    protected String getTokenUser() throws Exception {
        String response = mockMvc.perform(tokenRequestUserCred())
                .andReturn()
                .getResponse().getContentAsString();
        Object token = JsonPath.compile("$.access_token").read(response);
        return token.toString();
    }

}
