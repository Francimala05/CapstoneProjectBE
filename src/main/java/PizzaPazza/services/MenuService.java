package PizzaPazza.services;

import PizzaPazza.entities.Drink;
import PizzaPazza.entities.Fritto;
import PizzaPazza.entities.Panuozzo;
import PizzaPazza.entities.Pizza;
import PizzaPazza.repositories.DrinkRepository;
import PizzaPazza.repositories.FrittoRepository;
import PizzaPazza.repositories.PanuozzoRepository;
import PizzaPazza.repositories.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private PizzaRepository pizzaRepository;

    public List<Pizza> getPizzaList() {
        return pizzaRepository.findAll();
    }

    public void addPizza(Pizza pizza) {
        pizzaRepository.save(pizza);
    }

    @Autowired
    private DrinkRepository drinkRepository;

    public List<Drink> getDrinkList() {
        return drinkRepository.findAll();
    }

    public void addDrink(Drink drink) {
        drinkRepository.save(drink);
    }

    @Autowired
    private PanuozzoRepository panuozzoRepository;

    public List<Panuozzo> getPanuozzoList() {
        return panuozzoRepository.findAll();
    }

    public void addPanuozzo(Panuozzo panuozzo) {
        panuozzoRepository.save(panuozzo);
    }


    @Autowired
    private FrittoRepository frittoRepository;

    public List<Fritto> getFrittoList() {
        return frittoRepository.findAll();
    }

    public void addFritto(Fritto fritto) {
        frittoRepository.save(fritto);
    }

}
