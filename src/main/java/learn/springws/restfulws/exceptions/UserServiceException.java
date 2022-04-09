package learn.springws.restfulws.exceptions;

import learn.springws.restfulws.rest.model.response.ErrorMessages;

public class UserServiceException extends RuntimeException {

    public UserServiceException(ErrorMessages message) {
        super(message.getErrorMessage());
    }
}
