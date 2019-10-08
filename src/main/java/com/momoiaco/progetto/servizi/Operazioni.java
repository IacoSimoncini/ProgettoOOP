package com.momoiaco.progetto.servizi;

import com.momoiaco.progetto.modello.NottiNazione;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.Utilities;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.momoiaco.progetto.modello.NottiNazione.differenza_anni;

public class Operazioni {

    @GetMapping("/getRecord")
    public List getRecord(){
        return Download.record;
    }

    @GetMapping("/getAnni")
    public List getAnni(){
        List<String> anni = new ArrayList<>();
        for(int i = 0; i < differenza_anni; i++)
            anni.add(Integer.toString(2000+i));
        return anni;
    }

    @GetMapping("/getMetadati")
    public List getMetadati(){
        return Download.Lista;
    }

    @GetMapping("/Record[i]")
    public NottiNazione getNottiNazione(@PathVariable int i){
        if(i < Download.record.size()) return Download.record.get(i);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oggetto di indice " + i + " non esiste!")
    }

    @GetMapping("/getStatistiche")
    public List getStatistiche(@RequestParam(value = "Campo", required = false, defaultValue = "") String nomeCampo) {

    }
}
