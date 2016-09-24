package lingvo.movie.security;

import lingvo.movie.dao.AccountRepository;
import lingvo.movie.entity.Account;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static lingvo.movie.utils.SimpleMatcher.matcher;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by yaroslav on 22.09.16.
 */
@Transactional
@Rollback
public class AccountEndpointTest extends AbstractSecurityTest {
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void anonymousCanNotAccessAnyAccount() throws Exception {
        mockMvc.perform(get("/accounts/2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void userCanGetOwnAccountById() throws Exception {
        mockMvc.perform(get("/accounts/2")
                .header("Authorization", "Bearer " + getTokenUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("user")))
                .andExpect(jsonPath("$.email", is("user@gmail.com")));
    }

    @Test
    public void userForbiddenGetAnotherAccountById() throws Exception {
        mockMvc.perform(get("/accounts/1")
                .header("Authorization", "Bearer " + getTokenUser()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void adminCanAccessAnotherAccountById() throws Exception {
        mockMvc.perform(get("/accounts/2")
                .header("Authorization", "Bearer " + getTokenAdmin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("user")))
                .andExpect(jsonPath("$.email", is("user@gmail.com")));
    }

    @Test
    public void anonymousCanCreateAccount() throws Exception {
        Account melisandre = new Account();
        melisandre.setName("melisandre");
        melisandre.setEmail("melisandre@gmail.com");
        melisandre.setPassword("melisandre");
        mockMvc.perform(post("/accounts")
                .content(json(melisandre)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", notNullValue()));
    }

    @Test
    public void createdAccountHasRoleUSER() throws Exception {
        Account melisandre = new Account();
        melisandre.setName("melisandre");
        melisandre.setEmail("melisandre@gmail.com");
        melisandre.setPassword("melisandre");
        String location = mockMvc.perform(post("/accounts")
                .content(json(melisandre)))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location)
                .header("Authorization", "Bearer "+getToken("melisandre")))
                .andExpect(status().isOk());
    }

    @Test
    public void passwordIsPersistedWhenCreatingAccount() throws Exception {
        Account melisandre = new Account();
        melisandre.setName("melisandre");
        melisandre.setEmail("melisandre@gmail.com");
        melisandre.setPassword("melisandre");
        mockMvc.perform(post("/accounts")
                .content(json(melisandre)))
                .andExpect(status().isCreated());

        Account account = accountRepository.findOneByName("melisandre");
        assertEquals("melisandre", account.getName());
        assertEquals("melisandre", account.getPassword());
    }

    @Test
    public void passwordIsNotPresentInResponse() throws Exception {
        mockMvc.perform(get("/accounts/1")
                .header("Authorization", "Bearer " + getTokenAdmin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("admin")))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void userCanUpdateOwnAccount() throws Exception {

    }

    @Test
    public void userForbiddenUpdateAnotherAccount() throws Exception {

    }

    @Test
    public void adminCanUpdateAnotherAccount() throws Exception {

    }

    @Test
    public void userCanDeleteOwnAccount() throws Exception {

    }

    @Test
    public void userForbiddenDeleteAnotherAccount() throws Exception {

    }

    @Test
    public void adminCanDeleteAnotherAccount() throws Exception {

    }

}
