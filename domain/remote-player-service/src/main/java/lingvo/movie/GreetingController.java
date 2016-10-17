package lingvo.movie;

/**
 * Created by yaroslav on 10.10.16.
 */
import lingvo.movie.dto.UserDictionary;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.net.InetAddress;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static java.util.Arrays.stream;
import static lingvo.movie.security.SecurityUtils.extractID;

@Controller
public class GreetingController {

    @Autowired
    DictionaryService dictionaryService;


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message, Principal principal) throws Exception {
        Resources<Object> userDictionaries = dictionaryService.get();
        Thread.sleep(3000); // simulated delay
        InetAddress ip;
        String hostname;
        ip = InetAddress.getLocalHost();
        hostname = ip.getHostName();
        return new Greeting("Hello, " + message.getName() + "! \n"+
                "IP = " + ip + "  HOST = " + hostname + "  DATE=" + new Date());
    }

    @MessageMapping("/user/hello")
    @SendToUser("/queue/greetings")
    public Greeting usergGeeting(HelloMessage message, Principal principal) throws Exception {
        Thread.sleep(3000); // simulated delay
        InetAddress ip;
        String hostname;
        ip = InetAddress.getLocalHost();
        hostname = ip.getHostName();

        return new Greeting("Bonjur " + principal.getName());

/*        return new Greeting("User Greetings Hello, " + message.getName() + "! \n"+
                "IP = " + ip + "  HOST = " + hostname + "  DATE=" + new Date());*/
    }

}
