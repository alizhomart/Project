package kz.shop.Shop.services;

import kz.shop.Shop.entities.Brands;
import kz.shop.Shop.entities.Categories;
import kz.shop.Shop.entities.Countries;
import kz.shop.Shop.entities.Items;

import java.util.List;

public interface ItemService {

    List<Items> getAllItems();
    Items getItem(Long id);
    void deleteItem(Long id);
    Items saveItem(Items item);
    Items addItem(Items item);

    List<Items> findAllByNameLikeOrderByPriceAsc(String name);
    List<Items> findAllByNameLikeOrderByPriceDesc(String name);
    List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceAsc(String name, double price1, double price2);
    List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, double price1, double price2);

    List<Brands> getAllBrands();
    Brands getBrands(Long id);
    void deleteBrand(Long id);
    Brands saveBrand(Brands brand);
    Brands addBrands(Brands brand);

    List<Items> getAllByBrands(String name);

    List<Countries> getAllCountries();
    Countries getCountries(Long id);

    List<Categories> getAllCategories();
    Categories getCategory(Long id);
}
