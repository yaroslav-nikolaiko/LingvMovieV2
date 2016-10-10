package lingvo.movie;

/**
 * Created by yaroslav on 10.10.16.
 */
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.net.InetAddress;
import java.security.Principal;
import java.util.Date;

@Controller
public class GreetingController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
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
        Thread.sleep(1000); // simulated delay
        InetAddress ip;
        String hostname;
        ip = InetAddress.getLocalHost();
        hostname = ip.getHostName();

        return new Greeting("Bonjur " + principal.getName());

/*        return new Greeting("User Greetings Hello, " + message.getName() + "! \n"+
                "IP = " + ip + "  HOST = " + hostname + "  DATE=" + new Date());*/
    }

}
