package learn.springws.restfulws.service.impl;

import learn.springws.restfulws.data.entity.UserEntity;
import learn.springws.restfulws.data.repository.UserRepository;
import learn.springws.restfulws.exceptions.UserServiceException;
import learn.springws.restfulws.rest.model.response.ErrorMessages;
import learn.springws.restfulws.shared.Utils;
import learn.springws.restfulws.shared.dto.AddressDto;
import learn.springws.restfulws.shared.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    Utils utils;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    UserServiceImpl userService;
    @Captor
    ArgumentCaptor<UserEntity> entityCaptor;

    @Test
    void testGetUserByEmail() {
        // Given
        final Long id = 12L;
        final String userId = "1234abc";
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUserId(userId);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        // When
        UserDto userDto = userService.getUserByEmail("email");
        // Then
        assertNotNull(userDto);
        assertEquals(id, userDto.getId());
        assertEquals(userId, userDto.getUserId());
    }

    @Test
    void testGetUserByEmailWhenUserNotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Then
        assertThrows(UserServiceException.class,
                () -> userService.getUserByEmail("email"),
                ErrorMessages.NO_USER_FOUND.getErrorMessage());
    }

    @Test
    void testCreateUser() {
        // Given
        final Long id = 12L;
        final String userId = "1234abc";
        final String password = "password";
        final String encryptedPassword = "asd89ad76dadh3";
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUserId(userId);
        userEntity.setFirstName("Jane");
        userEntity.setLastName("Doe");
        userEntity.setEncryptedPassword(encryptedPassword);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(utils.generateUserId(anyInt())).willReturn(userId);
        given(bCryptPasswordEncoder.encode(anyString())).willReturn(encryptedPassword);
        given(userRepository.save(any(UserEntity.class))).willReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setEmail("email");
        userDto.setPassword(password);
        userDto.setAddresses(List.of(new AddressDto(), new AddressDto()));

        // When
        final UserDto created = userService.createUser(userDto);

        // Then
        assertNotNull(created);
        assertEquals(userId, created.getUserId());
        assertEquals(userEntity.getFirstName(), created.getFirstName());
        assertEquals(userEntity.getLastName(), created.getLastName());
        assertEquals(encryptedPassword, created.getEncryptedPassword());
        then(utils).should().generateUserId(anyInt());
        then(bCryptPasswordEncoder).should().encode(password);
        then(utils).should(times(userDto.getAddresses().size())).generatePublicId();
        then(userRepository).should().save(entityCaptor.capture());
        assertEquals(userId, entityCaptor.getValue().getUserId());
    }

    @Test
    void testCreateUser_whenUserExists_thenThrowException() {
        // Given
        given(userRepository.findByEmail(any())).willReturn(Optional.of(new UserEntity()));
        // Then
        assertThrows(UserServiceException.class,
                () -> userService.createUser(new UserDto()),
                ErrorMessages.USER_ALREADY_EXISTS.getErrorMessage());
    }
}
