package com.momoiaco.progetto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @author Iacopo Simoncini, Mohammad Wafa
 *
 */
@SpringBootApplication
public class ProgettoApplication {

    /**
     * Chiama il costruttore della classe Download che avvia il download del dataset
     * La classe main avvia il server web locale sulla porta 8080 ricorrendo a spring
     *
     * @param args
     */
	public static void main(String[] args) throws IOException {
        SpringApplication.run(ProgettoApplication.class, args);          //Avvia il server in locale
	}

}
