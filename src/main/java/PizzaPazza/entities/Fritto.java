package PizzaPazza.entities;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name="fritti")
public class Fritto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;


    @ManyToOne
    @JoinColumn(name = "ordine_asporto_id")
    private OrdineAsporto ordineAsporto;

    @ManyToOne
    @JoinColumn(name = "ordine_domicilio_id")
    private OrdineDomicilio ordineDomicilio;

    public Fritto() {
    }

    public Fritto(String name, double price) {
        this.name = name;
        this.price= price;
    }

}
