package lingvo.movie.dao;

import lingvo.movie.entity.Dictionary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yaroslav on 10.05.16.
 */
@Repository
public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {

}
