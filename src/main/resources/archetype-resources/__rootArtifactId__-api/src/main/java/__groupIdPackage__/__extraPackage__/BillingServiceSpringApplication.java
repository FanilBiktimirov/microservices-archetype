package ${package}.${extraPackageForClass};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillingServiceSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceSpringApplication.class, args);
    }
}
