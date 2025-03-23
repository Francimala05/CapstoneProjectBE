package PizzaPazza.repositories;

import PizzaPazza.entities.Fritto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrittoRepository extends JpaRepository<Fritto, Long> {
//Query da aggiungere nel caso
}
