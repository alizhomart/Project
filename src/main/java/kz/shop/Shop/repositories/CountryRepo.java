package kz.shop.Shop.repositories;

import kz.shop.Shop.entities.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
@Transactional
public interface CountryRepo extends JpaRepository<Countries, Long> {


}
