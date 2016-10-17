package lingvo.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.hateoas.config.EnableHypermediaSupport;

/**
 * Created by yaroslav on 10.10.16.
 */
@SpringBootApplication
@EnableFeignClients
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}