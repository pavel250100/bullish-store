package bullish.store.controller;

import bullish.store.assembler.StockModelAssembler;
import bullish.store.entity.Stock;
import bullish.store.exception.StockNotFoundException;
import bullish.store.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public EntityModel<Stock> one(@PathVariable Long id) throws StockNotFoundException {
        Stock stock = stockService.getById(id);
        return stockModelAssembler.toModel(stock);
    }

    @GetMapping("/product/{productId}")
    public EntityModel<Stock> getByProductId(@PathVariable Long productId) throws StockNotFoundException {
        Stock stock = stockService.getByProductId(productId);
        return stockModelAssembler.toModel(stock);
    }

}
