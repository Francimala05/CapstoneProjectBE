package PizzaPazza.entities;

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
    private double interoPrice;
    private double mezzoPrice;

    @Column(name = "topping_name")
    private String toppingNames;

    private String imageUrl;


    public Panuozzo(String name, double interoPrice, double mezzoPrice, String toppingNames, String imageUrl) {
        this.name = name;
        this.interoPrice = interoPrice;
        this.mezzoPrice = mezzoPrice;
        this.toppingNames = toppingNames;
        this.imageUrl = imageUrl;
    }


    public Panuozzo() {}
}
