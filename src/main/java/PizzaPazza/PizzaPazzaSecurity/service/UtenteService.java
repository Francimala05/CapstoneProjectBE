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
    public String insertUtente(UtenteDTO dto) throws DuplicateEmailException, DuplicateUsernameException, RuoloException {
        // Verifica duplicati
        checkDuplicateKeys(dto.getUsername(), dto.getEmail());


        if (dto.getRuolo() == null) {
            dto.setRuolo("USER");
        }

        // Travaso
        Utente user = dtoToEntity(dto);


        String pwdEncoded = passwordEncoder.encode(dto.getPassword());
        user.setPassword(pwdEncoded);

        // Salvataggio dell'utente nel DB
        Utente userDb = utenteRepository.save(user);



        return "L'utente " + userDb.getUsername() + " è stato inserito correttamente nel sistema.";
    }


    //LOGIN
    public ResponseEntity<?> login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

Object principal = authentication.getPrincipal();
String username;
if(principal instanceof User){
    username = ((User) principal).getUsername();
}else{
    username = principal.toString();
}
username = ((UserDetails) authentication.getPrincipal()).getUsername();

if(username == null || username.isEmpty()){
    throw new RuntimeException("Username non valido");
}

      Utente user= utenteRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("Utente non trovato"));
        List<String> ruoli = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    String jwt = jwtUtil.creaToken(user);
    return ResponseEntity.ok(jwt);
    }


        //TRAVASO
    private Utente dtoToEntity(UtenteDTO dto) {
        Utente user = new Utente();
        user.setNome(dto.getNome());
        user.setCognome(dto.getCognome());
        user.setEmail(dto.getEmail());
        user.setRuolo(Ruolo.valueOf(dto.getRuolo()));
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        return user;
    }
}
