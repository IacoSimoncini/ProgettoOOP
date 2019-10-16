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


    @GetMapping("/getStatistiche")
    public Map getStatistiche(@RequestParam(value = "Field", required = false, defaultValue = "") String nameField) {
        return Statistics.getAllStatistics(nameField, service.getField(nameField));
    }


    @PostMapping("/getFilteredStatistiche")
    public Map getFilteredStatistiche(@RequestParam(value = "Field", required = false, defaultValue = "") String fieldStatistics, @RequestBody String body){
        Map<String, Object> filter = parsingFilter(body);
        List<NottiNazione> filteredRecord = new ArrayList<>();
        List<Integer> filteredIndici = Filtri.filtra(service.getField((String) filter.get("Field")), (String) filter.get("Operator"), filter.get("Reference"));
        for(int i : filteredIndici){
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
        Map<String, Object> filter = parsingFilter(body);
        String nameField = (String) filter.get("Field");
        String oper = (String) filter.get("Operator");
        Object reference = filter.get("Reference");
        return service.getFilteredRecord(nameField, oper, reference);
    }

    /**
     * Metodo che effettua il parsing del filtro
     *
     * @param body body
     * @return filter, restituisce la mappa filtro
     */
    public Map<String, Object> parsingFilter(String body){
        Map<String, Object> bodyParsato = new BasicJsonParser().parseMap(body);
        System.out.println(body);
        String nameField = bodyParsato.keySet().toArray(new String[0])[0];
        System.out.println(nameField);
        Object value = bodyParsato.get(nameField);
        String operator;
        Object reference;
        if(value instanceof Map){
            Map filter = (Map) value;
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

    /*public Map<String, Object> parsingFilter(String body){
        Map<String, Object> parsedBody = new HashMap<>();
        String nameField;
        String operator;
        Object reference;
        String[] parsedString = body.trim().split(";");
        nameField = parsedString[0];
        operator = parsedString[1];
        reference = parsedString[2];
        parsedBody.put("Field", nameField);
        parsedBody.put("Operator", operator);
        parsedBody.put("Reference", reference);
        return parsedBody;
    }
*/
}
