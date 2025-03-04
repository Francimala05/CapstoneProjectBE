package PizzaPazza.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Table {
	private int numTable;
	private int numMaxCoperti;
	private boolean isFree;
	private double costoCoperto;

}
