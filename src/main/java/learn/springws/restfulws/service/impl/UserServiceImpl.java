package learn.springws.restfulws.service.impl;

import learn.springws.restfulws.data.entity.UserEntity;
import learn.springws.restfulws.data.repository.UserRepository;
import learn.springws.restfulws.rest.model.response.ErrorMessages;
import learn.springws.restfulws.service.UserService;
import learn.springws.restfulws.exceptions.UserServiceException;
import learn.springws.restfulws.shared.Utils;
import learn.springws.restfulws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

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
            throw new UserServiceException(ErrorMessages.USER_ALREADY_EXISTS);
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

    @Override
    public UserDto updateUser(String userId, UserDto dto) {
        Optional<UserEntity> byUserId = userRepository.findByUserId(userId);
        if (byUserId.isEmpty()) {
            throw new UserServiceException(ErrorMessages.NO_USER_FOUND);
        }
        UserEntity entity = byUserId.get();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        UserEntity updated = userRepository.save(entity);
        UserDto result = new UserDto();
        BeanUtils.copyProperties(updated, result);
        return result;
    }

    @Override
    public void deleteUser(String userId) {
        Optional<UserEntity> byUserId = userRepository.findByUserId(userId);
        if (byUserId.isEmpty()) {
            throw new UserServiceException(ErrorMessages.NO_USER_FOUND);
        }
        userRepository.deleteById(byUserId.get().getId());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Cannot find user by email " + email));
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public UserDto getUserByPublicId(String publicId) {
        UserEntity entity = userRepository.findByUserId(publicId)
                .orElseThrow(() -> new RuntimeException("Cannot find user by public id " + publicId));
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * Locates the user based on the email.
     * @param email in the case of this application the username is email address actually
     * @return a fully populated user record
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Can not find user by email " + email));
        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(), userEntity.getEncryptedPassword(), Collections.emptyList());
    }
}
