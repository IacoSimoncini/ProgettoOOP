package com.momoiaco.progetto.modello;

import java.io.Serializable;

/**
 * @author Iacopo Simoncini, Mohammad Wafa
 * Classe modellante dataset
 */
public class NottiNazione implements Serializable {     //Serializable permette di salvare gli oggetti della classe su file

    private String c_resid, unit, nace_r2, geo;
    private double[] valori;
    public static final int differenza_anni = 12;

    /**
     * Costruttore della classe NottiNazione
     * @param c_resid
     * @param unit
     * @param nace_r2
     * @param geo
     * @param valori
     */
    public NottiNazione (String c_resid, String unit, String nace_r2, String geo, double[] valori) {
        this.c_resid = c_resid;
        this.unit = unit;
        this.nace_r2 = nace_r2;
        this.geo = geo;
        this.valori = valori;
    }

    /**
     * Metodi get per C_resid
     * @return
     */
    public String getC_resid() {
        return c_resid;
    }

    /**
     * Metodo get per Unit
     * @return
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Metodi get per getNace_r2
     * @return
     */
    public String getNace_r2() {
        return nace_r2;
    }

    /**
     * Metodi get per geo
     * @return
     */
    public String getGeo() {
        return geo;
    }

    /**
     * Metodo toString che restituisce il record
     * @return  restituisce il record sotto forma di stringa
     */
    @Override
    public String toString() {
        StringBuilder record;
        record = new StringBuilder("NottiNazione: " + ", c_resid = " + c_resid + ", unit = " + unit + ", nace_r2 = " + nace_r2 + ", geo = " + geo + ";");
        for (int i = 0; i < differenza_anni; i++)
            record.append(" anno = ").append(2007 + i).append(" valori = ").append(valori[i]).append(";");
        return record.toString();
    }
}
