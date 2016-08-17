package lingvo.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by yaroslav on 07.03.16.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DictionaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DictionaryApplication.class, args);
    }

    @RestController
    @RequestMapping(path = "hello")
    public static class HelloWorld{

        @PreAuthorize("hasAuthority('READ')")
        @RequestMapping(method = GET)
        public String helloWorld() {
            return "Hello Dictionary Service";
        }
    }
}
