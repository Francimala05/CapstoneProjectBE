package PizzaPazza.entities;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Order {
	private int numeroOrdine;
	private StatoOrdine state;
	private int numCoperti;
	private LocalTime oraAcquisizione;
	private Table table;

	public Order(int numCoperti, Table table) {
		Random rndm = new Random();
		if (table.getNumMaxCoperti() <= numCoperti)
			throw new RuntimeException("Numero coperti maggiore di numero massimo posti sul tavolo!");
		this.numeroOrdine = rndm.nextInt(1000, 100000);
		this.state = StatoOrdine.IN_CORSO;
		this.numCoperti = numCoperti;
		this.oraAcquisizione = LocalTime.now();
		this.table = table;
	}



	public void print() {
		System.out.println("id ordine--> " + this.numeroOrdine);
		System.out.println("stato--> " + this.state);
		System.out.println("numero coperti--> " + this.numCoperti);
		System.out.println("ora acquisizione--> " + this.oraAcquisizione);
		System.out.println("numero tavolo--> " + this.table.getNumTable());
		System.out.println("Lista: ");
		System.out.println("totale--> ");

	}
}
