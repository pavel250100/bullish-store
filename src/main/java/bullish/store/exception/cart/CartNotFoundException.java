package bullish.store.exception.cart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(String username) {
        super("Cannot find cart for user " + username);
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler({CartNotFoundException.class})
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String notEnoughStockHandler(CartNotFoundException ex) {
            return ex.getMessage();
        }
    }
}
