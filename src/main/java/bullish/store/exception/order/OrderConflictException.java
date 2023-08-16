package bullish.store.exception.order;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.communication.product.Product;
import bullish.store.entity.ProductEntity;
import bullish.store.exception.product.ProductConflictException;
import lombok.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
public class OrderConflictException extends RuntimeException {

    private final List<ProductEntity> outOfStockProducts;
    private final List<ProductEntity> priceChangedProducts;

    public OrderConflictException(List<ProductEntity> outOfStockProducts, List<ProductEntity> priceChangedProducts) {
        super("Product details were updated");
        this.outOfStockProducts = outOfStockProducts;
        this.priceChangedProducts = priceChangedProducts;
    }

    @ControllerAdvice
    public static class Advice {
        private final ProductModelAssembler assembler;

        public Advice(ProductModelAssembler assembler) {
            this.assembler = assembler;
        }

        @ResponseBody
        @ExceptionHandler({OrderConflictException.class})
        @ResponseStatus(HttpStatus.CONFLICT)
        ResponseEntity<OrderConflictResponse> productConflictHandler(OrderConflictException ex) {
            OrderConflictResponse response = OrderConflictResponse.builder()
                    .outOfStockProducts(assembler.toCollectionModel(Product.toDtoList(ex.getOutOfStockProducts())))
                    .priceChangedProducts(assembler.toCollectionModel(Product.toDtoList(ex.getPriceChangedProducts())))
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static @Data class OrderConflictResponse {
        private CollectionModel<EntityModel<Product>> outOfStockProducts;
        private CollectionModel<EntityModel<Product>> priceChangedProducts;
    }

}

