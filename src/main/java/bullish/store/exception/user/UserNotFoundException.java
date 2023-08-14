package bullish.store.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserNotFoundException extends UsernameNotFoundException {

    public UserNotFoundException(String username) {
        super("Could not find user " + username);
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler({UserNotFoundException.class})
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String userNotFoundHandler(UserNotFoundException ex) {
            return ex.getMessage();
        }
    }
}