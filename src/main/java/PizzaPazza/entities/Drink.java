package PizzaPazza.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name= "bibite")
public class Drink{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String formato;
	private double price;


	//COLLEGO A ORDINEASPORTO E ORDINEDOMICILIO
	@ManyToOne
	@JoinColumn(name = "ordine_asporto_id")
	private OrdineAsporto ordineAsporto;

	@ManyToOne
	@JoinColumn(name = "ordine_domicilio_id")
	private OrdineDomicilio ordineDomicilio;


	public Drink() {
	}

	public Drink(String name, String formato, double price) {
		this.name = name;
		this.formato=formato;
		this.price=price;
	}

}
