package PizzaPazza.controllers;

import PizzaPazza.DTO.DrinkDAO;
import PizzaPazza.DTO.PizzaDTO;
import PizzaPazza.entities.Drink;
import PizzaPazza.entities.Menu;
import PizzaPazza.entities.Pizza;
import PizzaPazza.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drinks")
public class DrinkController {

    @Autowired
    private MenuService menuService;

    // RESTITUISCE LA LISTA DELLE BIBITE
    @GetMapping
    public List<Drink> getDrinks() {
        return menuService.getDrinkList();
    }

    // AGGIUNGE UNA NUOVA BIBITA
    @PostMapping
    public ResponseEntity<String> addDrink(@RequestBody DrinkDAO drinkDAO) {
        try {
            double drinkPrice = drinkDAO.getPrice();
            Drink newDrink = new Drink(drinkDAO.getName(), drinkPrice);
            menuService.addDrink(newDrink);
            return ResponseEntity.ok("Bibita aggiunta con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }

}
