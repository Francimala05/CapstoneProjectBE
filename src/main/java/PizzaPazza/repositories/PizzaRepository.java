package PizzaPazza.repositories;

import PizzaPazza.entities.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    //query da aggiungere nel caso
}
