package kz.shop.Shop.repositories;

import kz.shop.Shop.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Roles, Long> {

    Roles findByRole(String role);
}
