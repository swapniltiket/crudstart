package io.swapnil.crudflux.FluxApplication.controller;

import io.swapnil.crudflux.FluxApplication.entity.Order;
import io.swapnil.crudflux.FluxApplication.entity.Product;
import io.swapnil.crudflux.FluxApplication.entity.User;
import io.swapnil.crudflux.FluxApplication.repository.OrderRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //used to test on a real http server
public class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;   //creates a non blocking client for testing the non blocking endpoints
    //end points are like /user or /id etc

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void findAllOrder(){
        Order o1 = this.generateOrder();
        Order o2 = this.generateOrder();
        Flux<Order> flux = Flux.just(o1,o2);
        Mockito.when(this.orderRepository.findAll()).thenReturn(flux);
        this.webTestClient.get() //starts building a http get request
                .uri("/order/findAllOrder") // give the url of the endpoint
                .exchange() //get the response body and decode it
                .expectStatus() // assertion on the response status
                .is2xxSuccessful();   //is a successful response we can also use isok then comes under the 2xx range which also has other function
    }

    @Test
    void findById(){
        Order o1 = this.generateOrder();
        Mono<Order> mono = Mono.just(o1);
        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(mono);
        this.webTestClient.get().uri("/order/find/x").exchange().expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(o -> {
                    Assert.assertEquals(o1.getId(), o.getId());
                });
        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(Mono.empty());
        this.webTestClient.get().uri("/users/find/x").exchange().expectStatus().is4xxClientError();
    }

    @Test
    void save(){
        Order o1 = this.generateOrder();
        Mono<Order> mono = Mono.just(o1);
        Mockito.when(this.orderRepository.save(Mockito.any())).thenReturn(mono);
        this.webTestClient.post()
                .uri("/order/place")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(mono, Order.class)
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(o -> {
                    Assert.assertEquals(o1.getId(), o.getId());
                });
    }

    @Test
    void updateOrder(){
        Order o1 = this.generateOrder();
        Mono<Order> mono = Mono.just(o1);
        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(mono);
        o1.setAddress("ajo");
        Mockito.when(this.orderRepository.save(o1)).thenReturn(mono);
        this.webTestClient.put()
                .uri("/order/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(mono, Order.class)
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(o -> {
                    Assert.assertEquals(o.getAddress(), "ajo");
                });

        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(Mono.empty());
        this.webTestClient.put().uri("/order/update").exchange().expectStatus().is4xxClientError();
    }

    @Test
    void deleteOrder(){
        Order o1 = this.generateOrder();
        Mono<Order> mono = Mono.just(o1);
        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(mono);
        Mockito.when(this.orderRepository.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
        this.webTestClient.delete()
                .uri("/order/delete/x")
                .exchange().expectStatus().is2xxSuccessful();

        Mockito.when(this.orderRepository.findById(Mockito.anyString())).thenReturn(Mono.empty());
        this.webTestClient.delete().uri("/order/delete/x").exchange().expectStatus().is4xxClientError();
    }

    @Test
    void streamUsers(){
        Order o1 = this.generateOrder();
        Order o2 = this.generateOrder();
        Flux<Order> flux = Flux.just(o1,o2);
        Mockito.when(this.orderRepository.findAll()).thenReturn(flux);
        this.webTestClient.get().uri("/order/findAllOrder").exchange().expectStatus().is2xxSuccessful();
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
