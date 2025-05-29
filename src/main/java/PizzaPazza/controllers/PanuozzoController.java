package PizzaPazza.controllers;

import PizzaPazza.DTO.PanuozzoDTO;
import PizzaPazza.DTO.PizzaDTO;
import PizzaPazza.entities.Panuozzo;
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
@RequestMapping("/api/panuozzi")
public class PanuozzoController {

    @Autowired
    private MenuService menuService;

    // RESTITUISCE LA LISTA DEI PANUOZZI
    @GetMapping
    public List<PanuozzoDTO> getPanuozzi(@RequestParam(required = false) String name) {
        List<Panuozzo> panuozzi = (name != null && !name.isEmpty())
                ? menuService.getPanuozzoList().stream()
                .filter(panuozzo -> panuozzo.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList())
                : menuService.getPanuozzoList();

        List<PanuozzoDTO> panuozzoDTOs = new ArrayList<>();
        for (Panuozzo panuozzo : panuozzi) {
            PanuozzoDTO panuozzoDTO = new PanuozzoDTO();
            panuozzoDTO.setId(panuozzo.getId());
            panuozzoDTO.setName(panuozzo.getName());

            List<String> toppings = Arrays.asList(panuozzo.getToppingNames().split(","));
            panuozzoDTO.setToppings(toppings);

            panuozzoDTO.setFormato(panuozzo.getFormato());
            panuozzoDTO.setPrice(panuozzo.getPrice());

            String imageUrl = panuozzo.getImageUrl();
            if (!imageUrl.startsWith("/images/")) {
                imageUrl = "/images/" + imageUrl;
            }
            panuozzoDTO.setImageUrl("http://localhost:8085" + imageUrl);

            panuozzoDTOs.add(panuozzoDTO);
        }

        return panuozzoDTOs;
    }

    // AGGIUNGE UN NUOVO PANUOZZO CON FORMATO E PREZZO
    @PostMapping
    public ResponseEntity<String> addPanuozzo(@RequestParam("panuozzo") String panuozzoJson,
                                              @RequestParam("image") MultipartFile image) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PanuozzoDTO panuozzoDTO = objectMapper.readValue(panuozzoJson, PanuozzoDTO.class);

            List<String> toppingNames = panuozzoDTO.getToppings();
            String formato = panuozzoDTO.getFormato();
            double price = panuozzoDTO.getPrice();

            String imagePath = saveImage(image);
            panuozzoDTO.setImageUrl(imagePath);

            Panuozzo newPanuozzo = new Panuozzo(
                    panuozzoDTO.getName(),
                    formato,
                    price,
                    String.join(",", toppingNames),
                    imagePath
            );

            menuService.addPanuozzo(newPanuozzo);

            return ResponseEntity.ok("Panuozzo aggiunto con successo! Immagine salvata in: " + imagePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }
//MODIFICARE UN PANUOZZO
    @PutMapping
    public ResponseEntity<String> updatePanuozzo(@RequestParam String name,
                                              @RequestParam String formato,
                                              @RequestParam("panuozzo") String panuozzoJson,
                                              @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PanuozzoDTO dto = objectMapper.readValue(panuozzoJson, PanuozzoDTO.class);

            List<Panuozzo> panuozzos = menuService.getPanuozzoList().stream()
                    .filter(p -> p.getName().equalsIgnoreCase(name) &&
                            p.getFormato().equalsIgnoreCase(formato))
                    .collect(Collectors.toList());

            if (panuozzos.isEmpty()) {
                return ResponseEntity.status(404).body("Panuozzo con nome '" + name + "' e formato '" + formato + "' non trovata.");
            }

            Panuozzo existingPanuozzo = panuozzos.get(0);

            existingPanuozzo.setName(dto.getName());
            existingPanuozzo.setFormato(dto.getFormato());
            existingPanuozzo.setPrice(dto.getPrice());
            existingPanuozzo.setToppingNames(String.join(",", dto.getToppings()));

            if (image != null && !image.isEmpty()) {
                String imagePath = saveImage(image);
                existingPanuozzo.setImageUrl(imagePath);
            }

            menuService.updatePanuozzo(existingPanuozzo);
            return ResponseEntity.ok("Panuozzo aggiornato con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }


    // SALVA L’IMMAGINE DEL PANUOZZO
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

    // ELIMINA UN PANUOZZO PER NOME
    @DeleteMapping
    public ResponseEntity<String> deletePanuozzoByName(@RequestParam String name) {
        List<Panuozzo> panuozzi = menuService.getPanuozzoList().stream()
                .filter(panuozzo -> panuozzo.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (panuozzi.isEmpty()) {
            return ResponseEntity.status(404).body("Panuozzo con nome " + name + " non trovato.");
        }

        menuService.deletePanuozzoByName(name);
        return ResponseEntity.ok("Panuozzo con nome " + name + " eliminato con successo.");
    }
}
