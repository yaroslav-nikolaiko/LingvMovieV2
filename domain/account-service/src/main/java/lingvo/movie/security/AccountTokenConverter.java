package lingvo.movie.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lingvo.movie.dao.AuthorityRepository;
import lingvo.movie.entity.Account;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yaroslav on 28.08.16.
 */
public class AccountTokenConverter extends DefaultUserAuthenticationConverter {
    @Setter
    AuthorityRepository authorityRepository;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        Account account = (Account) authentication.getPrincipal();
        response.put(USERNAME, account.getUsername());
        response.put("id", account.getId());
        response.put("account", account);
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.convertValue(map.get("account"), Account.class);
        return new UsernamePasswordAuthenticationToken(account, "N/A", account.getAuthorities());
    }
}
