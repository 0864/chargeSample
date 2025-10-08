package code.sample.premcharge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PremChargeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PremChargeApplication.class, args);
    }

}
