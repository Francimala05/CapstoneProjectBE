package PizzaPazza.PizzaPazzaSecurity.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//COSA VIENE RESTITUITO AL LOGIN
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String token;
    private Long idUtente;
}
