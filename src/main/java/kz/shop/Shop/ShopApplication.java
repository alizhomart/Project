package kz.shop.Shop;

import kz.shop.Shop.services.ItemService;
import kz.shop.Shop.services.imp.ItemServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

}
