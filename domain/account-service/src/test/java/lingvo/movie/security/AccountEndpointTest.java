package lingvo.movie.security;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lingvo.movie.dao.AccountRepository;
import lingvo.movie.entity.Account;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        DocumentContext json = JsonPath.parse(json(melisandre));
        json = json.put("$","password", "melisandre");
        String location = mockMvc.perform(post("/accounts")
                .content(json.jsonString()))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location)
                .header("Authorization", "Bearer "+getToken("melisandre")))
                .andExpect(status().isOk());
    }

    @Test
    public void passwordIsPersisted() throws Exception {
        Account melisandre = new Account();
        melisandre.setName("melisandre");
        melisandre.setEmail("melisandre@gmail.com");
        DocumentContext json = JsonPath.parse(json(melisandre));
        json = json.put("$","password", "melisandre");
        mockMvc.perform(post("/accounts")
                .content(json.jsonString()))
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
        DocumentContext user = getAccount(userID, "user").put("$", "email", "updated@email.com");
        String link = user.read("$._links.self.href").toString();

        mockMvc.perform(patch(link)
                .header("Authorization", "Bearer " + getTokenUser())
                .content(user.jsonString()))
                .andExpect(status().isOk());

        Account updatedUser = accountRepository.findOneByName("user");
        assertEquals("Name does not match", "user", updatedUser.getName());
        assertEquals("Password does not match", "user", updatedUser.getPassword());
        assertEquals("updated@email.com", updatedUser.getEmail());
    }

    @Test
    public void userForbiddenUpdateAnotherAccount() throws Exception {
        DocumentContext admin = getAccount(adminID, "admin").put("$", "email", "updated@email.com");
        String link = admin.read("$._links.self.href").toString();

        mockMvc.perform(patch(link)
                .header("Authorization", "Bearer " + getTokenUser())
                .content(admin.jsonString()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void adminCanUpdateAnotherAccount() throws Exception {
        DocumentContext user = getAccount(userID, "user").put("$", "email", "updated@email.com");
        String link = user.read("$._links.self.href").toString();

        mockMvc.perform(patch(link)
                .header("Authorization", "Bearer " + getTokenAdmin())
                .content(user.jsonString()))
                .andExpect(status().isOk());

        Account updatedUser = accountRepository.findOneByName("user");
        assertEquals("Name does not match", "user", updatedUser.getName());
        assertEquals("Password does not match", "user", updatedUser.getPassword());
        assertEquals("updated@email.com", updatedUser.getEmail());
    }

    @Test
    public void userCanDeleteOwnAccount() throws Exception {
        String link = getAccount(userID, "user").read("$._links.self.href").toString();
        mockMvc.perform(delete(link)
                .header("Authorization", "Bearer " + getTokenUser()))
                .andExpect(status().isNoContent());

        assertNull(accountRepository.findOneByName("user"));
    }

    @Test
    public void userForbiddenDeleteAnotherAccount() throws Exception {
        String link = getAccount(adminID, "admin").read("$._links.self.href").toString();
        mockMvc.perform(delete(link)
                .header("Authorization", "Bearer " + getTokenUser()))
                .andExpect(status().isForbidden());

        assertNotNull(accountRepository.findOneByName("admin"));
    }

    @Test
    public void adminCanDeleteAnotherAccount() throws Exception {
        String link = getAccount(userID, "user").read("$._links.self.href").toString();
        mockMvc.perform(delete(link)
                .header("Authorization", "Bearer " + getTokenAdmin()))
                .andExpect(status().isNoContent());

        assertNull(accountRepository.findOneByName("user"));
    }
}
