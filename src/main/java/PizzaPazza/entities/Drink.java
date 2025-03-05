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

	public Drink() {
	}

	public Drink(String name, String formato, double price) {
		this.name = name;
		this.formato=formato;
		this.price=price;
	}

}
