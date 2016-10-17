package lingvo.movie.rest;

import lingvo.movie.dao.DictionaryRepository;
import lingvo.movie.entity.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static lingvo.movie.security.SecurityUtils.extractID;

/**
 * Created by yaroslav on 17.10.16.
 */
@RestController
public class DictionaryController {
    @Autowired
    DictionaryRepository dictionaryRepository;

    @RequestMapping(path = "search/languages")
    @PreAuthorize("authenticated")
    public Dictionary findDictionaryByLanguages(@RequestParam List<String> languages, Principal principal) {
        List<Dictionary> dictionaries = dictionaryRepository.findByAccountId(extractID(principal));
        System.out.println(dictionaries);
        return dictionaries.get(0);
    }
}
