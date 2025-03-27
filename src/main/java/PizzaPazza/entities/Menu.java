package PizzaPazza.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Component
public class Menu {
	private List<Pizza> pizzaList;
	private List<Drink> drinkList;
	private List<Panuozzo> panuozzoList;
	private List<Fritto> frittoList;
}


//RACCOLTA NEL MENU