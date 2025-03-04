package PizzaPazza.controllers;

import PizzaPazza.entities.Topping;
import PizzaPazza.entities.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ToppingController {

    @Autowired
    private Menu menu;

    @GetMapping("/api/toppings")
    public List<Topping> getToppings() {
        return menu.getToppingList();
    }
}
