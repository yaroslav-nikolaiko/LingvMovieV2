package lingvo.movie.security;

import lingvo.movie.AccountServiceApplication;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    public void passwordGrantFlow() throws Exception {
        mockMvc.perform(post("/oauth/token")
                .header("Authorization","Basic "+ new String(encodeBase64("any:".getBytes())))
                .param("grant_type", "password")
                .param("username", "admin")
                .param("password", "admin"))
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
}