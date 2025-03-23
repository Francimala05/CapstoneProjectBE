package PizzaPazza.repositories;

import PizzaPazza.entities.OrdineAsporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineAsportoRepository extends JpaRepository<OrdineAsporto, Long> {
//Query da aggiungere nel caso
}
