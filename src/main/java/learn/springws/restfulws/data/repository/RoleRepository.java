package learn.springws.restfulws.data.repository;

import learn.springws.restfulws.data.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);
}
