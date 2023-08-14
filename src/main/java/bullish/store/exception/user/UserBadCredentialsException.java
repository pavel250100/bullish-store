package bullish.store.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserBadCredentialsException extends RuntimeException {

    public UserBadCredentialsException() {
        super("Invalid username or password");
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler({UserBadCredentialsException.class})
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        String userExistsHandler(UserBadCredentialsException ex) {
            return ex.getMessage();
        }
    }
}
