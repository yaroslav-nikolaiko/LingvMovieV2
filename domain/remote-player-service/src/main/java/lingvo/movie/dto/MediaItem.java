package lingvo.movie.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaroslav on 01.03.15.
 */

@Data
@ToString(of = {"name", "displayPath"}) @EqualsAndHashCode(of = {"name", "displayPath"})
public class MediaItem {
    Long id;
    String name;
    String displayPath;
    Map<String, String> metaInfo = new HashMap<>();
}
