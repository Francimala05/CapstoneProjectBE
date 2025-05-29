package PizzaPazza.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "panuozzi")
public class Panuozzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "topping_name")
    private String toppingNames;
    private String formato;
    private double price;
    private String imageUrl;

    //COLLEGO A ORDINEASPORTO E ORDINEDOMICILIO
    @ManyToOne
    @JoinColumn(name = "ordine_asporto_id")
    @JsonBackReference
    private OrdineAsporto ordineAsporto;

    @ManyToOne
    @JoinColumn(name = "ordine_domicilio_id")
    @JsonBackReference
    private OrdineDomicilio ordineDomicilio;

    public Panuozzo(String name, String formato, double price, String toppingNames, String imageUrl) {
        this.name = name;
        this.formato = formato;
        this.price = price;
        this.toppingNames = toppingNames;
        this.imageUrl = imageUrl;
    }

    public Panuozzo() {}
}
