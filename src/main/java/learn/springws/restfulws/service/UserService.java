package learn.springws.restfulws.service;

import learn.springws.restfulws.shared.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto user);
}
