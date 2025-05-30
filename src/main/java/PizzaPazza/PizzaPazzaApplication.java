package PizzaPazza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("PizzaPazza")
@EntityScan("PizzaPazza")
public class PizzaPazzaApplication {
	public static void main(String[] args) {
		SpringApplication.run(PizzaPazzaApplication.class, args);
	}
}

