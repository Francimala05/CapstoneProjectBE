package PizzaPazza.DTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class PrenotazioneDTO {
    private String username;

    private LocalDate data;
    private LocalTime orario;

    private int numeroPersone;
    private String altrePreferenze;
}
