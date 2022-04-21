package learn.springws.restfulws.data.repository;

import learn.springws.restfulws.data.entity.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

    Optional<AuthorityEntity> findByName(String name);
}
