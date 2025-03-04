package PizzaPazza.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Drink extends Item {
	private String name;

	public Drink(String name,double price) {
		super(price);
		this.name = name;
	}

	@Override
	public String toString() {
		return "Drink{" +
				"name='" + name + '\'' +
				", calories=" +
				", price=" + price +
				'}';
	}
}
