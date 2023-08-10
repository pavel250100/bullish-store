package bullish.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class StockHasBeenChangedException extends RuntimeException {

    public StockHasBeenChangedException(Long productId) {
        super("Stock for product " + productId + " has been changed");
    }

    @ControllerAdvice
    public static class Advice {
        @ResponseBody
        @ExceptionHandler({StockHasBeenChangedException.class})
        @ResponseStatus(HttpStatus.CONFLICT)
        String productNotFoundHandler(StockHasBeenChangedException ex) {
            return ex.getMessage();
        }
    }
}
