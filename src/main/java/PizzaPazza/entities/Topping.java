package PizzaPazza.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Topping extends Item {
	private String name;

	public Topping(String name,double price) {
		super(price);
		this.name = name;
	}

	@Override
	public String toString() {
		return "Topping{" +
				"name='" + name + '\'' +
				", price=" + price +
				'}';
	}
}
