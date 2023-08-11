package bullish.store.controller;

import bullish.store.assembler.StockModelAssembler;
import bullish.store.communication.stock.Stock;
import bullish.store.communication.stock.StockUpdateRequest;
import bullish.store.entity.StockEntity;
import bullish.store.exception.stock.StockConflictException;
import bullish.store.exception.stock.StockNotFoundException;
import bullish.store.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;
    private final StockModelAssembler stockModelAssembler;

    @Autowired
    public StockController(StockService stockService, StockModelAssembler stockModelAssembler) {
        this.stockService = stockService;
        this.stockModelAssembler = stockModelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Stock>> getAll() {
        List<StockEntity> stockEntities = stockService.getAll();
        List<Stock> dtos = Stock.toDtoList(stockEntities);
        return stockModelAssembler.toCollectionModel(dtos);
    }

    @GetMapping("/{productId}")
    public EntityModel<Stock> getByProductId(@PathVariable Long productId) throws StockNotFoundException {
        StockEntity stockEntity = stockService.getByProductId(productId);
        Stock dto = Stock.toDto(stockEntity);
        return stockModelAssembler.toModel(dto);
    }

    @PutMapping("/{productId}")
    public EntityModel<Stock> update(
            @PathVariable Long productId,
            @RequestBody StockUpdateRequest request
    ) throws StockConflictException, StockNotFoundException {
        StockEntity stockEntity = stockService.update(productId, request);
        Stock dto = Stock.toDto(stockEntity);
        return stockModelAssembler.toModel(dto);
    }

}
