package bullish.store.exception.stock;

import bullish.store.assembler.StockModelAssembler;
import bullish.store.communication.stock.Stock;
import bullish.store.entity.StockEntity;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class StockConflictException extends RuntimeException {

    private final StockEntity latestStockEntity;

    public StockConflictException(StockEntity latestStockEntity) {
        super("Conflict detected while updating stock for product " + latestStockEntity.getProductId());
        this.latestStockEntity = latestStockEntity;
    }

    @ControllerAdvice
    public static class Advice {

        private final StockModelAssembler assembler;

        public Advice(StockModelAssembler assembler) {
            this.assembler = assembler;
        }

        @ResponseBody
        @ExceptionHandler({StockConflictException.class})
        @ResponseStatus(HttpStatus.CONFLICT)
        ResponseEntity<EntityModel<Stock>> stockConflictHandler(StockConflictException ex) {
            Stock dto = Stock.toDto(ex.getLatestStockEntity());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(assembler.toModel(dto));
        }
    }
}
