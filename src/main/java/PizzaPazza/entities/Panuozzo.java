package PizzaPazza.entities;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name="panuozzi")
public class Panuozzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean intero = false;

    @Column(name = "topping_name")
    private String toppingNames;

    private double price;

    public Panuozzo() {
    }

    public Panuozzo(String name, List<String> toppingNames, double price, boolean intero) {
        this.name = name;
        this.intero = intero;
        this.toppingNames = String.join(",",toppingNames);
        this.price= price;
    }
}
