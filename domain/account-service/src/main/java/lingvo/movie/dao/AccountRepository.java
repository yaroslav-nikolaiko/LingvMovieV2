package lingvo.movie.dao;

import lingvo.movie.entity.Account;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;
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



    @Aspect
    @Component
    class AccountRepositoryInterceptor {
        @Autowired
        AuthorityRepository authorityRepository;

        @Before("execution(* AccountRepository.save(..))")
        public void setAuthorities(JoinPoint joinPoint) {
            Account account = (Account) joinPoint.getArgs()[0];
            account.setAuthorities(authorityRepository.getAuthorities("USER"));
        }
    }
}
