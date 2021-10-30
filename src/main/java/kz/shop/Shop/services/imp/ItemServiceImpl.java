package kz.shop.Shop.services.imp;

import kz.shop.Shop.entities.Brands;
import kz.shop.Shop.entities.Categories;
import kz.shop.Shop.entities.Countries;
import kz.shop.Shop.entities.Items;
import kz.shop.Shop.repositories.BrandsRepo;
import kz.shop.Shop.repositories.CategoryRepo;
import kz.shop.Shop.repositories.CountryRepo;
import kz.shop.Shop.repositories.ItemRepo;
import kz.shop.Shop.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private BrandsRepo brandsRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CountryRepo countryRepo;

    @Override
    public List<Items> getAllItems() {
        return itemRepo.findAll();
    }

    @Override
    public Items getItem(Long id) {
        return itemRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteItem(Long id) {
        itemRepo.deleteById(id);
    }

    @Override
    public Items saveItem(Items item) {
        return itemRepo.save(item);
    }

    @Override
    public Items addItem(Items item) {
        return itemRepo.save(item);
    }

    @Override
    public List<Items> findAllByNameLikeOrderByPriceAsc(String name) {
        return itemRepo.findAllByNameStartingWith(name);
    }

    @Override
    public List<Items> findAllByNameLikeOrderByPriceDesc(String name) {
        return itemRepo.findAllByNameLikeOrderByPriceDesc(name);
    }

    @Override
    public List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceAsc(String name, double price1, double price2) {
        return itemRepo.findAllByNameStartingWithAndPriceBetweenOrderByPriceAsc(name,price1,price2);
    }

    @Override
    public List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, double price1, double price2) {
        return itemRepo.findAllByNameLikeAndPriceBetweenOrderByPriceDesc(name,price1,price2);
    }

    @Override
    public List<Brands> getAllBrands() {
        return brandsRepo.findAll();
    }

    @Override
    public Brands getBrands(Long id) {
        return brandsRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteBrand(Long id) {
        brandsRepo.deleteById(id);
    }

    @Override
    public Brands saveBrand(Brands brand) {
        return brandsRepo.save(brand);
    }

    @Override
    public Brands addBrands(Brands brand) {
        return brandsRepo.save(brand);
    }

    @Override
    public List<Items> getAllByBrands(String name) throws NullPointerException{
        List<Items> items = itemRepo.findAll();
        List<Items> itemsList = new ArrayList<>();
        for(Items it : items){
            if((it.getBrand()).getName().equalsIgnoreCase(name)){
                itemsList.add(it);
            }
        }
        return itemsList;
    }

    @Override
    public List<Countries> getAllCountries() {
        return countryRepo.findAll();
    }

    @Override
    public Countries getCountries(Long id) {
        return countryRepo.findById(id).orElse(null);
    }


    @Override
    public List<Categories> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Categories getCategory(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

}
