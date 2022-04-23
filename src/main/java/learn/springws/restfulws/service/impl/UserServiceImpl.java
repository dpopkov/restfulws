package learn.springws.restfulws.service.impl;

import learn.springws.restfulws.data.entity.AddressEntity;
import learn.springws.restfulws.data.entity.RoleEntity;
import learn.springws.restfulws.data.entity.UserEntity;
import learn.springws.restfulws.data.repository.RoleRepository;
import learn.springws.restfulws.data.repository.UserRepository;
import learn.springws.restfulws.rest.model.response.ErrorMessages;
import learn.springws.restfulws.security.UserPrincipal;
import learn.springws.restfulws.service.UserService;
import learn.springws.restfulws.exceptions.UserServiceException;
import learn.springws.restfulws.shared.Utils;
import learn.springws.restfulws.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final int PUBLIC_USER_ID_LENGTH = 32;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /** Returns page of users where number of page starts from 0. */
    @Override
    public List<UserDto> getUsers(int page, int limit) {
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        return usersPage.getContent().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserServiceException(ErrorMessages.USER_ALREADY_EXISTS);
        }
        ModelMapper mapper = new ModelMapper();
        UserEntity entity = mapper.map(dto, UserEntity.class);
        String publicUserId = utils.generateUserId(PUBLIC_USER_ID_LENGTH);
        entity.setUserId(publicUserId);
        entity.setEncryptedPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        if (entity.getAddresses() != null) {
            for (AddressEntity addressEntity : entity.getAddresses()) {
                addressEntity.setUserDetails(entity);
                addressEntity.setPublicId(utils.generatePublicId());
            }
        }

        // Set Roles
        List<RoleEntity> roles = new ArrayList<>();
        for (String role : dto.getRoles()) {
            RoleEntity roleEntity = roleRepository.findByName(role).orElseThrow();
            roles.add(roleEntity);
        }
        entity.setRoles(roles);

        UserEntity storedUser = userRepository.save(entity);
        return mapper.map(storedUser, UserDto.class);
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
        return entityToDto(updated);
    }

    @Override
    public void deleteUser(String userId) {
        Optional<UserEntity> byUserId = userRepository.findByUserId(userId);
        if (byUserId.isEmpty()) {
            throw new UserServiceException(ErrorMessages.NO_USER_FOUND);
        }
        userRepository.deleteById(byUserId.get().getId());
    }

    @Transactional
    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserServiceException(ErrorMessages.NO_USER_FOUND));
        return entityToDto(entity);
    }

    @Override
    public UserDto getUserByPublicId(String publicId) {
        UserEntity entity = userRepository.findByUserId(publicId)
                .orElseThrow(() -> new RuntimeException("Cannot find user by public id " + publicId));
        return entityToDto(entity);
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
        return new UserPrincipal(userEntity);
    }

    private UserDto entityToDto(UserEntity entity) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(entity, UserDto.class);
    }
}
