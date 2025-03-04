package PizzaPazza.controllers;

import PizzaPazza.DTO.PizzaDTO;
import PizzaPazza.entities.Pizza;
import PizzaPazza.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {
    @Autowired
    private MenuService menuService;


    //RESTITUISCE LA LISTA DELLE PIZZE
    @GetMapping
    public List<Pizza> getPizzas() {
        return menuService.getPizzaList();
    }


    //AGGIUNGE UNA NUOVA PIZZA
    @PostMapping
    public void addPizza(
            @RequestPart("pizza") PizzaDTO pizzaDTO,
            @RequestPart("image") MultipartFile image) {
        List<String> toppingNames = pizzaDTO.getToppings();
        double pizzaPrice = pizzaDTO.getPrice();
        Pizza newPizza = new Pizza(pizzaDTO.getName(), toppingNames ,pizzaPrice, false);

        menuService.addPizza(newPizza);
    }
}

