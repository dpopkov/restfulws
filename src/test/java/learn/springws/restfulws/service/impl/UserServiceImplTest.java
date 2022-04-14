package learn.springws.restfulws.service.impl;

import learn.springws.restfulws.data.entity.UserEntity;
import learn.springws.restfulws.data.repository.UserRepository;
import learn.springws.restfulws.exceptions.UserServiceException;
import learn.springws.restfulws.rest.model.response.ErrorMessages;
import learn.springws.restfulws.shared.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

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
}
