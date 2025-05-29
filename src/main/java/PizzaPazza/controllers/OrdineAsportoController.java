package PizzaPazza.controllers;

import PizzaPazza.DTO.OrdineAsportoDTO;
import PizzaPazza.entities.OrdineAsporto;
import PizzaPazza.entities.Prenotazione;
import PizzaPazza.repositories.OrdineAsportoRepository;
import PizzaPazza.services.OrdineAsportoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordini/asporto")
public class OrdineAsportoController {

    @Autowired
    private OrdineAsportoService ordineAsportoService;

    @GetMapping
    public List<OrdineAsporto> getOrdiniAsporto() {
        return ordineAsportoService.getAllOrdini();
    }

//CREA UN ORDINE ASPORTO OVVIAMENTE CON AUTORIZZAZIONE
    @PostMapping("/invia")
    public ResponseEntity<?> creaOrdine(@RequestBody OrdineAsportoDTO ordineRequest, @RequestHeader("Authorization") String authorization) {
        try {
            // Stampa del token ricevuto visualizzabile in console
            System.out.println("Token ricevuto: " + authorization);

            OrdineAsporto ordine = ordineAsportoService.creaOrdine(ordineRequest);
            return ResponseEntity.ok("Ordine inviato con successo! ID Ordine: " + ordine.getId());
        } catch (Exception e) {
            // Risposta in caso di errore
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nell'invio dell'ordine: " + e.getMessage());
        }
    }

    // Nuovo endpoint per ottenere gli ordini da asporto per uno specifico utente
    @GetMapping("/utente/{idUtente}")
    public ResponseEntity<List<OrdineAsporto>> getOrdiniByIdUtente(@PathVariable Long idUtente) {
        List<OrdineAsporto> ordiniAsporto = ordineAsportoService.getOrdineAsportoByIdUtente(idUtente);
        if (ordiniAsporto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // Nessun ordine trovato per l'utente
        }
        return ResponseEntity.ok(ordiniAsporto);  // Restituisce gli ordini se presenti
    }
}

