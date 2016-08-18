package lingvo.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by yaroslav on 07.03.16.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WebappApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebappApplication.class, args);
    }

/*    @RestController
    @RequestMapping(path = "hello-ui")
    public static class HelloWorld{

        @RequestMapping(method = GET)
        public String helloWorld() {
            return "Hello Webapp Service";
        }
    }*/

    @Controller
    public static class ViewResolver{
        @RequestMapping(value = {"/users/**", "/posts/**"}, method = RequestMethod.GET)
        public String index(){
            return "/index.html";
        }
    }
}
