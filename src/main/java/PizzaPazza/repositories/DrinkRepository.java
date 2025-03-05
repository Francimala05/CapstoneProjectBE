package PizzaPazza.repositories;

import PizzaPazza.entities.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinkRepository  extends JpaRepository<Drink, Long> {
    //query da aggiungere nel caso
}
