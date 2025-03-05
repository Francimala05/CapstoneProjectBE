package PizzaPazza.repositories;

import PizzaPazza.entities.Panuozzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanuozzoRepository extends JpaRepository<Panuozzo, Long> {
//Query da aggiungere nel caso
}
