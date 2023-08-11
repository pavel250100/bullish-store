package bullish.store.exception.stock;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(Long productId) {
        super("Could not find stock for product id " + productId);
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler({StockNotFoundException.class})
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String productNotFoundHandler(StockNotFoundException ex) {
            return ex.getMessage();
        }
    }
}