package bullish.store.controller;

import bullish.store.assembler.StockModelAssembler;
import bullish.store.communication.stock.StockDTO;
import bullish.store.entity.Stock;
import bullish.store.exception.StockNotFoundException;
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
    public CollectionModel<EntityModel<Stock>> all() {
        List<Stock> stocks = stockService.getAll();
        return stockModelAssembler.toCollectionModel(stocks);
    }

    @GetMapping("/{id}")
    public EntityModel<Stock> getById(@PathVariable Long id) throws StockNotFoundException {
//        Stock stock = stockService.getById(id);
//        return stockModelAssembler.toModel(stock);
        return null;
    }

    @GetMapping("/product/{productId}")
    public EntityModel<Stock> getByProductId(@PathVariable Long productId) throws StockNotFoundException {
        Stock stock = stockService.getByProductId(productId);
        return stockModelAssembler.toModel(stock);
    }

    @PutMapping("/{id}")
    public EntityModel<StockDTO> update(@PathVariable Long productId) throws StockNotFoundException {
        return null;
//        Stock stock = stockService.update(productId, quantity);
    }

}
