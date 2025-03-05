package PizzaPazza.controllers;

import PizzaPazza.DTO.PizzaDTO;
import PizzaPazza.entities.Pizza;
import PizzaPazza.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {
    @Autowired
    private MenuService menuService;

    // RESTITUISCE LA LISTA DELLE PIZZE
    @GetMapping
    public List<Pizza> getPizzas() {
        return menuService.getPizzaList();
    }

    // AGGIUNGE UNA NUOVA PIZZA
    @PostMapping
    public ResponseEntity<String> addPizza(@RequestBody PizzaDTO pizzaDTO) {
        try {
            List<String> toppingNames = pizzaDTO.getToppings();
            double pizzaPrice = pizzaDTO.getPrice();

            Pizza newPizza = new Pizza(pizzaDTO.getName(), toppingNames, pizzaPrice, false);
            menuService.addPizza(newPizza);
            return ResponseEntity.ok("Pizza aggiunta con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
}
