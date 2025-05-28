package PizzaPazza.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PanuozzoDTO {
    private Long id;
    private String name;
    private List<String> toppings;
    private String formato;
    private double price;
    private String imageUrl;
}
