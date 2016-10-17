package lingvo.movie;

import lingvo.movie.dto.UserDictionary;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by yaroslav on 16.10.16.
 */
@FeignClient(url = "http://server/api/dictionary-service",path = "/dictionaries", name = "dictionary-service")
public interface DictionaryService {
    /*@RequestMapping(value = "/dictionary-service/", method = GET)
    Resources<UserDictionary> get(List<String> languages);*/

    @RequestMapping(method = GET)
    Resources<UserDictionary> get(Long id);
}
