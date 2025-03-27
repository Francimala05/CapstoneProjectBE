package PizzaPazza.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name= "pizze")
public class Pizza{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "topping_name")
	private String toppingNames;

	private double price;
	private double mezzoChiloPrice;
	private double chiloPrice;
	private String imageUrl;

	//COLLEGO A ORDINEASPORTO E ORDINEDOMICILIO
	@ManyToOne
	@JoinColumn(name = "ordine_asporto_id")
	private OrdineAsporto ordineAsporto;

	@ManyToOne
	@JoinColumn(name = "ordine_domicilio_id")
	private OrdineDomicilio ordineDomicilio;



	public Pizza() {
	}


	public Pizza(String name, String toppingNames, double price, double mezzoChiloPrice, double chiloPrice, String imageUrl) {
		this.name = name;
		this.toppingNames = toppingNames;
		this.price = price;
		this.mezzoChiloPrice = mezzoChiloPrice;
		this.chiloPrice = chiloPrice;
		this.imageUrl = imageUrl;
	}
}
