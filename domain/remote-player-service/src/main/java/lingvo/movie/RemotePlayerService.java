package lingvo.movie;

import lingvo.movie.dto.UserDictionary;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.List;

/**
 * Created by yaroslav on 16.10.16.
 */
@Service
public class RemotePlayerService {
    public UserDictionary findDictionary(Long userID, List<String> languages){
        return null;
    }
}
