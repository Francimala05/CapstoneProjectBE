package PizzaPazza.entities;

import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="prenotazioni")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prenotazione {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPrenotazione;

	//COLLEGO A UTENTE
	@ManyToOne
	@JoinColumn(name = "idUtente", nullable = false)
	private Utente utente;

	@Column(nullable = false)
	private LocalDate data;

	@Column(nullable = false)
	private LocalTime orario;

	@Column(nullable = false)
	private int numeroPersone;

	@Column
	private String altrePreferenze;

	@Column
	private String cognome;
}
