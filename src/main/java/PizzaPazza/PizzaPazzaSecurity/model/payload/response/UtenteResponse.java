package PizzaPazza.PizzaPazzaSecurity.model.payload.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtenteResponse {
    private String message;
    private Long idUtente;
    private String username;
}
