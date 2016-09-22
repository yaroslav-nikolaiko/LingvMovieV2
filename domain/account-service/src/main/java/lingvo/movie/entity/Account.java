package lingvo.movie.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by yaroslav on 10.05.16.
 */
@Entity
@Table(name = "ACCOUNT")
@Data
@ToString(of = {"name", "email"}) @EqualsAndHashCode(of = {"name", "email"})
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY, getterVisibility= JsonAutoDetect.Visibility.NONE, isGetterVisibility= JsonAutoDetect.Visibility.NONE, setterVisibility= JsonAutoDetect.Visibility.NONE)
public class Account implements UserDetails {
    @Id
    @GeneratedValue
    Long id;

    @Size(max=20)
    @Column(nullable = false, unique = true)
    String name;

    @Size(min=3, max=20)
    @Column(nullable = false)
    @JsonIgnore
    String password;

    @Pattern(regexp = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)", message = "Email is not in valid format")
    @Column(unique = true)
    String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLES")
    List<Authority> authorities;

    boolean enabled = true;

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
