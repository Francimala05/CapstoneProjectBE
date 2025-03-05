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
	private boolean isXl = false;

	@Column(name = "topping_name")
	private String toppingNames;

	private double price;

	public Pizza(String name, List<String> toppingNames, double price, boolean isXl) {
		this.name = name;
		this.isXl = isXl;
		this.toppingNames = String.join(",",toppingNames);
		this.price= price;
	}



}
