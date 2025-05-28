package PizzaPazza.services;

import PizzaPazza.DTO.*;
import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.PizzaPazzaSecurity.repository.UtenteRepository;
import PizzaPazza.entities.*;
import PizzaPazza.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdineAsportoService {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private PanuozzoRepository panuozzoRepository;

    @Autowired
    private FrittoRepository frittoRepository;

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private OrdineAsportoRepository ordineRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    // RESTITUISCE TUTTI GLI ORDINI
    public List<OrdineAsporto> getAllOrdini() {
        return ordineRepository.findAll();
    }

    // GET ORDINE DELL'UTENTE SINGOLO
    public List<OrdineAsporto> getOrdineAsportoByIdUtente(Long idUtente) {
        Optional<Utente> utenteOptional = utenteRepository.findById(idUtente);
        if (utenteOptional.isEmpty()) {
            return List.of();
        }
        Utente utente = utenteOptional.get();
        return ordineRepository.findByUtente(utente);
    }

    // CREAZIONE ORDINE
    public OrdineAsporto creaOrdine(OrdineAsportoDTO ordineRequest) {

        // Recupera i prodotti selezionati dal carrello tramite gli ID
        List<Pizza> pizze = pizzaRepository.findAllById(
                ordineRequest.getPizze().stream().map(PizzaDTO::getId).collect(Collectors.toList()));
        List<Panuozzo> panuozzi = panuozzoRepository.findAllById(
                ordineRequest.getPanuozzi().stream().map(PanuozzoDTO::getId).collect(Collectors.toList()));
        List<Fritto> fritti = frittoRepository.findAllById(
                ordineRequest.getFritti().stream().map(FrittoDTO::getId).collect(Collectors.toList()));
        List<Drink> bibite = drinkRepository.findAllById(
                ordineRequest.getBibite().stream().map(DrinkDTO::getId).collect(Collectors.toList()));

        if (ordineRequest.getData() == null) {
            ordineRequest.setData(LocalDate.now());
        }
        if (ordineRequest.getOrario() == null) {
            ordineRequest.setOrario(LocalTime.now());
        }

        // Recupera utente dal repository
        Optional<Utente> utenteOptional = utenteRepository.findByUsername(ordineRequest.getUsername());
        if (utenteOptional.isEmpty()) {
            throw new RuntimeException("Utente non trovato: " + ordineRequest.getUsername());
        }
        Utente utente = utenteOptional.get();

        // Crea l’ordine con tutti i dati
        OrdineAsporto ordine = new OrdineAsporto(
                pizze, panuozzi, fritti, bibite,
                ordineRequest.getOrario(),
                ordineRequest.getData(),
                ordineRequest.getEsigenzeParticolari(),
                utente,
                ordineRequest.getConto()
        );

        // SETTA RELAZIONE INVERSA: assegna l’ordine a ogni prodotto
        pizze.forEach(pizza -> pizza.setOrdineAsporto(ordine));
        panuozzi.forEach(panuozzo -> panuozzo.setOrdineAsporto(ordine));
        fritti.forEach(fritto -> fritto.setOrdineAsporto(ordine));
        bibite.forEach(drink -> drink.setOrdineAsporto(ordine));

        // SALVA L’ORDINE (JPA salverà anche la relazione corretta)
        return ordineRepository.save(ordine);
    }
}
