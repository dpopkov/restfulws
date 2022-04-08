package learn.springws.restfulws.rest.model.request;

import lombok.Data;

@Data
public class UserLoginRequestModel {
    private String email;
    private String password;
}
