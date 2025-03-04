package PizzaPazza.entities;

import lombok.Getter;

@Getter
public abstract class Item {

	protected double price;

	public Item(double price) {
		this.price = price;
	}

}
