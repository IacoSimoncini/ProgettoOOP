package com.momoiaco.progetto;

import com.momoiaco.progetto.servizi.Download;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ProgettoApplication {

    /**
     * Chiama il costruttore della classe Download che avvia il download del dataset
     * La classe main avvia il server web locale sulla porta 8080 ricorrendo a spring
     *
     * @param args
     */
	public static void main(String[] args) throws IOException {
        new Download();         //Fa partire il costruttore della classe Download
        SpringApplication.run(ProgettoApplication.class, args);          //Avvia il server in locale
	}

}
