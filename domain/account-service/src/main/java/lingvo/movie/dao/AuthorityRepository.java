package lingvo.movie.dao;

import lingvo.movie.entity.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by yaroslav on 21.09.16.
 */
@RestResource(exported = false)
@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long>{
    List<Authority> findByRoleIn(Collection<String> roles);

    default List<Authority> getAuthorities(String... roles){
        return findByRoleIn(Arrays.asList(roles));
    }
}
