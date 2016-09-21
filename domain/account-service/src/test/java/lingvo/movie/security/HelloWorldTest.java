package lingvo.movie.security;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by yaroslav on 21.09.16.
 */
public class HelloWorldTest extends AbstractSecurityTest {

    @Test
    public void helloUserShouldReturn401() throws Exception {
        mockMvc.perform(get("/hello/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void adminCouldAccessHelloUser() throws Exception {
        mockMvc.perform(get("/hello/user")
                .header("Authorization", "Bearer "+getTokenAdmin()))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("Hello admin", result.getResponse().getContentAsString()));
    }

    @Test
    public void adminCouldAccessHelloAdmin() throws Exception {
        mockMvc.perform(get("/hello/admin")
                .header("Authorization", "Bearer "+getTokenAdmin()))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("Hello admin", result.getResponse().getContentAsString()));
    }

    @Test
    public void userCouldAccessHelloUser() throws Exception {
        mockMvc.perform(get("/hello/user")
                .header("Authorization", "Bearer "+getTokenUser()))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("Hello user", result.getResponse().getContentAsString()));
    }

    @Test
    public void userHelloAdmin403() throws Exception {
        mockMvc.perform(get("/hello/admin")
                .header("Authorization", "Bearer "+getTokenUser()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void anonymousCouldAccessHelloWorld() throws Exception {
        mockMvc.perform(get("/hello/world"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("Hello World", result.getResponse().getContentAsString()));
    }
}
