package PizzaPazza.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PizzaDTO {
    private String name;
    private List<String> toppings;
    private double price;
    private String imageUrl;
}
