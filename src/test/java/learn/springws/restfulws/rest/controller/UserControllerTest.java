package learn.springws.restfulws.rest.controller;

import learn.springws.restfulws.rest.model.response.UserRest;
import learn.springws.restfulws.service.UserService;
import learn.springws.restfulws.shared.dto.AddressDto;
import learn.springws.restfulws.shared.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;

    @Test
    void getUser() {
        // Given
        final String publicId = "public-id";
        UserDto toReturn = new UserDto();
        toReturn.setUserId(publicId);
        toReturn.setAddresses(List.of(new AddressDto(), new AddressDto()));
        given(userService.getUserByPublicId(publicId)).willReturn(toReturn);
        // When
        final UserRest user = userController.getUser(publicId);
        // Then
        then(userService).should().getUserByPublicId(publicId);
        assertNotNull(user);
        assertEquals(publicId, user.getUserId());
        assertEquals(toReturn.getAddresses().size(), user.getAddresses().size());
    }
}
