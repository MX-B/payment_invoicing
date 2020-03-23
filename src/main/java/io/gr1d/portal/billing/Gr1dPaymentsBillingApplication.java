package io.gr1d.portal.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableCaching
@EnableScheduling
@SpringBootApplication
@EnableFeignClients("io.gr1d.portal.billing.api")
public class Gr1dPaymentsBillingApplication {
	
	public static void main(final String[] args) {
		SpringApplication.run(Gr1dPaymentsBillingApplication.class, args);
	}
	
}
