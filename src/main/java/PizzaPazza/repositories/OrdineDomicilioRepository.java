package PizzaPazza.repositories;

import PizzaPazza.entities.OrdineDomicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineDomicilioRepository extends JpaRepository<OrdineDomicilio, Long> {
//Query da aggiungere nel caso
}
