package PizzaPazza.PizzaPazzaSecurity.model.payload.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Il campo username è obbligatorio")
    @Size(min=3, max=20, message = "Il campo username deve contenere minimo 3 caratteri e massimo 20 caratteri")
    private String username;

    @NotBlank(message = "Il campo password è obbligatorio")
    @Size(min=7, max=20, message = "Il campo username deve contenere minimo 7 caratteri e massimo 20 caratteri")
    private String password;
}
