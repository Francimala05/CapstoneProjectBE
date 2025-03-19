package PizzaPazza.repositories;

import PizzaPazza.entities.Panuozzo;
import PizzaPazza.entities.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PanuozzoRepository extends JpaRepository<Panuozzo, Long> {
    List<Panuozzo> findByName(String name);
}
