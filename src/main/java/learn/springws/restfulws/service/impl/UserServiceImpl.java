package learn.springws.restfulws.service.impl;

import learn.springws.restfulws.data.entity.UserEntity;
import learn.springws.restfulws.data.repository.UserRepository;
import learn.springws.restfulws.service.UserService;
import learn.springws.restfulws.shared.Utils;
import learn.springws.restfulws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final int PUBLIC_USER_ID_LENGTH = 32;

    private final UserRepository userRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        entity.setEncryptedPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        UserEntity storedUser = userRepository.save(entity);
        UserDto result = new UserDto();
        BeanUtils.copyProperties(storedUser, result);
        return result;
    }
}
