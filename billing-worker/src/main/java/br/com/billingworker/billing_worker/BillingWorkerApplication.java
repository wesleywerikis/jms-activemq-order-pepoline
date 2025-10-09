package br.com.billingworker.billing_worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;


@SpringBootApplication
@EnableJms
public class BillingWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingWorkerApplication.class, args);
	}

}
