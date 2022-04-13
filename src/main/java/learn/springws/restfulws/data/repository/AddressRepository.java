package learn.springws.restfulws.data.repository;

import learn.springws.restfulws.data.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

    Optional<AddressEntity> findByPublicId(String publicId);
}
