package lingvo.movie.security;

import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Created by yaroslav on 16.10.16.
 */
@Component("securityUtils")
public class SecurityUtils {
    public static Long extractID(String name){
        String[] name_id = name.split("-");
        return Long.valueOf(name_id[name_id.length - 1]);
    }

    public static Long extractID(Principal principal){
        return extractID(principal.getName());
    }

    public static String extractToken(Principal principal){
        //if(principal.getClass().equals("sdfdfg"))
        return "sdfsdf";

    }
}
