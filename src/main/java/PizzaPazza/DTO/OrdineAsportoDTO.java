package PizzaPazza.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class OrdineAsportoDTO {

    private List<Long> pizzeIds;
    private List<Long> panuozziIds;
    private List<Long> frittiIds;
    private List<Long> bibiteIds;
    private String esigenzeParticolari;
    private LocalDate data;
    private LocalTime orario;
    private String username;
    private double conto;
}
