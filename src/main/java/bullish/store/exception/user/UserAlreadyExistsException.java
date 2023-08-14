package bullish.store.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String username) {
        super("User " + username + " already exists");
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler({UserAlreadyExistsException.class})
        @ResponseStatus(HttpStatus.CONFLICT)
        String userExistsHandler(UserAlreadyExistsException ex) {
            return ex.getMessage();
        }
    }
}
