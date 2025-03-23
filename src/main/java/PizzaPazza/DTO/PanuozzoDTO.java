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
    private double interoPrice;
    private double mezzoPrice;
    private String imageUrl;
}
