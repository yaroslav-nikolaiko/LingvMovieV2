package lingvo.movie.aop;

import lingvo.movie.dao.AuthorityRepository;
import lingvo.movie.entity.Account;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yaroslav on 24.09.16.
 */
@Aspect
@Component
public class AccountRepositoryInterceptor {
    @Autowired
    AuthorityRepository authorityRepository;

    @Before("execution(* lingvo.movie.dao.AccountRepository.save(..))")
    public void setAuthorities(JoinPoint joinPoint) {
        Account account = (Account) joinPoint.getArgs()[0];
        if(account.getId() == null)
            account.setAuthorities(authorityRepository.getAuthorities("USER"));
    }
}
