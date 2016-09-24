package lingvo.movie.security;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lingvo.movie.AbstractTest;
import lingvo.movie.AccountServiceApplication;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    protected HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    protected MockHttpServletRequestBuilder tokenRequest(String username) {
        return post("/oauth/token")
                .header("Authorization", "Basic " + new String(encodeBase64("any:".getBytes())))
                .param("grant_type", "password")
                .param("username", username)
                .param("password", username);
    }

    protected MockHttpServletRequestBuilder tokenRequestAdminCred() {
        return tokenRequest("admin");
    }

    protected MockHttpServletRequestBuilder tokenRequestUserCred() {
        return tokenRequest("user");
    }

    protected String getToken(String username) throws Exception {
        String response = mockMvc.perform(tokenRequest(username))
                .andReturn()
                .getResponse().getContentAsString();
        Object token = JsonPath.compile("$.access_token").read(response);
        return token.toString();
    }

    protected String getTokenAdmin() throws Exception {
        return getToken("admin");
    }

    protected String getTokenUser() throws Exception {
        return getToken("user");
    }

    protected DocumentContext getAccount(Long id, String username) throws Exception {
        String json = mockMvc.perform(get("/accounts/" + id)
                .header("Authorization", "Bearer " + getToken(username)))
                .andReturn().getResponse().getContentAsString();
        return JsonPath.parse(json);
    }


    String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
