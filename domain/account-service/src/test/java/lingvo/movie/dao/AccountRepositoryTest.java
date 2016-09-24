package lingvo.movie.dao;

import lingvo.movie.AbstractTest;
import lingvo.movie.entity.Account;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.Assert.*;

/**
 * Created by yaroslav on 20.09.16.
 */
@DataJpaTest
public class AccountRepositoryTest extends AbstractTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuthorityRepository authorityRepository;

    @Test
    public void findById() throws Exception {
        Account admin = accountRepository.findOne(adminID);
        assertNotNull(admin);
        assertEquals("admin",admin.getName());
    }

    @Test
    public void findByName() throws Exception {
        Account admin = accountRepository.findOneByName("admin");
        assertNotNull(admin);
        assertEquals("admin",admin.getName());
    }

    @Test
    public void findByNonExistingNameShouldReturnNull() throws Exception {
        Account admin = accountRepository.findOneByName("wrong name");
        assertNull(admin);
    }

    @Test
    public void createAccount() throws Exception {
        Account account = new Account();
        account.setName("Jon Snow");
        account.setEmail("snow@winterfell.com");
        account.setPassword("crow");
        account.setAuthorities(authorityRepository.getAuthorities("USER"));
        account = accountRepository.save(account);
        assertNotNull(account.getId());
    }
}