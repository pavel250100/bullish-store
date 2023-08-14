package bullish.store.exception.cart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(Long productId, String username) {
        super("Cannot find product " + productId + " in user's + " + username + " cart");
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler({CartItemNotFoundException.class})
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String notEnoughStockHandler(CartItemNotFoundException ex) {
            return ex.getMessage();
        }
    }
}