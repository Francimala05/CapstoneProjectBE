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

    // AGGIUNGE UNA NUOVA PIZZA SINGOLA, MEZZOCHILO, CHILO
    @PostMapping
    public ResponseEntity<String> addPizza(@RequestBody PizzaDTO pizzaDTO) {
        try {
            List<String> toppingNames = pizzaDTO.getToppings();
            double pizzaPrice = pizzaDTO.getPrice();
            double mezzoChiloPrice = pizzaDTO.getMezzoChiloPrice();
            double chiloPrice = pizzaDTO.getChiloPrice();

            Pizza newPizza = new Pizza(pizzaDTO.getName()+ " Singola", toppingNames, pizzaPrice);
            menuService.addPizza(newPizza);

            Pizza mezzoChiloPizza = new Pizza(pizzaDTO.getName()+ " MezzoChilo", toppingNames, mezzoChiloPrice);
            menuService.addPizza(mezzoChiloPizza);

            Pizza chiloPizza = new Pizza(pizzaDTO.getName()+ " Chilo", toppingNames, chiloPrice);
            menuService.addPizza(chiloPizza);


            return ResponseEntity.ok("Pizza  singola, mezzo chilo e chilo aggiunte con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
}
