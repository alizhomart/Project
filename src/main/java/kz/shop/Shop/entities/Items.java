package kz.shop.Shop.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "stars")
    private int stars;

    @Column(name = "addedDate")
    private Date addedDate;

    @Column(name = "image")
    private String imageURL;

    @Column(name = "inTopPage")
    private boolean inTopPage;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brands brand;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Categories> categories;
}
