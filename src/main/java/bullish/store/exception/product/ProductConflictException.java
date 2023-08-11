package bullish.store.exception.product;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.communication.product.Product;
import bullish.store.entity.ProductEntity;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class ProductConflictException extends RuntimeException {

    private final ProductEntity latestProductEntity;

    public ProductConflictException(ProductEntity latestProductEntity) {
        super("Product " + latestProductEntity.getId() + " has been changed");
        this.latestProductEntity = latestProductEntity;
    }

    @ControllerAdvice
    public static class Advice {
        private final ProductModelAssembler assembler;

        public Advice(ProductModelAssembler assembler) {
            this.assembler = assembler;
        }

        @ResponseBody
        @ExceptionHandler({ProductConflictException.class})
        @ResponseStatus(HttpStatus.CONFLICT)
        ResponseEntity<EntityModel<Product>> productConflictHandler(ProductConflictException ex) {
            Product dto = Product.toDto(ex.getLatestProductEntity());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(assembler.toModel(dto));
        }
    }

}
