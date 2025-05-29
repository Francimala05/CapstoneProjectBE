package PizzaPazza.PizzaPazzaSecurity.service;

import PizzaPazza.PizzaPazzaSecurity.Ruolo;
import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.PizzaPazzaSecurity.model.exception.DuplicateEmailException;
import PizzaPazza.PizzaPazzaSecurity.model.exception.DuplicateUsernameException;
import PizzaPazza.PizzaPazzaSecurity.model.exception.RuoloException;
import PizzaPazza.PizzaPazzaSecurity.model.payload.UtenteDTO;
import PizzaPazza.PizzaPazzaSecurity.model.payload.request.LoginRequest;
import PizzaPazza.PizzaPazzaSecurity.model.payload.response.LoginResponse;
import PizzaPazza.PizzaPazzaSecurity.repository.UtenteRepository;
import PizzaPazza.PizzaPazzaSecurity.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UtenteService {

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

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
    public Utente insertUtente(UtenteDTO dto) throws DuplicateEmailException, DuplicateUsernameException, RuoloException {
        // Verifica duplicati
        checkDuplicateKeys(dto.getUsername(), dto.getEmail());

        // Imposta ruolo di default
        Ruolo ruoloUtente;
        try {
            ruoloUtente = dto.getRuolo() != null ? Ruolo.valueOf(dto.getRuolo().toUpperCase()) : Ruolo.USER;
        } catch (IllegalArgumentException e) {
            throw new RuoloException("Ruolo non valido.");
        }

        // Blocco la possibilità di creare utenti con ruoli privilegiati
        if (ruoloUtente == Ruolo.ADMIN || ruoloUtente == Ruolo.PROPRIETARIO) {
            throw new RuoloException("Non puoi creare un utente con ruolo " + ruoloUtente);
        }

        // Preparazione entità
        Utente user = dtoToEntity(dto);
        user.setRuolo(ruoloUtente);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return utenteRepository.save(user);
    }
//CREA UTENTE CON RUOLO
    public Utente creaUtenteConRuolo(UtenteDTO dto) throws DuplicateEmailException, DuplicateUsernameException, RuoloException {
        checkDuplicateKeys(dto.getUsername(), dto.getEmail());

        Ruolo ruoloUtente;
        try {
            ruoloUtente = Ruolo.valueOf(dto.getRuolo().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuoloException("Ruolo non valido.");
        }

        // Crea utente con qualsiasi ruolo (solo se chiama questo endpoint un admin)
        Utente user = dtoToEntity(dto);
        user.setRuolo(ruoloUtente);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return utenteRepository.save(user);
    }


    //LOGIN
    public ResponseEntity<?> login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof User) {
            username = ((User) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Utente user = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        String jwt = jwtUtil.creaToken(user);
        LoginResponse loginResponse = new LoginResponse(user.getUsername(), jwt, user.getIdUtente());

        return ResponseEntity.ok(loginResponse);
    }


        //TRAVASO
    private Utente dtoToEntity(UtenteDTO dto) {
        Utente user = new Utente();
        user.setNome(dto.getNome());
        user.setCognome(dto.getCognome());
        user.setEmail(dto.getEmail());

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        return user;
    }
//UTENTE DA USERNAME
    public Utente getUtenteByUsername(String username) {
        return utenteRepository.findByUsername(username).orElse(null);
    }

}
