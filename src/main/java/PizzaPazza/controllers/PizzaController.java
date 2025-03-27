package PizzaPazza.controllers;

import PizzaPazza.DTO.PizzaDTO;
import PizzaPazza.entities.Pizza;
import PizzaPazza.services.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    @Autowired
    private MenuService menuService;

    // RESTITUISCE LA LISTA DELLE PIZZE
    @GetMapping
    public List<PizzaDTO> getPizzas(@RequestParam(required = false) String name) {
        List<Pizza> pizzas;

        if (name != null && !name.isEmpty()) {
            pizzas = menuService.getPizzaList().stream()
                    .filter(pizza -> pizza.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        } else {
            // Altrimenti restituisci tutte le pizze
            pizzas = menuService.getPizzaList();
        }

        List<PizzaDTO> pizzaDTOs = new ArrayList<>();
        pizzas.stream()
                .collect(Collectors.groupingBy(pizza -> pizza.getName()))
                .forEach((nameKey, groupedPizzas) -> {
                    PizzaDTO pizzaDTO = new PizzaDTO();
                    pizzaDTO.setId(groupedPizzas.get(0).getId());
                    pizzaDTO.setName(nameKey);

                    // Trova gli ingredienti (toppings)
                    List<String> toppings = groupedPizzas.stream()
                            .flatMap(pizza -> Arrays.stream(pizza.getToppingNames().split(",")))
                            .distinct()
                            .collect(Collectors.toList());
                    pizzaDTO.setToppings(toppings);

                    //IMPOSTA I PREZZI
                    pizzaDTO.setPrice(groupedPizzas.stream()
                            .filter(pizza -> pizza.getName().equals(nameKey) && pizza.getPrice() > 0)
                            .findFirst().map(Pizza::getPrice).orElse(0.0));
                    pizzaDTO.setMezzoChiloPrice(groupedPizzas.stream()
                            .filter(pizza -> pizza.getName().equals(nameKey) && pizza.getMezzoChiloPrice() > 0)
                            .findFirst().map(Pizza::getMezzoChiloPrice).orElse(0.0));
                    pizzaDTO.setChiloPrice(groupedPizzas.stream()
                            .filter(pizza -> pizza.getName().equals(nameKey) && pizza.getChiloPrice() > 0)
                            .findFirst().map(Pizza::getChiloPrice).orElse(0.0));

                    // URL DELL IMMAGINE COLLEGATO
                    String imageUrl = groupedPizzas.get(0).getImageUrl();
                    if (!imageUrl.startsWith("/images/")) {
                        imageUrl = "/images/" + imageUrl;
                    }
                    pizzaDTO.setImageUrl("http://localhost:8085" + imageUrl);

                    pizzaDTOs.add(pizzaDTO);
                });

        return pizzaDTOs;
    }


    // AGGIUNGE UNA NUOVA PIZZA SINGOLA, MEZZOCHILO, CHILO
    @PostMapping
    public ResponseEntity<String> addPizza(@RequestParam("pizza") String pizzaJson, @RequestParam("image") MultipartFile image) {
        try {
            // JSON della pizza convertito in un oggetto PizzaDTO
            ObjectMapper objectMapper = new ObjectMapper();
            PizzaDTO pizzaDTO = objectMapper.readValue(pizzaJson, PizzaDTO.class);

            List<String> toppingNames = pizzaDTO.getToppings();
            double pizzaPrice = pizzaDTO.getPrice();
            double mezzoChiloPrice = pizzaDTO.getMezzoChiloPrice();
            double chiloPrice = pizzaDTO.getChiloPrice();

            //SALVA L'IMMAGINE
            String imagePath = saveImage(image);
            pizzaDTO.setImageUrl(imagePath);

            // CREA UNA NUOVA PIZZA E COLLEGA L'IMMAGINE
            Pizza newPizza = new Pizza(pizzaDTO.getName(), String.join(",", toppingNames), pizzaPrice, mezzoChiloPrice, chiloPrice, pizzaDTO.getImageUrl());
            menuService.addPizza(newPizza);

            return ResponseEntity.ok("Pizza aggiunta con successo! Immagine salvata in: " + imagePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }

    //SALVA L'IMMAGINE SUL SERVER
    private String saveImage(MultipartFile image) throws IOException {
        String projectPath = System.getProperty("user.dir");
        String uploadsDir = projectPath + "/src/main/resources/static/images/";
        File directory = new File(uploadsDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File targetFile = new File(uploadsDir + uniqueFileName);
        image.transferTo(targetFile);

        return "/images/" + uniqueFileName;
    }

    // ELIMINAZIONE PIZZA TRAMITE NOME
    @DeleteMapping
    public ResponseEntity<String> deletePizzaByName(@RequestParam String name) {
        List<Pizza> pizzas = menuService.getPizzaList().stream()
                .filter(pizza -> pizza.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (pizzas.isEmpty()) {
            return ResponseEntity.status(404).body("Pizza con nome " + name + " non trovata.");
        }

        // ELIMINARE TUTTE LE PIZZE COL SINGOLO NOME
        menuService.deletePizzasByName(name);

        return ResponseEntity.ok("Pizza con nome " + name + " eliminata con successo.");
    }

    //MODIFICA PIZZA RICERCATA PER NOME
    @PutMapping
    public ResponseEntity<String> updatePizza(@RequestParam String name, @RequestParam("pizza") String pizzaJson, @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PizzaDTO pizzaDTO = objectMapper.readValue(pizzaJson, PizzaDTO.class);

            List<Pizza> pizzas = menuService.getPizzaList().stream()
                    .filter(pizza -> pizza.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());

            if (pizzas.isEmpty()) {
                return ResponseEntity.status(404).body("Pizza con nome " + name + " non trovata.");
            }

            Pizza existingPizza = pizzas.get(0);

            //AGGIORNARE DETTAGLI
            existingPizza.setName(pizzaDTO.getName());
            existingPizza.setToppingNames(String.join(",", pizzaDTO.getToppings()));
            existingPizza.setPrice(pizzaDTO.getPrice());
            existingPizza.setMezzoChiloPrice(pizzaDTO.getMezzoChiloPrice());
            existingPizza.setChiloPrice(pizzaDTO.getChiloPrice());

            if (image != null && !image.isEmpty()) {
                String imagePath = saveImage(image);
                existingPizza.setImageUrl(imagePath);
            }

            menuService.updatePizza(existingPizza);

            return ResponseEntity.ok("Pizza con nome " + name + " aggiornata con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
}
