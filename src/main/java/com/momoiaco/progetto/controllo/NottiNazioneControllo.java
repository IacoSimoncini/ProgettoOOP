package com.momoiaco.progetto.controllo;

import com.momoiaco.progetto.modello.NottiNazione;
import com.momoiaco.progetto.servizi.Download;
import com.momoiaco.progetto.servizi.Filtri;
import com.momoiaco.progetto.servizi.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
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

    /**
     * Metodo GET che su richiesta dell'utente restituisce tutte le statistiche relative ad un campo
     *
     * @param nameField nome del campo inserito dall'utente
     * @return restituisce tutte le statistiche relative al campo nameField
     */
    @GetMapping("/getStatistiche")
    public Map getStatistiche(@RequestParam(value = "Field", required = false, defaultValue = "") String nameField) {
        return Statistics.getAllStatistics(nameField, service.getField(nameField));
    }

    /**
     * Metodo POST che su richiesta dell'utente restituisce le statistiche filtrate relative ad un campo
     *
      * @param fieldStatistics
     * @param body il JSON passato dall'utente con i relativi campo, operatore e riferimento
     * @return restituice le statistiche filtrate di un campo inserito dall'utente
     */
    @PostMapping("/getFilteredStatistiche")
    public Map getFilteredStatistiche(@RequestParam(value = "Field", required = false, defaultValue = "") String fieldStatistics, @RequestBody String body){
        Map<String, Object> filter = parsingFilter(body);                               //Effettua il parsing del body
        List<NottiNazione> filteredRecord = new ArrayList<>();
        List<Integer> filteredIndici = Filtri.filtra(service.getField((String) filter.get("Field")), (String) filter.get("Operator"), filter.get("Reference"));         //Richiama la funzione filtra all'interno della classe Filtri che filtra il campo passato dall'utente insieme all'operatore e al riferimento
        for(int i : filteredIndici){                //For each che inserisce all'interno della lista filteredRecord tutti gli elementi del record filtrati
            filteredRecord.add(service.getRecord(i));
        }
        return Statistics.getAllStatistics(fieldStatistics, filteredRecord);
    }

    /**
     * Metodo get che restituisce il record filtrato passando il body al metodo
     *
     * @param body body
     * @return
     */
    @PostMapping("/getFilteredRecord")
    public List getFilteredRecord(@RequestBody String body){
        Map<String, Object> filter = parsingFilter(body);               //Effettua il parsing del body
        String nameField = (String) filter.get("Field");                //Inserisce all'interno della variabile nameField il campo passato dall'utente
        String oper = (String) filter.get("Operator");                  //Inserisce all'interno della variabile l'operatore passato dall'utente
        Object reference = filter.get("Reference");                     //Inserisce all'interno di reference l'oggetto passato dall'utente
        return service.getFilteredRecord(nameField, oper, reference);      //Richiama la funzione getFilteredRecord della classe Download
    }

    /**
     * Metodo che effettua il parsing JSON del filtro
     *
     * @param body body
     * @return filter, restituisce la mappa filtro
     */
    public Map<String, Object> parsingFilter(String body){
        Map<String, Object> bodyParsato = new BasicJsonParser().parseMap(body);                 //Effettua il parsing del body
        String nameField = bodyParsato.keySet().toArray(new String[0])[0];
        Object value = bodyParsato.get(nameField);                                              //Inserisce dentro value il nome del campo da bodyParsato
        String operator;
        Object reference;
        if(value instanceof Map){
            Map filter = (Map) value;                                                           //Inserisce in filter value castandolo come mappa
            operator = ((String) filter.keySet().toArray()[0]).toLowerCase();
            reference = filter.get(operator);
        } else {
            operator = "$gte";
            reference = value;
        }
        Map<String, Object> filter = new HashMap<>();
        filter.put("Operator", operator);
        filter.put("Reference", reference);
        filter.put("Field", nameField);
        return filter;
    }
}
