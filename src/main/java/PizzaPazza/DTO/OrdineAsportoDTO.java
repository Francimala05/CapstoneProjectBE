package PizzaPazza.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class OrdineAsportoDTO {

    private List<PizzaDTO> pizze;
    private List<PanuozzoDTO> panuozzi;
    private List<FrittoDTO> fritti;
    private List<DrinkDTO> bibite;
    private String esigenzeParticolari;
    private LocalDate data;
    private LocalTime orario;
    private String username;
    private double conto;
}
