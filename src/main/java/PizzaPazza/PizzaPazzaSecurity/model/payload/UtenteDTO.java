package PizzaPazza.PizzaPazzaSecurity.model.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//CAMPI UTENTE IN FASE DI REGISTRAZIONE CON DEI LIMITI
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtenteDTO {

    private String nome;
    private String cognome;

    @NotBlank(message = "Il campo username è obbligatorio")
    @Size(min=3, max=20, message = " Il campo username deve contenere minimo 3 caratteri e massimo 20 caratteri")
    private String username;

    @NotBlank(message = "Il campo password è obbligatorio")
    @Size(min=7, max=20, message = "Il campo password deve contenere minimo 7 caratteri e massimo 20 caratteri")
    private String password;

    @NotBlank(message = "Il campo email è obbligatorio")
@Email(message="Il formato della mail non è corretto")
    private String email;

    private String ruolo;
}
