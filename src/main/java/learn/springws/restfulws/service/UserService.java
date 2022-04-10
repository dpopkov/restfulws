package learn.springws.restfulws.service;

import learn.springws.restfulws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<UserDto> getUsers(int page, int limit);

    UserDto createUser(UserDto user);

    UserDto getUserByEmail(String email);

    UserDto getUserByPublicId(String publicId);

    UserDto updateUser(String userId, UserDto user);

    void deleteUser(String userId);
}
