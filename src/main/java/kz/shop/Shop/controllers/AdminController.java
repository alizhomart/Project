package kz.shop.Shop.controllers;


import kz.shop.Shop.entities.*;
import kz.shop.Shop.services.ItemService;
import kz.shop.Shop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;



    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/adminUsers")
    public String adminusersdetails(Model model){
        List<Users> users = userService.getAllUsers();
        List<Roles> roles = userService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("CURRENT_USER", getUser());
        return "admin_users";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/add-user")
    public String addUser(Model model,
                          @RequestParam(name = "fullname")String fullname,
                          @RequestParam(name = "email")String email,
                          @RequestParam(name = "password")String password,
                          @RequestParam(name = "password2")String pass2,
                          @RequestParam(name = "roleId")String role
    ){
        model.addAttribute("CURRENT_USER", getUser());
        if(password.equals(pass2)){

            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setFullname(fullname);
            newUser.setPassword(password);

            if(userService.addUser(newUser,role)!=null){
                return "redirect:/adminUsers?success";
            }
        }
        return "redirect:/adminUsers";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/deleteuser")
    public String deleteUser(@RequestParam(name = "id")Long id){
        userService.deleteUser(id);
        return "redirect:/adminUsers";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/adminItems")
    public String adminitems(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        List<Categories> categories = itemService.getAllCategories();
        List<Brands> brands = itemService.getAllBrands();
        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        return "admin_items";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/add-item")
    public String addItem(Model model,
                          @RequestParam(name = "name")String name,
                          @RequestParam(name = "description")String description,
                          @RequestParam(name = "price")double price,
                          @RequestParam(name = "stars")int star,
                          @RequestParam(name = "added_date") Date addedDate,
                          @RequestParam(name = "image")String image,
                          @RequestParam(name = "inTope")boolean inTope,
                          @RequestParam(name = "brandId")Long brandId){
        Items item = new Items();
        Brands brand = itemService.getBrands(brandId);
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setStars(star);
        item.setAddedDate(addedDate);
        item.setImageURL(image);
        item.setInTopPage(inTope);
        item.setBrand(brand);
        itemService.saveItem(item);
        return "redirect:/adminItems";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/deleteItem")
    public String deleteItem(@RequestParam(name = "id")Long id){
        itemService.deleteItem(id);
        return "redirect:/adminItems";
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/itemDetails/{id}.html")
    public String detailsItem(Model model, @PathVariable(name = "id")Long id){
        Items item = itemService.getItem(id);
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = itemService.getAllCategories();
        List<Categories> itemCategories = item.getCategories();
        categories.removeAll(itemCategories);
        model.addAttribute("item", item);
        model.addAttribute("categories", categories);
        model.addAttribute("brands",brands);
        return "admin_itemDetails";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/assigncategory")
    public String assignCategory(@RequestParam(name = "category_id")Long categoryId,
                                 @RequestParam(name = "item_id")Long itemId){
        Categories cat = itemService.getCategory(categoryId);
        if(cat!=null){
            Items item = itemService.getItem(itemId);
            if(item!=null){
                List<Categories> categories = item.getCategories();
                if(categories==null){
                    categories = new ArrayList<>();
                }
                categories.add(cat);
                item.setCategories(categories);
                itemService.saveItem(item);
                return "redirect:/itemDetails/"+itemId+".html";
            }
        }
        return "redirect:/itemDetails?error";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/removecategory")
    public String removeCategory(@RequestParam(name = "category_id")Long categoryId,
                                 @RequestParam(name = "item_id")Long itemId){
        Categories cat = itemService.getCategory(categoryId);
        if(cat!=null){
            Items item = itemService.getItem(itemId);
            if(item!=null){
                List<Categories> categories = item.getCategories();
                if(categories==null){
                    categories = new ArrayList<>();
                }
                categories.remove(cat);
                item.setCategories(categories);
                itemService.saveItem(item);
                return "redirect:/itemDetails/"+itemId+".html";
            }
        }
        return "redirect:/itemDetails?error";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/editItem")
    public String editItem(@RequestParam(name = "itemId")Long id,
                           @RequestParam(name = "name")String name,
                           @RequestParam(name = "desc")String desc,
                           @RequestParam(name = "price")double price,
                           @RequestParam(name = "stars")int stars,
                           @RequestParam(name = "inTop")boolean inTop,
                           @RequestParam(name = "brandId")Long brandId){
        Items item = itemService.getItem(id);
        Brands brand = itemService.getBrands(brandId);
        item.setName(name);
        item.setDescription(desc);
        item.setPrice(price);
        item.setStars(stars);
        item.setInTopPage(inTop);
        item.setBrand(brand);
        itemService.saveItem(item);
        return "redirect:/adminItems";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/adminBrands")
    public String adminBrands(Model model){
        List<Brands> brands = itemService.getAllBrands();
        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);
        model.addAttribute("brands", brands);
        return "admin_brands";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/addBrand")
    public String addBrand(@RequestParam(name = "countryId")Long id,
                           @RequestParam(name = "name")String name){
        Brands brand = new Brands();
        Countries country = itemService.getCountries(id);
        brand.setName(name);
        brand.setCountry(country);
        itemService.addBrands(brand);
        return "redirect:/adminBrands";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/brandDetails/{id}.html")
    public String brandDetails(Model model, @PathVariable(name = "id")Long id){
        Brands brand = itemService.getBrands(id);
        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("brand", brand);
        model.addAttribute("countries", countries);
        return "admin_brandDetails";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/editBrand")
    public String editBrand(@RequestParam(name = "name")String name,
                            @RequestParam(name = "ountryId")Long id,
                            @RequestParam(name = "brandId")Long brandId){
        Brands brand = itemService.getBrands(brandId);
        Countries country = itemService.getCountries(id);
        brand.setName(name);
        brand.setCountry(country);
        itemService.saveBrand(brand);
        return "redirect:/adminBrands";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/deleteBrand")
    public String deleteBrand(@RequestParam(name = "id")Long id){
        itemService.deleteBrand(id);
        return "redirect:/adminBrands";
    }

    private Users getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            return (Users) authentication.getPrincipal();
        }
        return null;
    }
}
