package org.asu.chilll.power;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PowerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowerApplication.class, args);
		System.out.println();
		System.out.println("========================================== START POWER PROGRAM SUCCESSFULLY! ==========================================");
	}
}
