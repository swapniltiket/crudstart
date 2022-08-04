package io.swapnil.crudflux.FluxApplication.service;

import io.swapnil.crudflux.FluxApplication.entity.Order;
import io.swapnil.crudflux.FluxApplication.entity.Product;
import io.swapnil.crudflux.FluxApplication.entity.User;
import io.swapnil.crudflux.FluxApplication.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@RunWith(SpringRunner.class)
// why is this used????
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @MockBean
    OrderRepository orderRepository;

    @Test
    void findAllOrder(){
        Order o1 = this.generateOrder();
        Order o2 = this.generateOrder();
        Flux<Order> orderFlux = Flux.just(o1,o2);
        Mockito.when(this.orderRepository.findAll()).thenReturn(orderFlux);
        StepVerifier.create(this.orderService.findAllOrder()).expectNext(o1,o2).verifyComplete();
    }

    @Test
    void findById(){
        Order o1 = this.generateOrder();
        Mono<Order> orderMono = Mono.just(o1);
        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(orderMono);
        StepVerifier.create(this.orderService.findById("abc")).expectNext(o1).verifyComplete();
    }

    @Test
    void save(){
        Order o1 = this.generateOrder();
        Mono<Order> orderMono = Mono.just(o1);
        Mockito.when(this.orderRepository.save(Mockito.any())).thenReturn(orderMono);
        StepVerifier.create(this.orderService.save(o1)).expectNext(o1).verifyComplete();
    }

    @Test
    void updateOrder(){
        Order o1 = this.generateOrder();
        Mono<Order> orderMono = Mono.just(o1);
        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(orderMono);
        Mockito.when(this.orderRepository.save(o1)).thenReturn(orderMono);
//        BDDMockito.when(this.orderService.updateOrder(o1))
//                .thenReturn(orderMono);
        // why are we using this above function
        StepVerifier.create(this.orderService.updateOrder(o1)).expectNext(o1).verifyComplete();
    }

    @Test
    void deleteOrder(){
        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(Mono.empty());
        StepVerifier.create(this.orderService.deleteOrder("o1")).expectError().verify();

        Order o1 = this.generateOrder();
        Mono<Order> orderMono = Mono.just(o1);
        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(orderMono);
        Mockito.when(this.orderRepository.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
        StepVerifier.create(this.orderService.deleteOrder("o1")).expectNext("ok").verifyComplete();
    }

    @Test
    void streamUsers(){
        Order o1 = this.generateOrder();
        Order o2 = this.generateOrder();
        Flux<Order> orderFlux = Flux.just(o1,o2);
        Mockito.when(this.orderRepository.findAll()).thenReturn(orderFlux);
        StepVerifier.create(this.orderService.findAllOrder()).expectNext(o1,o2).verifyComplete();
    }

    private Order generateOrder() {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setAddress("Pune");
        order.setIsDelivered(false);

        User user = new User();
        user.setName("pratik");
        user.setMobileNumber("1234567890");
        order.setUser(user);

        List<Product> products = new ArrayList<Product>();

        Product product = new Product();
        product.setName("Rayzen 7");
        product.setPrise("200$");

        Product product1 = new Product();
        product1.setPrise("Gigabyte MotherBoard");
        product1.setName("50$");

        products.add(product);
        products.add(product1);
        order.setProducts(products);

        return order;
    }

}
