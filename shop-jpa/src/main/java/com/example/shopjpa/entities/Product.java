package com.example.shopjpa.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="_product")
public class Product {
    @Id @Column(columnDefinition = "varchar(100)") String id;
    private String name;
    private String description;
    private BigDecimal price;
    @Version Long version;
    public Product(String name, String description, BigDecimal price) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.version = null;
    }
}
