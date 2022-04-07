package learn.springws.restfulws.data.repository;

import learn.springws.restfulws.data.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
