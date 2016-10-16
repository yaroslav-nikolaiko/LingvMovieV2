package lingvo.movie.dao;

import lingvo.movie.entity.Dictionary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yaroslav on 10.05.16.
 */
@Repository
public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {

    @PreAuthorize("authenticated " +
            "and @securityUtils.extractID(principal)==#id " +
            "or hasAuthority('ADMIN')")
    List<Dictionary> findByAccountId(@Param("id") Long id);
}
