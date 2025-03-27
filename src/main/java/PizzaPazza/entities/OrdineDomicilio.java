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
@Table(name = "ordini_domicilio")
public class OrdineDomicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //RELAZIONE ONETOMANY AI PRODOTTI
    @OneToMany(mappedBy = "ordineDomicilio")
    private List<Pizza> pizze;

    @OneToMany(mappedBy = "ordineDomicilio")
    private List<Panuozzo> panuozzi;

    @OneToMany(mappedBy = "ordineDomicilio")
    private List<Fritto> fritti;

    @OneToMany(mappedBy = "ordineDomicilio")
    private List<Drink> bibite;

    private LocalDate data;
    private LocalTime orario;
    private Long telefono;
    private  String indirizzo;
    private String esigenzeParticolari;

    //COLLEGO A UTENTE
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    private double conto;

    public OrdineDomicilio() {}

    public OrdineDomicilio(List<Pizza> pizze, List<Panuozzo> panuozzi, List<Fritto> fritti, List<Drink> bibite,
                         LocalTime orario, LocalDate data,Long telefono,String indirizzo, String esigenzeParticolari, Utente utente, double conto) {
        this.pizze = pizze;
        this.panuozzi = panuozzi;
        this.fritti = fritti;
        this.bibite = bibite;
        this.orario = orario;
        this.data = data;
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.esigenzeParticolari = esigenzeParticolari;
        this.utente = utente;
        this.conto = conto;
    }
}
