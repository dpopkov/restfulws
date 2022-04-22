package learn.springws.restfulws.exceptions;

import learn.springws.restfulws.rest.model.response.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
        log.trace("handleUserServiceException(..) : message = {}", ex.getMessage());
        return responseEntity(ex);
    }

    // Commented out in order to continue to receive status 403 when user is not authorized,
    // otherwise it is 500.
    /*@ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        log.trace("handleOtherExceptions(..) : message = {}", ex.getMessage());
        return responseEntity(ex);
    }*/

    private ResponseEntity<Object> responseEntity(Exception ex) {
        return new ResponseEntity<>(
                new ErrorMessage(new Date(), ex.getMessage()),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
