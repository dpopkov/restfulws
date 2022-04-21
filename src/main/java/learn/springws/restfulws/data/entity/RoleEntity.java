package learn.springws.restfulws.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@Entity(name = "roles")
public class RoleEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;
}
