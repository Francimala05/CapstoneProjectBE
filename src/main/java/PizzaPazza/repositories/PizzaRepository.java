package PizzaPazza.repositories;

import PizzaPazza.entities.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    //PER NOME
    List<Pizza> findByName(String name);
    List<Pizza> findByNameAndFormato(String name, String formato);
}
