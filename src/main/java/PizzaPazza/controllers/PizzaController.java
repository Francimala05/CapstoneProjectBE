package PizzaPazza.controllers;

import PizzaPazza.DTO.PizzaDTO;
import PizzaPazza.entities.Pizza;
import PizzaPazza.entities.Topping;
import PizzaPazza.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void addPizza(@RequestBody PizzaDTO pizzaDTO) {
        List<Topping> toppings = pizzaDTO.getToppings().stream()
                .map(toppingName -> new Topping(toppingName, 0))
                .collect(Collectors.toList());

        Pizza newPizza = new Pizza(pizzaDTO.getName(), toppings, false);
        menuService.addPizza(newPizza);
    }
}

//ESEMPIO POSTMAN
//{
//        "name": "Pizza Quattro Stagioni",
//        "toppings": ["Tomato", "Cheese", "Ham", "Mushrooms", "Olives"]
//        }