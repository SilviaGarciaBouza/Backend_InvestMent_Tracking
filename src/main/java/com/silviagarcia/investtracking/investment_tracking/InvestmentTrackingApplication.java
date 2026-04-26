package com.silviagarcia.investtracking.investment_tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Clase principal de la aplicación.
 * Gestiona el inicio del backend y la persistencia de datos.
 */
@SpringBootApplication
public class InvestmentTrackingApplication {
	/**
	 * Punto de entrada de la aplicación.
	 * @param args Argumentos de configuración por línea de comandos.
	 */
	public static void main(String[] args) {
		SpringApplication.run(InvestmentTrackingApplication.class, args);
	}

}
