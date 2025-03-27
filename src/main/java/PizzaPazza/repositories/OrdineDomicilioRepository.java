package PizzaPazza.repositories;

import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.entities.OrdineDomicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdineDomicilioRepository extends JpaRepository<OrdineDomicilio, Long> {
    //PER UTENTE
    List<OrdineDomicilio> findByUtente(Utente utente);
}
