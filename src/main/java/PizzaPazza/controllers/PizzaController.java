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
                    pizzaDTO.setName(nameKey);

                    // Trova gli ingredienti (toppings)
                    List<String> toppings = groupedPizzas.stream()
                            .flatMap(pizza -> Arrays.stream(pizza.getToppingNames().split(",")))
                            .distinct()
                            .collect(Collectors.toList());
                    pizzaDTO.setToppings(toppings);

                    // Imposta i prezzi per i vari formati
                    pizzaDTO.setPrice(groupedPizzas.stream()
                            .filter(pizza -> pizza.getName().equals(nameKey) && pizza.getPrice() > 0)
                            .findFirst().map(Pizza::getPrice).orElse(0.0));
                    pizzaDTO.setMezzoChiloPrice(groupedPizzas.stream()
                            .filter(pizza -> pizza.getName().equals(nameKey) && pizza.getMezzoChiloPrice() > 0)
                            .findFirst().map(Pizza::getMezzoChiloPrice).orElse(0.0));
                    pizzaDTO.setChiloPrice(groupedPizzas.stream()
                            .filter(pizza -> pizza.getName().equals(nameKey) && pizza.getChiloPrice() > 0)
                            .findFirst().map(Pizza::getChiloPrice).orElse(0.0));

                    // Gestisci l'URL dell'immagine
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

            // Salva l'immagine e ottieni il percorso
            String imagePath = saveImage(image);
            pizzaDTO.setImageUrl(imagePath);  // Imposta il campo imageUrl nel PizzaDTO

            // Crea una nuova pizza, passandole l'URL dell'immagine
            Pizza newPizza = new Pizza(pizzaDTO.getName(), String.join(",", toppingNames), pizzaPrice, mezzoChiloPrice, chiloPrice, pizzaDTO.getImageUrl());
            menuService.addPizza(newPizza); // Aggiungi la pizza al menu

            return ResponseEntity.ok("Pizza aggiunta con successo! Immagine salvata in: " + imagePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }

    // Metodo per salvare l'immagine sul server
    private String saveImage(MultipartFile image) throws IOException {
        String projectPath = System.getProperty("user.dir");
        String uploadsDir = projectPath + "/src/main/resources/static/images/";
        File directory = new File(uploadsDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Genera un nome unico per l'immagine per evitare conflitti di nome
        String uniqueFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File targetFile = new File(uploadsDir + uniqueFileName);
        image.transferTo(targetFile);

        // Restituisci il percorso dell'immagine che può essere accessibile tramite un URL
        return "/images/" + uniqueFileName;
    }

    // Metodo per eliminare una pizza in base al nome
    @DeleteMapping
    public ResponseEntity<String> deletePizzaByName(@RequestParam String name) {
        List<Pizza> pizzas = menuService.getPizzaList().stream()
                .filter(pizza -> pizza.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (pizzas.isEmpty()) {
            return ResponseEntity.status(404).body("Pizza con nome " + name + " non trovata.");
        }

        // Rimuovi tutte le pizze con quel nome
        menuService.deletePizzasByName(name);

        return ResponseEntity.ok("Pizza con nome " + name + " eliminata con successo.");
    }

    @PutMapping
    public ResponseEntity<String> updatePizza(@RequestParam String name, @RequestParam("pizza") String pizzaJson, @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            // JSON della pizza convertito in un oggetto PizzaDTO
            ObjectMapper objectMapper = new ObjectMapper();
            PizzaDTO pizzaDTO = objectMapper.readValue(pizzaJson, PizzaDTO.class);

            // Cerca la pizza esistente nel database
            List<Pizza> pizzas = menuService.getPizzaList().stream()
                    .filter(pizza -> pizza.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());

            if (pizzas.isEmpty()) {
                return ResponseEntity.status(404).body("Pizza con nome " + name + " non trovata.");
            }

            // Trova la pizza che corrisponde al nome
            Pizza existingPizza = pizzas.get(0);

            // Aggiorna i dettagli della pizza
            existingPizza.setName(pizzaDTO.getName());
            existingPizza.setToppingNames(String.join(",", pizzaDTO.getToppings()));
            existingPizza.setPrice(pizzaDTO.getPrice());
            existingPizza.setMezzoChiloPrice(pizzaDTO.getMezzoChiloPrice());
            existingPizza.setChiloPrice(pizzaDTO.getChiloPrice());

            if (image != null && !image.isEmpty()) {
                String imagePath = saveImage(image);
                existingPizza.setImageUrl(imagePath);
            }

            // Salva la pizza aggiornata nel database
            menuService.updatePizza(existingPizza);

            return ResponseEntity.ok("Pizza con nome " + name + " aggiornata con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
}
