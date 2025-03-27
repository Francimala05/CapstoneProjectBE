package PizzaPazza.entities;

import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "ordini_asporto")
public class OrdineAsporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //RELAZIONE ONETOMANY AI PRODOTTI
    @OneToMany(mappedBy = "ordineAsporto")
    private List<Pizza> pizze;

    @OneToMany(mappedBy = "ordineAsporto")
    private List<Panuozzo> panuozzi;

    @OneToMany(mappedBy = "ordineAsporto")
    private List<Fritto> fritti;

    @OneToMany(mappedBy = "ordineAsporto")
    private List<Drink> bibite;

    private LocalDate data;
    private LocalTime orario;
    private String esigenzeParticolari;

    //COLLEGO A UTENTE
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    private double conto;

    public OrdineAsporto() {}

    public OrdineAsporto(List<Pizza> pizze, List<Panuozzo> panuozzi, List<Fritto> fritti, List<Drink> bibite,
                         LocalTime orario, LocalDate data, String esigenzeParticolari, Utente utente, double conto) {
        this.pizze = pizze;
        this.panuozzi = panuozzi;
        this.fritti = fritti;
        this.bibite = bibite;
        this.orario = orario;
        this.data = data;
        this.esigenzeParticolari = esigenzeParticolari;
        this.utente = utente;
        this.conto = conto;
    }
}
