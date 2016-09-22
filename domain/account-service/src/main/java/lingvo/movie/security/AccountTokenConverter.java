package lingvo.movie.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lingvo.movie.dao.AuthorityRepository;
import lingvo.movie.entity.Account;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
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
        ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        Account account = mapper.convertValue(map.get("account"), Account.class);
/*        Account account = new Account();
        account.setName(map.get());
        account.setAuthorities(authorityRepository.getAuthorities(extractAuthorities(map)));*/
        return new UsernamePasswordAuthenticationToken(account, "N/A", account.getAuthorities());
    }

/*    private String[] extractAuthorities(Map<String, ?> map) {
        if (!map.containsKey(AUTHORITIES)) {
            return defaultAuthorities;
        }
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }*/
}
