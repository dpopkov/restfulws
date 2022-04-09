package learn.springws.restfulws.service;

import learn.springws.restfulws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);

    UserDto getUserByEmail(String email);

    UserDto getUserByPublicId(String publicId);

    UserDto updateUser(String userId, UserDto user);
}
