package com.momoiaco.progetto.servizi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe Statistics
 * restituisce statistiche sui dati informato JSON
 */
public abstract  class Statistics {

    /**
     * Metodo avg che effettua la media degli elementi all'interno della lista
     *
     * @param lista lista di double
     * @return  restituisce la media della lista
     */
    public static double avg (List<Double> lista){
        return sum(lista) / count(lista) ;
    }

    /**
     * Metodo min che calcola il minimo all'interno della lista
     *
     * @param lista lista di double
     * @return restituisce il minimo della lista
     */
    public static double min(List<Double> lista) {
        double min = lista.get(0);
        for(Double num : lista ){
            if (num < min) min = num;
        }
        return min;
    }

    /**
     * Metodo max che calcola il massimo di una lista
     *
     * @param lista lista di double
     * @return restituisce il massimo di una lista
     */
    public static double max (List<Double> lista) {
        double max = lista.get(0);
        for (Double num : lista) {
            if (num > max) max = num;
        }
        return max;
    }

    /**
     * Metodo devStd che calcola la deviazione standard
     *
     * @param lista lista di double
     * @return restituisce la deviazione standard
     */
    public static double devStd(List<Double> lista) {
        double avg = avg(lista) ;
        double var = 0;
        for (Double num : lista ) {
            var += Math.pow(num - avg, 2);
        }
        return Math.sqrt(var);
    }

    /**
     * Metodo sum che calcola la somma di tutti gli elementi all'interno della lista
     *
     * @param lista lista di double
     * @return restituisce la somma degli elementi della lista
     */
    public static double sum(List<Double> lista) {
        double s = 0 ;
        for(Double n : lista) {
            s += n;
        }
        return s;
    }

    /**
     * Metodo count che restituisce la lunghezza della lista
     *
     * @param lista lista
     * @return restituisce la lunghezza della lista
     */
    public static int count(List lista){
        return lista.size();
    }

    /**
     * Metodo contaElementiUnici che conta quante volte un elemento è inserito all'interno di una lista
     *
     * @param lista lista
     * @return restituisce il numero degli elementi unici
     */
    public static Map<Object,Integer> contaElementiUnici(List lista){
        Map<Object, Integer> mappa = new HashMap<>();
        for (Object obj : lista){
            Integer numero = mappa.get(obj);
            mappa.put(obj, ( numero == null ? 1 : numero + 1));
        }
        return mappa;
    }

    /**
     * Metodo get che restituisce tutte le statistiche relative ad un campo
     *
     * @param nameField nome del campo
     * @param list lista
     * @return restituisce una mappa con tutte le statistiche relative al campo inserito
     */
    public static Map<String, Object> getAllStatistics(String nameField, List list){
        Map<String, Object> mappa = new HashMap<>();
        mappa.put("field",nameField);
        if(!list.isEmpty()){                                       //Se la lista non è vuota
            if(list.get(0) instanceof Number){
                List<Double> listaNumer = new ArrayList<>();
                for(Object oggetto : list){                        //Ciclo che scorre gli oggetti
                    listaNumer.add((Double) oggetto);
                }
                mappa.put("avg", avg(listaNumer));
                mappa.put("min", min(listaNumer));
                mappa.put("max", max(listaNumer));
                mappa.put("std", devStd(listaNumer));
                mappa.put("sum", sum(listaNumer));
                mappa.put("count", count(listaNumer));
            } else {
                mappa.put("elementiUnici", contaElementiUnici(list));
                mappa.put("count", count(list));
            }
        }
        return mappa;
    }
}
