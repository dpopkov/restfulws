package learn.springws.restfulws.rest.controller;

import learn.springws.restfulws.exceptions.UserServiceException;
import learn.springws.restfulws.rest.model.request.UserDetailsRequestModel;
import learn.springws.restfulws.rest.model.response.ErrorMessages;
import learn.springws.restfulws.rest.model.response.UserRest;
import learn.springws.restfulws.service.UserService;
import learn.springws.restfulws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import static learn.springws.restfulws.shared.Utils.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userPublicId}")
    public UserRest getUser(@PathVariable String userPublicId) {
        UserRest returnUser = new UserRest();
        UserDto dto = userService.getUserByPublicId(userPublicId);
        BeanUtils.copyProperties(dto, returnUser);
        return returnUser;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel user) {
        if (anyFieldIsMissing(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword())) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        UserRest returnUser = new UserRest();
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        UserDto created = userService.createUser(dto);
        BeanUtils.copyProperties(created, returnUser);
        return returnUser;
    }

    @PutMapping("/{userPublicId}")
    public UserRest updateUser(@PathVariable String userPublicId, @RequestBody UserDetailsRequestModel user) {
        if (anyFieldIsMissing(user.getFirstName(), user.getLastName())) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        UserDto updated = userService.updateUser(userPublicId, dto);
        UserRest returnUser = new UserRest();
        BeanUtils.copyProperties(updated, returnUser);
        return returnUser;
    }

    @DeleteMapping
    public String deleteUser() {
        return "deleteUser was called";
    }
}
