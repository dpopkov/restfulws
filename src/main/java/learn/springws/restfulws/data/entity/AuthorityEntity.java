package learn.springws.restfulws.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "authorities")
public class AuthorityEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 32)
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Collection<RoleEntity> roles = new ArrayList<>();

    public AuthorityEntity(String name) {
        this.name = name;
    }
}
