package PizzaPazza.entities;
import lombok.Getter;

import java.util.Arrays;

@Getter
public abstract class Item {
	protected double price;

	public Item(double price) {
		this.price = price;
	}

}
