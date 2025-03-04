package PizzaPazza.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Entity
@Table(name= "pizze")
public class Pizza extends Item {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


	private String name;
	private boolean isXl = false;

	@ElementCollection
	private List<String> toppingNames;

	public Pizza(String name, List<String> toppingNames,double price, boolean isXl) {
		super(price);
		this.name = name;
		this.isXl = isXl;
		this.toppingNames = toppingNames;
	}

	@Override
	public double getPrice() {
		return super.getPrice();
	}

	public void setPrice(double price) {
	}

	public void setImageUrl(String imageName) {
	}
}
