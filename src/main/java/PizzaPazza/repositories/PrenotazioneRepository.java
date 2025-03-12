package PizzaPazza.repositories;
import PizzaPazza.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByDataOraPrenotazione(LocalDateTime dataOraPrenotazione);


}
