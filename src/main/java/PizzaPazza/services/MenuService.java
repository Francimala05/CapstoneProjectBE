package PizzaPazza.services;

import PizzaPazza.entities.Pizza;
import PizzaPazza.entities.Topping;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private List<Pizza> pizzaList = new ArrayList<>();
    private List<Topping> toppingList = new ArrayList<>();


    public List<Pizza> getPizzaList() {
        return pizzaList;
    }
    public void addPizza(Pizza pizza) {
        pizzaList.add(pizza);
    }
}
