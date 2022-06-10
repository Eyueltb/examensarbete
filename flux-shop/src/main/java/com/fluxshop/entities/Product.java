package com.fluxshop.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@NoArgsConstructor
@Table("_product")
public class Product {
    @Id String id;
    String name;
    String description;
    BigDecimal price;
    @Version Long version;

    public Product(String name, String description, BigDecimal price) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.version = null;
    }
}