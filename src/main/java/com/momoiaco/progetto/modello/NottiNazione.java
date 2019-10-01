package com.momoiaco.progetto.modello;


public class NottiNazione {

    private char freq;
    private String c_resid, unit, nace_r2, geo;
    private double[] valori = new double[12];

    //Costruttore
    public NottiNazione(char freq, String c_resid, String unit, String nace_r2, String geo, double valori[]){
        this.freq = freq;
        this.c_resid = c_resid;
        this.unit = unit;
        this.nace_r2 = nace_r2;
        this.geo = geo;
        for(int i = 0 ; i < 12; i++){
            this.valori[i] = valori[i];
        }
    }

    public char getFreq() { return freq; }

    public String getC_resid() { return c_resid; }

    public String getUnit() { return unit; }

    public String getNace_r2() { return nace_r2; }

    public String getGeo() { return geo; }

    @Override
    public String toString() {    //Metodo che restituisce la string del record
        return "NottiNazione: " + "freq = " + freq + ", c_resid = " + c_resid + ", unit = " + unit + ", nace_r2 = " +  nace_r2 + ", geo = " + geo + ";" ;
    }
}
