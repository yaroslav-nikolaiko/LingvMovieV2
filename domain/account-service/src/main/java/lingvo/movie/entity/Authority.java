package lingvo.movie.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    public Authority(String role){
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
