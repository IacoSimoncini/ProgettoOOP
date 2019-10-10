package com.momoiaco.progetto.servizi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe Statistics
 * restituisce statistiche sui dati informato JSON
 */
public abstract  class Statistics {

    /**
     * Metodo avg
     *
     * @param lista
     * @return  restituisce la media della lista
     */
    public static double avg (List<Double> lista){
        return sum(lista) / count(lista) ;
    }

    /**
     * Metodo min
     *
     * @param lista
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
     * Metodo max
     *
     * @param lista
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
     * Metodo devStd
     *
     * @param lista
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
     * Metodo sum
     *
     * @param lista
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
     * Metodo count
     *
     * @param lista
     * @return restituisce la lunghezza della lista
     */
    public static int count(List lista){
        return lista.size();
    }

    /**
     * Metodo contaElementiUnici
     *
     * @param lista
     * @return restituisce il numero degli elementi unici
     */
    public static Map<Object,Integer> contaElementiUnici(List lista){
        Map<Object, Integer> mappa = new HashMap<>();
        for (Object obj : lista){
            Integer numero = mappa.get(0);
            mappa.put(obj, ( numero == null ? 1 : numero +1));
        }
        return mappa;
    }
}
