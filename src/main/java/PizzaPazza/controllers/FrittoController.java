package PizzaPazza.controllers;


import PizzaPazza.DTO.FrittoDAO;
import PizzaPazza.DTO.PanuozzoDAO;
import PizzaPazza.entities.Fritto;
import PizzaPazza.entities.Panuozzo;
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
    public ResponseEntity<String> addFritto(@RequestBody FrittoDAO frittoDAO) {
        try {
            double frittoPrice = frittoDAO.getPrice();

            Fritto newFritto = new Fritto(frittoDAO.getName(),frittoPrice);
            menuService.addFritto(newFritto);
            return ResponseEntity.ok("Fritto aggiunto con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
}
