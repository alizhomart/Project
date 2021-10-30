package kz.shop.Shop.repositories;

import kz.shop.Shop.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CategoryRepo extends JpaRepository<Categories, Long> {
}
