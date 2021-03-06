package lingvo.movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lingvo.movie.entity.lookup.Language;
import lingvo.movie.entity.lookup.Level;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yaroslav on 10.05.16.
 */
@Entity
@Table(name = "DICTIONARY", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Data @ToString(of = {"name"}) @EqualsAndHashCode(of = {"name", "learningLanguage", "nativeLanguage", "level"})
public class Dictionary {
    @Id @GeneratedValue
    Long id;

    @Column(nullable = false)
    Long accountId;

    @Size(max=20)
    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Language learningLanguage;

    @Enumerated(EnumType.STRING)
    Language nativeLanguage;

    @Enumerated(EnumType.STRING)
    Level level;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "dictionary")
    List<MediaItem> mediaItems = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dictionary_id")
    @XmlTransient
    Set<WordCell> wordCells = new HashSet<>();
}
