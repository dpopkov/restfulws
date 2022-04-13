package learn.springws.restfulws.rest.controller;

import learn.springws.restfulws.exceptions.UserServiceException;
import learn.springws.restfulws.rest.model.OperationName;
import learn.springws.restfulws.rest.model.request.UserDetailsRequestModel;
import learn.springws.restfulws.rest.model.response.*;
import learn.springws.restfulws.service.AddressService;
import learn.springws.restfulws.service.UserService;
import learn.springws.restfulws.shared.dto.AddressDto;
import learn.springws.restfulws.shared.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static learn.springws.restfulws.shared.Utils.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    public UserController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "limit", defaultValue = "5") int limit) {
        page = page - 1; // number of page starts from 0, not from 1 (zero indexing)
        List<UserDto> usersDto = userService.getUsers(page, limit);
        return usersDto.stream().map(this::dtoToRest).collect(Collectors.toList());
    }

    @GetMapping("/{userPublicId}")
    public UserRest getUser(@PathVariable String userPublicId) {
        UserDto dto = userService.getUserByPublicId(userPublicId);
        return dtoToRest(dto);
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel user) {
        if (anyFieldIsMissing(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword())) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        ModelMapper mapper = new ModelMapper();
        UserDto dto = mapper.map(user, UserDto.class);
        UserDto created = userService.createUser(dto);
        UserRest returnUser = mapper.map(created, UserRest.class);
        log.debug("Created User with public ID {}", returnUser.getUserId());
        return returnUser;
    }

    @PutMapping("/{userPublicId}")
    public UserRest updateUser(@PathVariable String userPublicId, @RequestBody UserDetailsRequestModel user) {
        if (anyFieldIsMissing(user.getFirstName(), user.getLastName())) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        UserDto dto = requestModelToDto(user);
        UserDto updated = userService.updateUser(userPublicId, dto);
        return dtoToRest(updated);
    }

    @DeleteMapping("/{userPublicId}")
    public OperationStatus deleteUser(@PathVariable String userPublicId) {
        userService.deleteUser(userPublicId);
        return new OperationStatus(OperationResult.SUCCESS, OperationName.DELETE);
    }

    private UserRest dtoToRest(UserDto dto) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(dto, UserRest.class);
    }

    private UserDto requestModelToDto(UserDetailsRequestModel requestModel) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(requestModel, UserDto.class);
    }

    @GetMapping("/{userPublicId}/addresses")
    public CollectionModel<AddressRest> getAddressesForUser(@PathVariable String userPublicId) {
        List<AddressDto> addressesDto = addressService.getAddresses(userPublicId);
        java.lang.reflect.Type typeOfList = new TypeToken<List<AddressRest>>() {}.getType();
        final List<AddressRest> addresses = new ModelMapper().map(addressesDto, typeOfList);
        return CollectionModel.of(addresses,
                linkTo(methodOn(UserController.class).getUser(userPublicId)).withRel("user"),
                linkTo(methodOn(UserController.class).getAddressesForUser(userPublicId)).withSelfRel()
        );
    }

    @GetMapping("/{userPublicId}/addresses/{addressPublicId}")
    public EntityModel<AddressRest> getAddress(@PathVariable String userPublicId, @PathVariable String addressPublicId) {
        AddressDto dto = addressService.getAddress(addressPublicId);
        AddressRest address = new ModelMapper().map(dto, AddressRest.class);
        return EntityModel.of(address,
                linkTo(methodOn(UserController.class).getUser(userPublicId)).withRel("user"),
                linkTo(methodOn(UserController.class).getAddressesForUser(userPublicId)).withRel("addresses"),
                linkTo(methodOn(UserController.class).getAddress(userPublicId, addressPublicId)).withSelfRel()
        );
    }
}
