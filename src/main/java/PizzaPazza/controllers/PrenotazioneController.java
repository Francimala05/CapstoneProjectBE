package PizzaPazza.controllers;

import PizzaPazza.DTO.PrenotazioneDTO;
import PizzaPazza.entities.Prenotazione;
import PizzaPazza.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    // RESTITUISCE LA LISTA DELLE PRENOTAZIONI
    @GetMapping
    public List<Prenotazione> getPrenotazioni() {

        return prenotazioneService.getPrenotazioneList();
    }

    //AGGIUNGERE UNA NUOVA PRENOTAZIONE
    @PostMapping
    public ResponseEntity<String> aggiungiPrenotazione(@RequestBody PrenotazioneDTO prenotazioneDTO) {
        boolean prenotazioneConfermata = prenotazioneService.aggiungiPrenotazione(prenotazioneDTO);

        if (prenotazioneConfermata) {
            return ResponseEntity.ok("Prenotazione confermata!");
        } else {
            return ResponseEntity.status(400).body("Prenotazione non disponibile o dati errati.");
        }
    }
}
