package PizzaPazza.controllers;

import PizzaPazza.DTO.PanuozzoDTO;
import PizzaPazza.entities.Panuozzo;
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
        panuozzi.forEach(panuozzo -> {
            PanuozzoDTO panuozzoDTO = new PanuozzoDTO();
            panuozzoDTO.setId(panuozzo.getId());
            panuozzoDTO.setName(panuozzo.getName());

            List<String> toppings = Arrays.asList(panuozzo.getToppingNames().split(","));
            panuozzoDTO.setToppings(toppings);

            panuozzoDTO.setInteroPrice(panuozzo.getInteroPrice());
            panuozzoDTO.setMezzoPrice(panuozzo.getMezzoPrice());

            String imageUrl = panuozzo.getImageUrl();
            if (!imageUrl.startsWith("/images/")) {
                imageUrl = "/images/" + imageUrl;
            }
            panuozzoDTO.setImageUrl("http://localhost:8085" + imageUrl);

            panuozzoDTOs.add(panuozzoDTO);
        });

        return panuozzoDTOs;
    }

    // AGGIUNGE UN NUOVO PANUOZZO CON I PREZZI PER INTERO E MEZZO
    @PostMapping
    public ResponseEntity<String> addPanuozzo(@RequestParam("panuozzo") String panuozzoJson, @RequestParam("image") MultipartFile image) {
        try {
            // JSON del panuozzo convertito in un oggetto PanuozzoDTO
            ObjectMapper objectMapper = new ObjectMapper();
            PanuozzoDTO panuozzoDTO = objectMapper.readValue(panuozzoJson, PanuozzoDTO.class);

            List<String> toppingNames = panuozzoDTO.getToppings();
            double interoPrice = panuozzoDTO.getInteroPrice();
            double mezzoPrice = panuozzoDTO.getMezzoPrice();

            String imagePath = saveImage(image);
            panuozzoDTO.setImageUrl(imagePath);


            Panuozzo newPanuozzo = new Panuozzo(panuozzoDTO.getName(), interoPrice, mezzoPrice, String.join(",", toppingNames), imagePath);
            menuService.addPanuozzo(newPanuozzo);

            return ResponseEntity.ok("Panuozzo aggiunto con successo! Immagine salvata in: " + imagePath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }

//SALVARE PANUOZZO ANCHE CON IMMAGINE STATICA IN STATIC.IMAGES
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


    //METODO PER ELIMINARE UN PANUOZZO PER NOME
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
