package learn.springws.restfulws.service.impl;

import learn.springws.restfulws.data.entity.UserEntity;
import learn.springws.restfulws.data.repository.UserRepository;
import learn.springws.restfulws.service.UserService;
import learn.springws.restfulws.shared.Utils;
import learn.springws.restfulws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final int PUBLIC_USER_ID_LENGTH = 32;

    private final UserRepository userRepository;
    private final Utils utils;

    public UserServiceImpl(UserRepository userRepository, Utils utils) {
        this.userRepository = userRepository;
        this.utils = utils;
    }

    @Override
    public UserDto createUser(UserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(dto, entity);
        String publicUserId = utils.generateUserId(PUBLIC_USER_ID_LENGTH);
        entity.setUserId(publicUserId);
        entity.setEncryptedPassword("test");
        UserEntity storedUser = userRepository.save(entity);
        UserDto result = new UserDto();
        BeanUtils.copyProperties(storedUser, result);
        return result;
    }
}
