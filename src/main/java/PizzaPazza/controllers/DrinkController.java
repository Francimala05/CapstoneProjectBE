package PizzaPazza.controllers;

import PizzaPazza.DTO.DrinkDTO;
import PizzaPazza.entities.Drink;
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

    // RESTITUISCE LA LISTA DELLE BIBITE, FILTRATE PER NOME SE SPECIFICATO
    @GetMapping
    public List<Drink> getDrinks(@RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return menuService.getDrinkListByName(name);
        }
        return menuService.getDrinkList();
    }

    // AGGIUNGE UNA NUOVA BIBITA
    @PostMapping
    public ResponseEntity<String> addDrink(@RequestBody DrinkDTO drinkDTO) {
        try {
            String drinkFormato = drinkDTO.getFormato();
            double drinkPrice = drinkDTO.getPrice();
            Drink newDrink = new Drink(drinkDTO.getName(), drinkFormato, drinkPrice);
            menuService.addDrink(newDrink);
            return ResponseEntity.ok("Bibita aggiunta con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
    //MODIFICA UN DRINK
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDrink(@PathVariable Long id, @RequestBody DrinkDTO drinkDTO) {
        Drink existingDrink = menuService.getDrinkById(id);
        if (existingDrink == null) {
            return ResponseEntity.status(404).body("Drink con ID " + id + " non trovato.");
        }

        existingDrink.setName(drinkDTO.getName());
        existingDrink.setFormato(drinkDTO.getFormato());
        existingDrink.setPrice(drinkDTO.getPrice());

        menuService.updateDrink(existingDrink);

        return ResponseEntity.ok("Drink aggiornato con successo.");
    }

    // ELIMINA UNA BIBITA
    @DeleteMapping
    public ResponseEntity<String> deleteDrinkByName(@RequestParam String name) {
        // Prova a eliminare la bibita
        if (menuService.deleteDrinkByName(name)) {
            return ResponseEntity.ok("Drink con nome " + name + " eliminato con successo.");
        } else {
            return ResponseEntity.status(404).body("Drink con nome " + name + " non trovato.");
        }
    }
}
