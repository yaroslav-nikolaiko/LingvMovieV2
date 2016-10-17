package lingvo.movie.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaroslav on 16.10.16.
 */
@Data
public class UserDictionary {
    Long id;
    String name;
    String learningLanguage;
    String nativeLanguage;
    String level;
    List<MediaItem> mediaItems = new ArrayList<>();
}
