package PizzaPazza.PizzaPazzaSecurity.model.entities;


import PizzaPazza.PizzaPazzaSecurity.Ruolo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="utenti")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUtente;

    private String nome;
    private String cognome;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;
}
