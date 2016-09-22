package lingvo.movie.dao;

import lingvo.movie.entity.Account;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

/**
 * Created by yaroslav on 10.05.16.
 */
@Repository
public interface AccountRepository extends SecureCrudRepository<Account> {
    @RestResource(exported = false)
    Account findOneByName(String name);

    @RestResource(exported = false)
    Account findOneByEmail(String email);
}
