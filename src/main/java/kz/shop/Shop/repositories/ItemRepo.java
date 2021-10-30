package kz.shop.Shop.repositories;

import kz.shop.Shop.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepo extends JpaRepository<Items,Long> {

    List<Items> findAllByNameLikeOrderByPriceAsc(String name);
    List<Items> findAllByNameStartingWith(String name);

    List<Items> findAllByBrandStartingWith(String name);

    List<Items> findAllByNameLikeOrderByPriceDesc(String name);
    List<Items> findAllByNameStartingWithAndPriceBetweenOrderByPriceAsc(String name, double price1, double price2);
    List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, double price1, double price2);
}
