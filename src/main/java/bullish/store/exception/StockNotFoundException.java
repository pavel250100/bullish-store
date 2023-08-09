package bullish.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(String message) {
        super(message);
    }
    public StockNotFoundException(Long id) {
        super("Could not find stock " + id);
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