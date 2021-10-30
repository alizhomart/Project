package kz.shop.Shop.services;

import kz.shop.Shop.entities.Roles;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import  kz.shop.Shop.entities.Users;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    Users updateUser(Users user);
    Users getUser(String email);
    Users addUser(Users user);
    Users addUser(Users user, String role);
    void deleteUser(Long id);
    List<Users> getAllUsers();
    List<Roles> getAllRoles();
}
