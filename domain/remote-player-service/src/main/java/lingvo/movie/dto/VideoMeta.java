package lingvo.movie.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yaroslav on 16.10.16.
 */
@Data
@EqualsAndHashCode(of = "hash")
public class VideoMeta {
    String hash;
    Long size;
    List<String> audioLanguages = new ArrayList<>();
    List<String> subtitlesLanguages = new ArrayList<>();
    Map<String,String> meta;
}
