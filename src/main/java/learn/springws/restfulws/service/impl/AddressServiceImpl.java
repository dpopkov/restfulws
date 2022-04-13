package learn.springws.restfulws.service.impl;

import learn.springws.restfulws.data.entity.AddressEntity;
import learn.springws.restfulws.data.entity.UserEntity;
import learn.springws.restfulws.data.repository.AddressRepository;
import learn.springws.restfulws.data.repository.UserRepository;
import learn.springws.restfulws.exceptions.AddressServiceException;
import learn.springws.restfulws.exceptions.UserServiceException;
import learn.springws.restfulws.rest.model.response.ErrorMessages;
import learn.springws.restfulws.service.AddressService;
import learn.springws.restfulws.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressDto> getAddresses(String userPublicId) {
        UserEntity userEntity = userRepository.findByUserId(userPublicId)
                .orElseThrow(() -> new UserServiceException(ErrorMessages.NO_USER_FOUND));
        List<AddressEntity> addresses = userEntity.getAddresses();
        if (addresses == null || addresses.isEmpty()) {
            return List.of();
        }
        Type typeOfList = new TypeToken<List<AddressDto>>() {}.getType();
        return new ModelMapper().map(addresses, typeOfList);
    }

    @Override
    public AddressDto getAddress(String addressPublicId) {
        AddressEntity entity = addressRepository.findByPublicId(addressPublicId)
                .orElseThrow(() -> new AddressServiceException(ErrorMessages.NO_RECORD_FOUND));
        return new ModelMapper().map(entity, AddressDto.class);
    }
}
