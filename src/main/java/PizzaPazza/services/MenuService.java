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

    //RESTITUISCE TUTTE LE PIZZE
    public List<Pizza> getPizzaList() {
        return pizzaRepository.findAll();
    }
//AGGIUNGE E SALVA UNA PIZZA
    public void addPizza(Pizza pizza) {
        pizzaRepository.save(pizza);
    }

    @Autowired
    private DrinkRepository drinkRepository;

    //RESTITUISCE TUTTE LE BIBITE
    public List<Drink> getDrinkList() {
        return drinkRepository.findAll();
    }

    //RESTITUISCE SINGOLE BIBITE PER NOME
    public List<Drink> getDrinkListByName(String name) {
        return drinkRepository.findByNameIgnoreCase(name);  // Ignora maiuscole/minuscole
    }

    //AGGIUNGE BIBITA
    public void addDrink(Drink drink) {
        drinkRepository.save(drink);
    }

    @Autowired
    private PanuozzoRepository panuozzoRepository;


    //RESTITUISCE LISTA PANUOZZI
    public List<Panuozzo> getPanuozzoList() {
        return panuozzoRepository.findAll();
    }

    //AGGIUNGE PANUOZZO
    public void addPanuozzo(Panuozzo panuozzo) {
        panuozzoRepository.save(panuozzo);
    }


    @Autowired
    private FrittoRepository frittoRepository;

    //RESTITUISCE LISTA FRITTI
    public List<Fritto> getFrittoList() {
        return frittoRepository.findAll();
    }


    //AGGIUNGE FRITTO
    public void addFritto(Fritto fritto) {
        frittoRepository.save(fritto);
    }

    //ELIMINA UNA PIZZA PER NOME E FORMATO
    public void deletePizzaByNameAndFormato(String name, String formato) {
        List<Pizza> pizzas = pizzaRepository.findByNameAndFormato(name, formato);
        if (!pizzas.isEmpty()) {
            pizzaRepository.deleteAll(pizzas);
        }
    }


    //AGGIORNA UNA SINGOLA PIZZA
    public void updatePizza(Pizza pizza) {
        pizzaRepository.save(pizza);
    }

    //ELIMINA PANUOZZO PER NOME
    public void deletePanuozzoByName(String name) {
        List<Panuozzo> panuozziToDelete = panuozzoRepository.findByName(name);
        if (panuozziToDelete != null && !panuozziToDelete.isEmpty()) {
            panuozzoRepository.deleteAll(panuozziToDelete);
        }
    }


    //ELIMINA IL DRINK PER NOME
    public boolean deleteDrinkByName(String name) {
        List<Drink> drinksToDelete = drinkRepository.findByNameIgnoreCase(name);
        if (!drinksToDelete.isEmpty()) {
            drinkRepository.deleteAll(drinksToDelete);
            return true;
        }
        return false;
    }


}
