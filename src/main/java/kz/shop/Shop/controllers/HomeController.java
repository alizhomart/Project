package kz.shop.Shop.controllers;


import kz.shop.Shop.entities.*;
import kz.shop.Shop.services.ItemService;
import kz.shop.Shop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping(value = "/")
    public String home(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        List<Items> items = itemService.getAllItems();
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("brands", brands);
        model.addAttribute("items", items);
        model.addAttribute("categories", categories);
        return "content";
    }

    @GetMapping(value = "/loginpage")
    public String loginpage(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "login";
    }

    @GetMapping(value = "/registerpage")
    public String register(){
        return "register";
    }

    @PostMapping(value = "/register")
    public String signUp(@RequestParam(name = "fullname")String fullname,
                           @RequestParam(name = "email")String email,
                           @RequestParam(name = "password")String password,
                           @RequestParam(name = "password2")String pass2){
        if(password.equals(pass2)){

            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setFullname(fullname);
            newUser.setPassword(password);

            if(userService.addUser(newUser)!=null){
                return "redirect:/register?success";
            }
        }
        return "redirect:/registerpage?error";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updateprofile")
    public String updateprofile(@RequestParam(name = "fullname")String fullname){
        Users currentUser = getUser();
        currentUser.setFullname(fullname);
        userService.updateUser(currentUser);
        return "redirect:/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updatepassword")
    public String updatepassword(@RequestParam(name = "old_password")String old_pas,
                                 @RequestParam(name = "new_password")String new_pass,
                                 @RequestParam(name = "new_password2")String new_pass2){
        Users currentUser = getUser();
        if(passwordEncoder.matches(old_pas, currentUser.getPassword()) && new_pass.equals(new_pass2)){

            currentUser.setPassword(passwordEncoder.encode(new_pass));
            userService.updateUser(currentUser);
            return "redirect:/profile?success";
        }
        return "redirect:/profile?error";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/profile")
    public String profile(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "profile";
    }

    @GetMapping(value = "/search")
    public String search(@RequestParam(name = "name")String name,Model model){
        List<Items> items = itemService.findAllByNameLikeOrderByPriceAsc(name);
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("brands", brands);
        model.addAttribute("items", items);
        model.addAttribute("categories", categories);
        model.addAttribute("CURRENT_USER", getUser());
        return "searchpage";
    }

    @PreAuthorize("isAnonymous() || isAuthenticated()")
    @GetMapping (value = "/searchpage")
    public String searchpage(Model model,
                             @RequestParam(name = "name")String name,
                             @RequestParam(name = "priceFrom")double priceFrom,
                             @RequestParam(name = "priceTo")double priceTo
    ){
        List<Items> items = itemService.findAllByNameLikeAndPriceBetweenOrderByPriceAsc(name,priceFrom,priceTo);
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("brands", brands);
        model.addAttribute("itemss", items);
        model.addAttribute("categories", categories);
        model.addAttribute("CURRENT_USER", getUser());
        return "searchpage";
    }


    @PreAuthorize("isAnonymous() || isAuthenticated()")
    @GetMapping(value = "/sortbybrands")
    public String sortByBrands(Model model, @RequestParam(name = "name")String name){
        List<Items> items = itemService.getAllByBrands(name);

        model.addAttribute("items", items);
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("brands", brands);
        model.addAttribute("items", items);
        return "sortByBrands";
    }

    @PostMapping(value = "/addtocart")
    public String addtocart(HttpSession session,
            @RequestParam(name = "itemId")Long itemId){
        if(session.getAttribute("basket") == null){
            List<Carts> carts = new ArrayList<>();
            Items item = itemService.getItem(itemId);
            Carts cart = new Carts();
            cart.setCount(1);
            cart.setPrice(item.getPrice());
            cart.setItem(item);
            carts.add(cart);
            session.setAttribute("basket", carts);
        }else{
            List<Carts> carts = (List<Carts>) session.getAttribute("basket");
            int index = this.exists(itemId, carts);
            if (index == -1) {
                Items item = itemService.getItem(itemId);
                Carts cart = new Carts();
                cart.setCount(1);
                cart.setPrice(item.getPrice());
                cart.setItem(item);
                carts.add(cart);
            } else {
                int count = carts.get(index).getCount() + 1;
                carts.get(index).setCount(count);
            }
            session.setAttribute("basket", carts);
        }
        return "redirect:/item-details/"+itemId+".html";
    }

    private int exists(Long id, List<Carts> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getItem().getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @PreAuthorize("isAnonymous() || isAuthenticated()")
    @GetMapping(value = "/item-details/{id}.html")
    public String itemdetails(Model model, @PathVariable(name = "id")Long id){
        Items item = itemService.getItem(id);
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("brands", brands);
        model.addAttribute("item", item);
        model.addAttribute("categories", categories);
        return "item-details";
    }

    @PreAuthorize("isAnonymous() || isAuthenticated()")
    @GetMapping(value = "/cart")
    public String basket(HttpSession session, Model model){
        int totalPrice = 0;
        List<Carts> carts = (List<Carts>) session.getAttribute("basket");
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = itemService.getAllCategories();

        if(carts != null){
            for(Carts c : carts){
                totalPrice += (c.getPrice() * c.getCount());
            }
        }

        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("basket",carts);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @PostMapping(value = "/addtocart2")
    public String addtocart2(HttpSession session,
                             @RequestParam(name = "itemId")Long itemId){
        if(session.getAttribute("basket") == null){
            List<Carts> carts = new ArrayList<>();
            Items item = itemService.getItem(itemId);
            Carts cart = new Carts();
            cart.setCount(1);
            cart.setPrice(item.getPrice());
            cart.setItem(item);
            carts.add(cart);
            session.setAttribute("basket", carts);
        }else{
            List<Carts> carts = (List<Carts>) session.getAttribute("basket");
            int index = this.exists(itemId, carts);
            if (index == -1) {
                Items item = itemService.getItem(itemId);
                Carts cart = new Carts();
                cart.setCount(1);
                cart.setPrice(item.getPrice());
                cart.setItem(item);
                carts.add(cart);
            } else {
                int count = carts.get(index).getCount() + 1;
                carts.get(index).setCount(count);
            }
            session.setAttribute("basket", carts);
        }
        return "redirect:/cart";
    }

    @PostMapping(value = "remove")
    public String removecart(HttpSession session, @RequestParam(name = "id")Long id){
        List<Carts> carts = (List<Carts>) session.getAttribute("basket");
        int index = exists(id, carts);
        Carts cart = carts.get(index);
        if(cart.getCount() >= 2){
            cart.setCount(cart.getCount()-1);
            carts.remove(index);
            carts.add(index,cart);
        }else{
            carts.remove(index);
        }
        session.setAttribute("basket", carts);
        return "redirect:/cart";
    }

    @PostMapping(value = "removeAll")
    public String removeAll(HttpSession session){
        List<Carts> carts = (List<Carts>) session.getAttribute("basket");
        if(carts != null){
            session.removeAttribute("basket");
        }
        return "redirect:/cart";
    }

    private Users getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            return (Users) authentication.getPrincipal();
        }
        return null;
    }
}
