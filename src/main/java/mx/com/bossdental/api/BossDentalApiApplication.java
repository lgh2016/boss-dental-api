package mx.com.bossdental.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.TimeZone;

@SpringBootApplication
public class BossDentalApiApplication {

	public static void main(String[] args) {
		//System.out.println("### test: " + new BCryptPasswordEncoder().encode("admin123"));
		TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
		SpringApplication.run(BossDentalApiApplication.class, args);
	}

}
