package com.ismailcet.CafeManagement.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name="product")
public class Product implements Serializable {

    public static final long serialVersionUID = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="category",
            referencedColumnName ="id",
            nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name="price")
    private Integer price;

    @Column(name = "status")
    private String status;


}
