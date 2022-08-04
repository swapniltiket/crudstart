package io.swapnil.crudflux.FluxApplication.controller;

import io.swapnil.crudflux.FluxApplication.entity.Order;
import io.swapnil.crudflux.FluxApplication.errorHandler.NotFoundException;
import io.swapnil.crudflux.FluxApplication.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private final OrderService orderService; //why use final here?

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/findAllOrder")
    public Flux<Order> findAllOrder() {
        return this.orderService.findAllOrder();
    }

    @GetMapping("/find/{ID}")
    public Mono<ResponseEntity<Order>> findById(@PathVariable("ID") String id) {
        return this.orderService.findById(id).map(body -> ResponseEntity.ok(body))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST , value="/place")
    public Mono<Order> save(@RequestBody Order Order) {
        return this.orderService.save(Order);
    }

    @RequestMapping(method = RequestMethod.PUT , value="/update")
    public Mono<ResponseEntity<Order>> updateOrder(@RequestBody Order order) {
        return this.orderService.updateOrder(order)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @RequestMapping(method = RequestMethod.DELETE , value="/delete/{id}")
    public Mono<ResponseEntity<String>> deleteOrder(@PathVariable("id") String id) {
        return this.orderService.deleteOrder(id).map(s -> {
            return ResponseEntity.ok(s);
        }).onErrorResume(NotFoundException.class, e -> {
            return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
        });
    }

    @GetMapping(value = "stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Order> streamUsers() {
        return this.orderService.streamUsers();
    }

}
