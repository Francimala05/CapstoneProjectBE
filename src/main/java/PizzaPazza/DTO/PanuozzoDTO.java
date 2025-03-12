package PizzaPazza.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PanuozzoDTO {
    private String name;
    private List<String> toppings;
    private double price;
    private boolean intero = true;
}
