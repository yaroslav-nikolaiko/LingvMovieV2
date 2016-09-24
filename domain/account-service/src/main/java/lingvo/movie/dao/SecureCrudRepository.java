package lingvo.movie.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by yaroslav on 22.09.16.
 */

@NoRepositoryBean
public interface SecureCrudRepository <T> extends CrudRepository<T, Long> {
    @PreAuthorize("#entity.id==null " +
            "or hasAuthority('ADMIN') " +
            "or (authenticated and principal?.id==#entity.id)")
    @Override
    <S extends T> S save(S entity);

    @Override
    <S extends T> Iterable<S> save(Iterable<S> entities);


    @PreAuthorize("authenticated " +
            "and principal?.id==#id " +
            "or hasAuthority('ADMIN')")
    @Override
    T findOne(@Param("id") Long id);

    @Override
    boolean exists(Long id);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    Iterable<T> findAll();

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    Iterable<T> findAll(Iterable<Long> ids);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    long count();

    @Override
    @PreAuthorize("authenticated " +
            "and principal?.id==#id " +
            "or hasAuthority('ADMIN')")
    void delete(@Param("id") Long id);

    @Override
    @PreAuthorize("authenticated " +
            "and principal?.id==#entity.id " +
            "or hasAuthority('ADMIN')")
    void delete(T entity);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    void delete(Iterable<? extends T> entities);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    void deleteAll();
}
