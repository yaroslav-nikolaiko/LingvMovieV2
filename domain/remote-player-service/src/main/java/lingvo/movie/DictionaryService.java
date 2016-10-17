package lingvo.movie;

import lingvo.movie.dto.UserDictionary;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by yaroslav on 16.10.16.
 */
@FeignClient(url = "${app.service.dictionary-service}", name = "dictionary-service")
public interface DictionaryService {
    /*@RequestMapping(value = "/dictionary-service/", method = GET)
    Resources<UserDictionary> get(List<String> languages);*/

    @RequestMapping(method = GET, path = "/search/languages")
    UserDictionary get(@RequestParam("languages") List<String> languages);
}
