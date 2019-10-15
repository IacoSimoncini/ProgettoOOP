package com.momoiaco.progetto.controllo;

import com.momoiaco.progetto.modello.NottiNazione;
import com.momoiaco.progetto.servizi.Download;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NottiNazioneControllo {
    private Download service;

    /**
     * Costruttore della classe NottiNazioneControllo
     *
     * @param service
     */
    @Autowired
    public NottiNazioneControllo(Download service) {
        this.service = service;
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce l'intero dataset
     *
     * @return "record" ovvero la lista con gli oggetti del dataset
     */
    @GetMapping("/getRecord")
    public List getRecord() {
        return service.getRecord();
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce il vettore degli anni
     *
     * @return "anni" ovvero una lista di string
     */
    @GetMapping("/getTime")
    public List getAnni() {
        return service.getTime();
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce la lista dei metadata
     *
     * @return "Lista" ovvero la lista nella classe Download che contiene i metadata
     */
    @GetMapping("/getMetadati")
    public List getMetadati() {
        return service.getMetadata();
    }

    /**
     * Metodo GET che su richiesta dell'utente restituisce un elemento all'indice i della lista "record" che contiene gli oggetti NottiNazione
     *
     * @param i indice della lista che si vuole ottenere
     * @return "record" ovvero la lista con gli oggetti NottiNazione
     */
    @GetMapping("/getRecord/{i}")
    public NottiNazione getNottiNazione(@PathVariable int i) {
        return service.getRecord(i);
    }


    @GetMapping("/getStatistiche")
    public List getStatistiche(@RequestParam(value = "Field", required = false, defaultValue = "") String nameField) {
        if(nameField.equals(""))
            return service.getAllFieldStatistics();
        else {
            return service.getField(nameField);
        }
    }

    //METODI POST

    //CREARE METODO GETFILTEREDSTATISTICS

    //CREARE PARSING FILTRO

    @PostMapping("/getFilteredRecord")
    public List getFilteredRecord(@RequestBody String body){
        Map<String, Object> filter = parsingFiltro (body);
        String nameField = (String) filter.get("field");
        String oper = (String) filter.get("oper");
        Object rif = filter.get("rif");
        return service.getFilteredRecord(nameField, oper, rif);
    }











}
