package io.swapnil.crudflux.FluxApplication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data //A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, @Setter on all non-final fields, and @RequiredArgsConstructor
@Document(collection = "order") //This annotation marks a class as being a domain object that we want to persist to the database
// It also allows us to choose the name of the collection we want to use
public class Order {
    @Id
    private String id;
    private User user;
    private List<Product> products;
    private String address;
    private Boolean isDelivered;


}