package lingvo.movie.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by yaroslav on 22.09.16.
 */

//TODO: Secure all methods

@NoRepositoryBean
public interface SecureCrudRepository <T> extends CrudRepository<T, Long> {
    @Override @PreAuthorize("authenticated " +
            "and principal?.id==#id " +
            "or hasAuthority('ADMIN') " +
            "or entity.id==null")
    <S extends T> S save(S entity);
    @Override
    <S extends T> Iterable<S> save(Iterable<S> entities);
    @Override @PreAuthorize("authenticated " +
            "and principal?.id==#id " +
            "or hasAuthority('ADMIN') ")
    T findOne(@Param("id") Long id);
    @Override
    boolean exists(Long id);
    @Override
    Iterable<T> findAll();
    @Override
    Iterable<T> findAll(Iterable<Long> ids);
    @Override
    long count();
    @Override
    void delete(Long id);
    @Override
    void delete(T entity);
    @Override
    void delete(Iterable<? extends T> entities);
    @Override
    void deleteAll();
}
