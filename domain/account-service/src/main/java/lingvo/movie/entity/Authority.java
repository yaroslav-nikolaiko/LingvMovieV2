package lingvo.movie.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Created by yaroslav on 19.09.16.
 */
@Entity
@Table(name = "AUTHORITY")
@Data @NoArgsConstructor
@ToString(of = "role") @EqualsAndHashCode(of = "role")
public class Authority implements GrantedAuthority{
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    String role;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return role;
    }
}
