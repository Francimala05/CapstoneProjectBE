package PizzaPazza.repositories;

import PizzaPazza.entities.Drink;
import PizzaPazza.entities.Panuozzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkRepository  extends JpaRepository<Drink, Long> {
  //TROVA PER NOME
    List<Drink> findByNameIgnoreCase(String name);
}
