package PizzaPazza.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrenotazioneDTO {
    private String username;
    private LocalDateTime dataOra;
    private int numeroPersone;
    private String altrePreferenze;
}
