package bullish.store.exception.cart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CartNotEnoughStockException extends RuntimeException {

    public CartNotEnoughStockException(Long productId) {
        super("Cannot add product " + productId + " to cart due to not enough stock");
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler({CartNotEnoughStockException.class})
        @ResponseStatus(HttpStatus.CONFLICT)
        String notEnoughStockHandler(CartNotEnoughStockException ex) {
            return ex.getMessage();
        }
    }
}
