package com.momoiaco.progetto;

import com.momoiaco.progetto.servizi.DownloadTSV;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProgettoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgettoApplication.class, args);
	}
		private String indirizzo = "http://data.europa.eu/euodp/data/api/3/action/package_show?id=CLfXgIIz02XfA2MTjWgjSQ";
		new DownloadTSV(indirizzo);


}
