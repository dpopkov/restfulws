package learn.springws.restfulws.exceptions;

import learn.springws.restfulws.rest.model.response.ErrorMessages;

public class AddressServiceException extends RuntimeException {

    public AddressServiceException(ErrorMessages message) {
        super(message.getErrorMessage());
    }
}
