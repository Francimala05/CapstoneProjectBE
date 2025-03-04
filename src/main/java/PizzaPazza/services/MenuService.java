package PizzaPazza.services;

import PizzaPazza.entities.Pizza;
import PizzaPazza.repositories.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Value("${pizza.image.directory}")
    private String imageDirectory;

    public List<Pizza> getPizzaList() {
        return pizzaRepository.findAll();
    }

    public void addPizza(Pizza pizza) {
        pizzaRepository.save(pizza);
    }

    public String saveImage(MultipartFile image) {
        String imageName = image.getOriginalFilename();

        File imageFile = new File(imageDirectory + File.separator + imageName);

        try {
            image.transferTo(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nel salvataggio dell'immagine");
        }
        return imageName;
    }
}
