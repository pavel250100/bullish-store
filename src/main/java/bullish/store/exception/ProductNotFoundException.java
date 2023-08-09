package bullish.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Could not find product " + id);
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler(ProductNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String productNotFoundHandler(ProductNotFoundException ex) {
            return ex.getMessage();
        }
    }
}
