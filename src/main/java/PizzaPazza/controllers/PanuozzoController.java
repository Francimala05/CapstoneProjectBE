package PizzaPazza.controllers;


import PizzaPazza.DTO.PanuozzoDTO;
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


    //AGGIUNGE UN NUOVO PANUOZZO INTERO E MEZZO
    @PostMapping
    public ResponseEntity<String> addPanuozzo(@RequestBody PanuozzoDTO panuozzoDTO) {
        try {
            List<String> toppingNames = panuozzoDTO.getToppings();
            double panuozzoPrice = panuozzoDTO.getPrice();

            Panuozzo newPanuozzo = new Panuozzo(panuozzoDTO.getName(), toppingNames, panuozzoPrice, true);
            menuService.addPanuozzo(newPanuozzo);

            Panuozzo mezzoPanuozzo = new Panuozzo(panuozzoDTO.getName(), toppingNames, panuozzoPrice / 2, false);
            menuService.addPanuozzo(mezzoPanuozzo);

            return ResponseEntity.ok("Panuozzo aggiunto con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
}
