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

    // GET - Lista delle pizze (opzionalmente filtrata per nome)
    @GetMapping
    public List<PizzaDTO> getPizzas(@RequestParam(required = false) String name) {
        List<Pizza> pizzas = (name != null && !name.isEmpty())
                ? menuService.getPizzaList().stream()
                .filter(pizza -> pizza.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList())
                : menuService.getPizzaList();

        List<PizzaDTO> pizzaDTOs = new ArrayList<>();
        for (Pizza pizza : pizzas) {
            PizzaDTO dto = new PizzaDTO();
            dto.setId(pizza.getId());
            dto.setName(pizza.getName());
            dto.setFormato(pizza.getFormato());
            dto.setPrice(pizza.getPrice());

            List<String> toppings = Arrays.asList(pizza.getToppingNames().split(","));
            dto.setToppings(toppings);

            String imageUrl = pizza.getImageUrl();
            if (!imageUrl.startsWith("/images/")) {
                imageUrl = "/images/" + imageUrl;
            }
            dto.setImageUrl("http://localhost:8085" + imageUrl);

            pizzaDTOs.add(dto);
        }

        return pizzaDTOs;
    }

    // POST
    @PostMapping
    public ResponseEntity<String> addPizza(@RequestParam("pizza") String pizzaJson,
                                           @RequestParam("image") MultipartFile image) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PizzaDTO dto = objectMapper.readValue(pizzaJson, PizzaDTO.class);

            String imagePath = saveImage(image);
            dto.setImageUrl(imagePath);

            Pizza pizza = new Pizza(
                    dto.getName(),
                    dto.getFormato(),
                    dto.getPrice(),
                    String.join(",", dto.getToppings()),
                    imagePath
            );

            menuService.addPizza(pizza);
            return ResponseEntity.ok("Pizza aggiunta con successo! Immagine salvata in: " + imagePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping
    public ResponseEntity<String> deletePizza(@RequestParam String name,
                                              @RequestParam String formato) {
        List<Pizza> pizzas = menuService.getPizzaList().stream()
                .filter(pizza -> pizza.getName().equalsIgnoreCase(name) &&
                        pizza.getFormato().equalsIgnoreCase(formato))
                .collect(Collectors.toList());

        if (pizzas.isEmpty()) {
            return ResponseEntity.status(404).body("Pizza con nome '" + name + "' e formato '" + formato + "' non trovata.");
        }

        menuService.deletePizzaByNameAndFormato(name, formato);
        return ResponseEntity.ok("Pizza '" + name + "' con formato '" + formato + "' eliminata con successo.");
    }


    @PutMapping
    public ResponseEntity<String> updatePizza(@RequestParam String name,
                                              @RequestParam String formato,
                                              @RequestParam("pizza") String pizzaJson,
                                              @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PizzaDTO dto = objectMapper.readValue(pizzaJson, PizzaDTO.class);

            List<Pizza> pizzas = menuService.getPizzaList().stream()
                    .filter(p -> p.getName().equalsIgnoreCase(name) &&
                            p.getFormato().equalsIgnoreCase(formato))
                    .collect(Collectors.toList());

            if (pizzas.isEmpty()) {
                return ResponseEntity.status(404).body("Pizza con nome '" + name + "' e formato '" + formato + "' non trovata.");
            }

            Pizza existingPizza = pizzas.get(0);

            existingPizza.setName(dto.getName());
            existingPizza.setFormato(dto.getFormato());
            existingPizza.setPrice(dto.getPrice());
            existingPizza.setToppingNames(String.join(",", dto.getToppings()));

            if (image != null && !image.isEmpty()) {
                String imagePath = saveImage(image);
                existingPizza.setImageUrl(imagePath);
            }

            menuService.updatePizza(existingPizza);
            return ResponseEntity.ok("Pizza aggiornata con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }

    //SALVO L'IMMAGINE
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
}
