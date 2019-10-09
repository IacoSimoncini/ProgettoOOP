package com.momoiaco.progetto.servizi;

import com.momoiaco.progetto.modello.NottiNazione;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static com.momoiaco.progetto.modello.NottiNazione.differenza_anni;

public class Operazioni {

    /**
     * Metodo GET che su richiesta dell'utente restituisce l'intero dataset
     *
     * @return "record" ovvero la lista con gli oggetti del dataset
     */
    @GetMapping("/getData")
    public List getRecord(){
        return Download.record;
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce il vettore degli anni
     *
     * @return "anni" ovvero una lista di string
     */
    @GetMapping("/getAnni")
    public List getAnni(){
        List<String> anni = new ArrayList<>();
        for(int i = 0; i < differenza_anni; i++)
            anni.add(Integer.toString(2007+i));
        return anni;
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce la lista dei metadata
     *
     * @return "Lista" ovvero la lista nella classe Download che contiene i metadata
     */
    @GetMapping("/getMetadati")
    public List getMetadati(){
        return Download.Lista;
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce un elemento all'indice i della lista "record" che contiene gli oggetti NottiNazione
     * @param i indice della lista che si vuole ottenere
     * @return "record" ovvero la lista con gli oggetti NottiNazione
     */
    @GetMapping("/getRecord[i]")
    public NottiNazione getNottiNazione(@PathVariable int i){
        if(i < Download.record.size()) return Download.record.get(i);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oggetto di indice " + i + " non esiste!");
    }

    /*@GetMapping("/getStatistiche")
    public List getStatistiche(@RequestParam(value = "Campo", required = false, defaultValue = "") String nomeCampo) {

    }

    @GetMapping("")
    */
}
