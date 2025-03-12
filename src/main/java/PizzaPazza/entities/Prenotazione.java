package PizzaPazza.entities;

import PizzaPazza.PizzaPazzaSecurity.model.entities.Utente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

	@ManyToOne
	@JoinColumn(name = "idUtente", nullable = false)
	private Utente utente;

	@Column(nullable = false)
	private LocalDateTime dataOraPrenotazione;

	@Column(nullable = false)
	private int numeroPersone;

	@Column
	private String altrePreferenze;

	@Column
	private String cognome;
}
