package lingvo.movie.dao;

import lingvo.movie.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
 * Created by yaroslav on 10.05.16.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    @RestResource(path = "/admin/search/name")
    Account findOneByName(String name);

    @RestResource(path = "/admin/search/email")
    Account findOneByEmail(String email);
}
