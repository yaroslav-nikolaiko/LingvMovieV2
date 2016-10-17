package lingvo.movie;

import lingvo.movie.dto.VideoMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

/**
 * Created by yaroslav on 16.10.16.
 */

@Controller
public class RemotePlayerController {
    @Autowired
    RemotePlayerService remotePlayerService;

    //@MessageMapping("/track/start")
    public void trackStart(VideoMeta message, Principal principal, MessageHeaders headers) {
        /*OAuth2Authentication authentication = (OAuth2Authentication) principal;
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String token = details.getTokenValue();
        headers.put("auth_token", token);*/
        List<String> languages = message.getAudioLanguages();
        languages.addAll(message.getSubtitlesLanguages());
        //UserDictionary dictionary = remotePlayerService.findDictionary(languages);

    }
}
