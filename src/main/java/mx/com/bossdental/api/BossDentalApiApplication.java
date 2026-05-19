package mx.com.bossdental.api;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.TimeZone;

@SpringBootApplication
public class BossDentalApiApplication {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
	}

	public static void main(String[] args) {
		//System.out.println("### test: " + new BCryptPasswordEncoder().encode("admin123"));
		SpringApplication.run(BossDentalApiApplication.class, args);
	}

}
