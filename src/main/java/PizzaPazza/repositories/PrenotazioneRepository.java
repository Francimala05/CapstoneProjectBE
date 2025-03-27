package PizzaPazza.repositories;
import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    //RACCOGLIE PER ORARIO E DATA
    List<Prenotazione> findByDataAndOrario(LocalDate data, LocalTime orario);
    //PER UTENTE
    List<Prenotazione> findByUtente(Utente utente);
}


