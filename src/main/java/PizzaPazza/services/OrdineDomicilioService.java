package PizzaPazza.services;

import PizzaPazza.DTO.OrdineAsportoDTO;
import PizzaPazza.DTO.OrdineDomicilioDTO;
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

    // Metodo per creare un ordine
    public OrdineDomicilio creaOrdine(OrdineDomicilioDTO ordineRequest) {
        List<Pizza> pizze = pizzaRepository.findAllById(ordineRequest.getPizzeIds());
        List<Panuozzo> panuozzi = panuozzoRepository.findAllById(ordineRequest.getPanuozziIds());
        List<Fritto> fritti = frittoRepository.findAllById(ordineRequest.getFrittiIds());
        List<Drink> bibite = drinkRepository.findAllById(ordineRequest.getBibiteIds());

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


        OrdineDomicilio ordine = new OrdineDomicilio(
                pizze, panuozzi, fritti, bibite, ordineRequest.getOrario(), ordineRequest.getData(),
                ordineRequest.getTelefono(), ordineRequest.getIndirizzo(), ordineRequest.getEsigenzeParticolari(), utente, ordineRequest.getConto()
        );

        // Salva l'ordine nel database
        return ordineRepository.save(ordine);
    }
}

