package lingvo.movie.dao;

import lingvo.movie.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yaroslav on 10.05.16.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}
