package PizzaPazza.services;

import PizzaPazza.DTO.OrdineAsportoDTO;
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

    // Metodo per creare un ordine
    public OrdineAsporto creaOrdine(OrdineAsportoDTO ordineRequest) {
        // Recupera i prodotti selezionati dal carrello tramite gli ID
        List<Pizza> pizze = pizzaRepository.findAllById(ordineRequest.getPizzeIds());
        List<Panuozzo> panuozzi = panuozzoRepository.findAllById(ordineRequest.getPanuozziIds());
        List<Fritto> fritti = frittoRepository.findAllById(ordineRequest.getFrittiIds());
        List<Drink> bibite = drinkRepository.findAllById(ordineRequest.getBibiteIds());

        // Se non sono presenti, si potrebbe impostare la data e l'ora attuale come default
        if (ordineRequest.getData() == null) {
            ordineRequest.setData(LocalDate.now()); // Imposta la data attuale come default
        }
        if (ordineRequest.getOrario() == null) {
            ordineRequest.setOrario(LocalTime.now()); // Imposta l'ora attuale come default
        }

        // Ottieni l'utente dal repository utilizzando il nome utente dal DTO
        Optional<Utente> utenteOptional = utenteRepository.findByUsername(ordineRequest.getUsername());
        if (utenteOptional.isEmpty()) {
            throw new RuntimeException("Utente non trovato: " + ordineRequest.getUsername());
        }
        Utente utente = utenteOptional.get();

        // Crea l'ordine, associando l'utente recuperato e passando anche il conto
        OrdineAsporto ordine = new OrdineAsporto(
                pizze, panuozzi, fritti, bibite, ordineRequest.getOrario(), ordineRequest.getData(),
                ordineRequest.getEsigenzeParticolari(), utente, ordineRequest.getConto()
        );

        // Salva l'ordine nel database
        return ordineRepository.save(ordine);
    }
}

