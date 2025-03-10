package PizzaPazza.PizzaPazzaSecurity.controller;


import PizzaPazza.PizzaPazzaSecurity.model.exception.DuplicateEmailException;
import PizzaPazza.PizzaPazzaSecurity.model.exception.DuplicateUsernameException;
import PizzaPazza.PizzaPazzaSecurity.model.exception.RuoloException;
import PizzaPazza.PizzaPazzaSecurity.model.payload.UtenteDTO;
import PizzaPazza.PizzaPazzaSecurity.model.payload.request.LoginRequest;
import PizzaPazza.PizzaPazzaSecurity.model.payload.response.LoginResponse;
import PizzaPazza.PizzaPazzaSecurity.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
    UtenteService service;

    @PostMapping("/insert")
    public ResponseEntity<String> insertUtente(@Validated @RequestBody UtenteDTO nuovoUtente, BindingResult chechValidazione) {
        if (chechValidazione.hasErrors()) {
            StringBuilder erroriValidazione = new StringBuilder("Problemi nella validazione");
            for (ObjectError errore : chechValidazione.getAllErrors()) {
                erroriValidazione.append(errore.getDefaultMessage());
            }
            return new ResponseEntity<>(erroriValidazione.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            String messaggio = service.insertUtente((nuovoUtente));
            return new ResponseEntity<>(messaggio, HttpStatus.OK);
        } catch (DuplicateEmailException e) {
            return new ResponseEntity<>("Email già presente nel sistema", HttpStatus.NOT_ACCEPTABLE);
        } catch (DuplicateUsernameException e) {
            return new ResponseEntity<>("Username già presente nel sistema", HttpStatus.NOT_ACCEPTABLE);
        } catch (RuoloException e) {
            return new ResponseEntity<>("Errore di gestione ruolo utente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

}
