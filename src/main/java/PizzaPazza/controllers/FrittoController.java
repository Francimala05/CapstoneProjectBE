package PizzaPazza.controllers;
import PizzaPazza.DTO.FrittoDTO;
import PizzaPazza.entities.Fritto;
import PizzaPazza.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fritti")
public class FrittoController {
    @Autowired
    private MenuService menuService;

    // RESTITUISCE LA LISTA DEI FRITTI
    @GetMapping
    public List<Fritto> getFritti() {
        return menuService.getFrittoList();
    }

    //AGGIUNGE UN NUOVO FRITTO
    @PostMapping
    public ResponseEntity<String> addFritto(@RequestBody FrittoDTO frittoDTO) {
        try {
            double frittoPrice = frittoDTO.getPrice();

            Fritto newFritto = new Fritto(frittoDTO.getName(),frittoPrice);
            menuService.addFritto(newFritto);
            return ResponseEntity.ok("Fritto aggiunto con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
}
