package PizzaPazza.controllers;


import PizzaPazza.DTO.PanuozzoDAO;
import PizzaPazza.entities.Panuozzo;
import PizzaPazza.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/panuozzi")
public class PanuozzoController {
    @Autowired
    private MenuService menuService;

        // RESTITUISCE LA LISTA DEI PANUOZZI
    @GetMapping
    public List<Panuozzo> getPanuozzi() {
        return menuService.getPanuozzoList();
    }


    //AGGIUNGE UN NUOVO PANUOZZO
    @PostMapping
    public ResponseEntity<String> addPanuozzo(@RequestBody PanuozzoDAO panuozzoDAO) {
        try {
            List<String> toppingNames = panuozzoDAO.getToppings();
            double panuozzoPrice = panuozzoDAO.getPrice();

            Panuozzo newPanuozzo = new Panuozzo(panuozzoDAO.getName(), toppingNames, panuozzoPrice, false);
            menuService.addPanuozzo(newPanuozzo);
            return ResponseEntity.ok("Panuozzo aggiunto con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
}
