package bullish.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ProductHasBeenChangedException extends RuntimeException {

    public ProductHasBeenChangedException(Long productId) {
        super("Product " + productId + " has been changed");
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler({ProductHasBeenChangedException.class})
        @ResponseStatus(HttpStatus.CONFLICT)
        String productNotFoundHandler(ProductHasBeenChangedException ex) {
            return ex.getMessage();
        }
    }

}
