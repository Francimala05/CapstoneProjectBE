package PizzaPazza.PizzaPazzaSecurity.service;

import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.PizzaPazzaSecurity.model.exception.DuplicateEmailException;
import PizzaPazza.PizzaPazzaSecurity.model.exception.DuplicateUsernameException;
import PizzaPazza.PizzaPazzaSecurity.model.exception.RuoloException;
import PizzaPazza.PizzaPazzaSecurity.model.payload.UtenteDTO;
import PizzaPazza.PizzaPazzaSecurity.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UtenteService {

    @Autowired
    UtenteRepository utenteRepository;

    // Metodo per controllare i duplicati di email e username
    public void checkDuplicateKeys(String username, String email) throws DuplicateUsernameException, DuplicateEmailException {
        if (utenteRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("L'username " + username + " è già in uso.");
        }

        if (utenteRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("L'email " + email + " è già registrata.");
        }
    }

    // Metodo per inserire un nuovo utente
    public String insertUtente(UtenteDTO dto) throws DuplicateEmailException, DuplicateUsernameException, RuoloException {
        // Verifica duplicati
        checkDuplicateKeys(dto.getUsername(), dto.getEmail());

        // Se non c'è ruolo, impostiamo 'USER', altrimenti lanciamo un'eccezione
        if (dto.getRuolo() == null) {
            dto.setRuolo("USER");
        } else {
            throw new RuoloException("Il ruolo " + dto.getRuolo() + " non è valido.");
        }

        // Travaso (conversione) di UtenteDTO in Utente
        Utente user = dtoToEntity(dto);

        // Salvataggio dell'utente nel DB
        Utente userDb = utenteRepository.save(user);

        // Restituzione del messaggio di successo
        return "L'utente " + userDb.getUsername() + " è stato inserito correttamente nel sistema.";
    }

    // Metodo di utilità per convertire UtenteDTO in Utente (travasi)
    private Utente dtoToEntity(UtenteDTO dto) {
        Utente user = new Utente();
        user.setNome(dto.getNome());
        user.setCognome(dto.getCognome());
        user.setEmail(dto.getEmail());
        user.setRuolo(dto.getRuolo());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        return user;
    }
}
