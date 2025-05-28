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
public class OrdineDomicilioService {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private PanuozzoRepository panuozzoRepository;

    @Autowired
    private FrittoRepository frittoRepository;

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private OrdineDomicilioRepository ordineRepository;

    @Autowired
    private UtenteRepository utenteRepository;

//RESTITUISCE TUTTI GLI ORDINI DOMICILIO
    public List<OrdineDomicilio> getAllOrdini() {
        return ordineRepository.findAll();
    }

    //PRENDE ORDINE DOMICILIO TRAMITE L'ID
    public List<OrdineDomicilio> getOrdineDomicilioByIdUtente(Long idUtente) {
        Optional<Utente> utenteOptional = utenteRepository.findById(idUtente);

        if (utenteOptional.isEmpty()) {
            return List.of();
        }

        Utente utente = utenteOptional.get();
        return ordineRepository.findByUtente(utente);
    }


    // CREAZIONE ORDINE
    public OrdineDomicilio creaOrdine(OrdineDomicilioDTO ordineRequest) {
        // Recupera le entità dai DTO (non più "Ids")
        List<Pizza> pizze = pizzaRepository.findAllById(ordineRequest.getPizze().stream().map(PizzaDTO::getId).collect(Collectors.toList()));
        List<Panuozzo> panuozzi = panuozzoRepository.findAllById(ordineRequest.getPanuozzi().stream().map(PanuozzoDTO::getId).collect(Collectors.toList()));
        List<Fritto> fritti = frittoRepository.findAllById(ordineRequest.getFritti().stream().map(FrittoDTO::getId).collect(Collectors.toList()));
        List<Drink> bibite = drinkRepository.findAllById(ordineRequest.getBibite().stream().map(DrinkDTO::getId).collect(Collectors.toList()));

        // Imposta la data e l'orario di default, se non forniti
        if (ordineRequest.getData() == null) {
            ordineRequest.setData(LocalDate.now());
        }
        if (ordineRequest.getOrario() == null) {
            ordineRequest.setOrario(LocalTime.now());
        }

        // Ottieni l'utente dal repository utilizzando il nome utente dal DTO
        Optional<Utente> utenteOptional = utenteRepository.findByUsername(ordineRequest.getUsername());
        if (utenteOptional.isEmpty()) {
            throw new RuntimeException("Utente non trovato: " + ordineRequest.getUsername());
        }
        Utente utente = utenteOptional.get();

        // Crea l'ordine e salvalo nel database
        OrdineDomicilio ordine = new OrdineDomicilio(
                pizze, panuozzi, fritti, bibite, ordineRequest.getOrario(), ordineRequest.getData(),
                ordineRequest.getTelefono(), ordineRequest.getIndirizzo(), ordineRequest.getEsigenzeParticolari(), utente, ordineRequest.getConto()
        );

        pizze.forEach(pizza -> pizza.setOrdineDomicilio(ordine));
        panuozzi.forEach(panuozzo -> panuozzo.setOrdineDomicilio(ordine));
        fritti.forEach(fritto -> fritto.setOrdineDomicilio(ordine));
        bibite.forEach(drink -> drink.setOrdineDomicilio(ordine));


        // Salva l'ordine nel database
        return ordineRepository.save(ordine);
    }
}

