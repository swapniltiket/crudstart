package io.swapnil.crudflux.FluxApplication.repository;

import io.swapnil.crudflux.FluxApplication.entity.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OrderRepository extends ReactiveMongoRepository<Order,String> {
}
