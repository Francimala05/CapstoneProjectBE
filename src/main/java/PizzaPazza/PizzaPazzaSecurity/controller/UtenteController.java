package PizzaPazza.PizzaPazzaSecurity.controller;


import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import PizzaPazza.PizzaPazzaSecurity.model.exception.DuplicateEmailException;
import PizzaPazza.PizzaPazzaSecurity.model.exception.DuplicateUsernameException;
import PizzaPazza.PizzaPazzaSecurity.model.exception.RuoloException;
import PizzaPazza.PizzaPazzaSecurity.model.payload.UtenteDTO;
import PizzaPazza.PizzaPazzaSecurity.model.payload.request.LoginRequest;
import PizzaPazza.PizzaPazzaSecurity.model.payload.response.LoginResponse;
import PizzaPazza.PizzaPazzaSecurity.model.payload.response.UtenteResponse;
import PizzaPazza.PizzaPazzaSecurity.security.JwtUtil;
import PizzaPazza.PizzaPazzaSecurity.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
    UtenteService service;

    @Autowired
    JwtUtil jwtUtil;

    //METODO PER INSERIRE UN UTENTE
    @PostMapping("/insert")
    public ResponseEntity<UtenteResponse> insertUtente(@Validated @RequestBody UtenteDTO nuovoUtente, BindingResult checkValidazione) {
        if (checkValidazione.hasErrors()) {
            StringBuilder erroriValidazione = new StringBuilder("Problemi nella validazione");
            for (ObjectError errore : checkValidazione.getAllErrors()) {
                erroriValidazione.append(errore.getDefaultMessage());
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            Utente savedUser = service.insertUtente(nuovoUtente);
            String token = jwtUtil.creaToken(savedUser);

            UtenteResponse response = new UtenteResponse(
                    "L'utente " + savedUser.getUsername() + " è stato inserito correttamente nel sistema.",
                    savedUser.getIdUtente(),
                    savedUser.getUsername(),
                    token
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DuplicateEmailException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        } catch (DuplicateUsernameException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        } catch (RuoloException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginDTO, BindingResult checkValidazione) {

        try {

            if (checkValidazione.hasErrors()) {
                StringBuilder erroriValidazione = new StringBuilder("Problemi nella validazione");
                for (ObjectError errore : checkValidazione.getAllErrors()) {
                    erroriValidazione.append(errore.getDefaultMessage());
                }

                return new ResponseEntity<>(erroriValidazione.toString(), HttpStatus.BAD_REQUEST);
            }

           return service.login(loginDTO);

        } catch (Exception e) {
            return new ResponseEntity<>("Credenziali non valide"+e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    //PRENDERE UTENTE TRAMITE USERNAME
    @GetMapping("/get/{username}")
    public ResponseEntity<?> getUtente(@PathVariable String username) {
        Utente utente = service.getUtenteByUsername(username);
        if (utente != null) {
            return ResponseEntity.ok(utente);
        } else {
            return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/admin/create")
    public ResponseEntity<UtenteResponse> creaUtenteConRuolo(@RequestBody UtenteDTO dto) {
        try {
            Utente nuovo = service.creaUtenteConRuolo(dto);
            String token = jwtUtil.creaToken(nuovo);

            return ResponseEntity.ok(new UtenteResponse(
                    "Utente " + nuovo.getUsername() + " creato con ruolo " + nuovo.getRuolo(),
                    nuovo.getIdUtente(),
                    nuovo.getUsername(),
                    token
            ));
        } catch (DuplicateUsernameException | DuplicateEmailException e) {
            return ResponseEntity.status(409).body(new UtenteResponse(e.getMessage(), null, null, null));
        } catch (RuoloException e) {
            return ResponseEntity.status(400).body(new UtenteResponse(e.getMessage(), null, null, null));
        }
    }


}
