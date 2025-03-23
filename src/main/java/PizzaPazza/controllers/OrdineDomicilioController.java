package PizzaPazza.controllers;

import PizzaPazza.DTO.OrdineDomicilioDTO;
import PizzaPazza.entities.OrdineDomicilio;
import PizzaPazza.services.OrdineDomicilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordini/domicilio")
public class OrdineDomicilioController {

    @Autowired
    private OrdineDomicilioService ordineDomicilioService;

    @PostMapping("/invia")
    public ResponseEntity<?> creaOrdine(@RequestBody OrdineDomicilioDTO ordineRequest, @RequestHeader("Authorization") String authorization) {
        try {
            // Stampa del token ricevuto (per debugging)
            System.out.println("Token ricevuto: " + authorization);

            // Creazione dell'ordine senza associare chiavi esterne
            OrdineDomicilio ordine = ordineDomicilioService.creaOrdine(ordineRequest);
            return ResponseEntity.ok("Ordine inviato con successo! ID Ordine: " + ordine.getId());
        } catch (Exception e) {
            // Risposta in caso di errore
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nell'invio dell'ordine: " + e.getMessage());
        }
    }
}
