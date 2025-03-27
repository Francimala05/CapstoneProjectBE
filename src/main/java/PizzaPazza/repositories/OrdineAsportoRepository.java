package PizzaPazza.repositories;

import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.entities.OrdineAsporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdineAsportoRepository extends JpaRepository<OrdineAsporto, Long> {
    //PER UTENTE
    List<OrdineAsporto> findByUtente(Utente utente);
}
