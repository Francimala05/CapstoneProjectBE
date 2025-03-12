package PizzaPazza.services;

import PizzaPazza.DTO.PrenotazioneDTO;
import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.PizzaPazzaSecurity.repository.UtenteRepository;
import PizzaPazza.entities.Fritto;
import PizzaPazza.entities.Prenotazione;
import PizzaPazza.repositories.FrittoRepository;
import PizzaPazza.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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

    // Metodo per aggiungere una nuova prenotazione
    public boolean aggiungiPrenotazione(PrenotazioneDTO prenotazioneDTO) {
        String username = prenotazioneDTO.getUsername();
        Optional<Utente> utente = utenteRepository.findByUsername(username);

        if (utente.isEmpty()) {
            return false;
        }

        LocalDateTime dataOra = prenotazioneDTO.getDataOra();
        int numeroPersone = prenotazioneDTO.getNumeroPersone();

        if (!orarioValido(dataOra) || !disponibilitaPosti(dataOra, numeroPersone)) {
            return false;
        }


        Prenotazione prenotazione = new Prenotazione(null, utente.get(), dataOra, numeroPersone, prenotazioneDTO.getAltrePreferenze(),utente.get().getCognome() );
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
    private boolean disponibilitaPosti(LocalDateTime dataOra, int numeroPersone) {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findByDataOraPrenotazione(dataOra);
        int postiOccupati = prenotazioni.stream().mapToInt(Prenotazione::getNumeroPersone).sum();
        return (postiOccupati + numeroPersone) <= MAX_POSTI;
    }

    // Restituisce il numero di posti disponibili per una data e ora
    public int postiDisponibiliPerData(LocalDateTime dataOra) {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findByDataOraPrenotazione(dataOra);
        int postiOccupati = prenotazioni.stream().mapToInt(Prenotazione::getNumeroPersone).sum();
        return MAX_POSTI - postiOccupati;
    }

}
