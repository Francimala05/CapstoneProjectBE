package PizzaPazza.controllers;

import PizzaPazza.DTO.OrdineAsportoDTO;
import PizzaPazza.entities.OrdineAsporto;
import PizzaPazza.services.OrdineAsportoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordini/asporto")
public class OrdineAsportoController {

    @Autowired
    private OrdineAsportoService ordineAsportoService;

    @PostMapping("/invia")
    public ResponseEntity<?> creaOrdine(@RequestBody OrdineAsportoDTO ordineRequest, @RequestHeader("Authorization") String authorization) {
        try {
            // Stampa del token ricevuto (per debugging)
            System.out.println("Token ricevuto: " + authorization);

            // Creazione dell'ordine senza associare chiavi esterne
            OrdineAsporto ordine = ordineAsportoService.creaOrdine(ordineRequest);
            return ResponseEntity.ok("Ordine inviato con successo! ID Ordine: " + ordine.getId());
        } catch (Exception e) {
            // Risposta in caso di errore
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nell'invio dell'ordine: " + e.getMessage());
        }
    }
}
