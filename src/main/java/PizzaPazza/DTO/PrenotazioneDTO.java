package PizzaPazza.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class PrenotazioneDTO {
    private String username;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime dataOra;

    private LocalDate data;
    private LocalTime orario;

    private int numeroPersone;
    private String altrePreferenze;
}
