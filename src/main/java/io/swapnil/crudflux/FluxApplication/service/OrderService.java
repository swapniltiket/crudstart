package io.swapnil.crudflux.FluxApplication.service;

import io.swapnil.crudflux.FluxApplication.entity.Order;
import io.swapnil.crudflux.FluxApplication.errorHandler.NotFoundException;
import io.swapnil.crudflux.FluxApplication.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public Flux<Order> findAllOrder()
    {
        return orderRepository.findAll();
    }

    public Mono<Order> findById(String id)
    {
        return orderRepository.findById(id);
    }

    public Mono<Order> save(Order order) {
        return orderRepository.save(order);
    }

    public Mono<Order> updateOrder(Order order) {
        return  orderRepository.findById(order.getId())
                .flatMap(x ->{
                    x.setProducts(order.getProducts());
                    x.setAddress(order.getAddress());
                    x.setId(order.getId());
                    x.setIsDelivered(order.getIsDelivered());
                    x.setUser(order.getUser());
                    return orderRepository.save(x);
                });
    }

    public Mono<String> deleteOrder(String id) {
        return this.orderRepository.findById(id).flatMap(userEntities -> {
            return this.orderRepository.deleteById(userEntities.getId()).thenReturn("ok");
        }).switchIfEmpty(Mono.error(new NotFoundException("user not found")));
    }

    public Flux<Order> streamUsers() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        Flux<Order> entitiesFlux = Flux.from(orderRepository.findAll());
        return Flux.zip(entitiesFlux, interval, (key, value) -> key);
    }

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /*
    @GetMapping("/find/{ID}")
    //ResponseEntity represents the whole HTTP response: status code, headers, and body.
    // so we are able to make use of the response ok or not found etc build in features
    public Mono<ResponseEntity<Order>> findById(@PathVariable(value ="ID" ) String id)//value = "ID" used when the parameters passed in get mapping
            //and path variable are not same
    {
        return orderRepository.findById(id)
                .map(x -> ResponseEntity.ok(x))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    */


    /*
    @RequestMapping(method = RequestMethod.PUT , value="/update")
    public Mono<ResponseEntity<Order>> updateOrder(@RequestBody Order order) {
        //orderRepository.save(order);
        return  orderRepository.findById(order.getId())
                .flatMap(x ->{
                    x.setProducts(order.getProducts());
                    x.setAddress(order.getAddress());
                    x.setId(order.getId());
                    x.setIsDelivered(order.getIsDelivered());
                    x.setUser(order.getUser());
                    return orderRepository.save(x);
                })
                .map(x -> ResponseEntity.ok(x))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    */

}
