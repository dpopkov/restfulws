package learn.springws.restfulws.rest.controller;

import learn.springws.restfulws.rest.model.request.UserDetailsRequestModel;
import learn.springws.restfulws.rest.model.response.ErrorMessages;
import learn.springws.restfulws.rest.model.response.UserRest;
import learn.springws.restfulws.service.UserService;
import learn.springws.restfulws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

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
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if (userDetails.getFirstName().isBlank()) {
            throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }
        UserRest returnUser = new UserRest();
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(userDetails, dto);
        UserDto created = userService.createUser(dto);
        BeanUtils.copyProperties(created, returnUser);
        return returnUser;
    }

    @PutMapping
    public String updateUser() {
        return "updateUser was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "deleteUser was called";
    }
}
