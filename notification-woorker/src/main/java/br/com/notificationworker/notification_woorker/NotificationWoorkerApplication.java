package br.com.notificationworker.notification_woorker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class NotificationWoorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationWoorkerApplication.class, args);
	}

}
