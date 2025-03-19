package PizzaPazza.services;

import PizzaPazza.DTO.PrenotazioneDTO;
import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.PizzaPazzaSecurity.repository.UtenteRepository;
import PizzaPazza.entities.Prenotazione;
import PizzaPazza.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    private static final int MAX_POSTI = 45;

    public List<Prenotazione> getPrenotazioneList() {
        return prenotazioneRepository.findAll();
    }

    public boolean aggiungiPrenotazione(PrenotazioneDTO prenotazioneDTO) {
        String username = prenotazioneDTO.getUsername();
        Optional<Utente> utenteOptional = utenteRepository.findByUsername(username);

        if (utenteOptional.isEmpty()) {
            return false;
        }

        Utente utente = utenteOptional.get();
        LocalDate data = prenotazioneDTO.getData();
        LocalTime orario = prenotazioneDTO.getOrario();
        int numeroPersone = prenotazioneDTO.getNumeroPersone();

        // Combina data e orario in un solo LocalDateTime
        LocalDateTime dataOra = LocalDateTime.of(data, orario);

        // Controllo della validità dell'orario e disponibilità dei posti
        if (!orarioValido(dataOra)) {
            return false;
        }

        if (!disponibilitaPosti(data, orario, numeroPersone)) {
            return false;
        }

        // Crea la prenotazione
        Prenotazione prenotazione = new Prenotazione(null, utente, data, orario, numeroPersone, prenotazioneDTO.getAltrePreferenze(), utente.getCognome());
        prenotazioneRepository.save(prenotazione);
        return true;
    }


    // Verifica se l'orario è valido
    private boolean orarioValido(LocalDateTime dataOra) {
        return dataOra.getDayOfWeek() != DayOfWeek.TUESDAY
                && !dataOra.toLocalTime().isBefore(LocalTime.of(16, 0))
                && !dataOra.toLocalTime().isAfter(LocalTime.of(23, 59));
    }

    // Verifica la disponibilità dei posti per la data e l'orario
    private boolean disponibilitaPosti(LocalDate data,LocalTime orario, int numeroPersone) {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findByDataAndOrario(data,orario);
        int postiOccupati = prenotazioni.stream().mapToInt(Prenotazione::getNumeroPersone).sum();
        return (postiOccupati + numeroPersone) <= MAX_POSTI;
    }

    // Restituisce il numero di posti disponibili per una data e ora
    public int postiDisponibiliPerData(LocalDate data,LocalTime orario) {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findByDataAndOrario(data,orario);
        int postiOccupati = prenotazioni.stream().mapToInt(Prenotazione::getNumeroPersone).sum();
        return MAX_POSTI - postiOccupati;
    }
}
