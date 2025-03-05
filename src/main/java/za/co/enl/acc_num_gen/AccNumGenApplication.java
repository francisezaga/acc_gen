package za.co.enl.acc_num_gen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import za.co.enl.acc_num_gen.services.AccountNumberGeneratorService;

@SpringBootApplication
public class AccNumGenApplication implements CommandLineRunner {

	@Autowired
	private AccountNumberGeneratorService accountNumberGeneratorService;

	public static void main(String[] args) {
		SpringApplication.run(AccNumGenApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		accountNumberGeneratorService.generateAccountNumbers();
	}
}
