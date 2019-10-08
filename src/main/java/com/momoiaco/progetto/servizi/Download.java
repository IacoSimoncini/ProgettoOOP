package com.momoiaco.progetto.servizi;

import com.momoiaco.progetto.modello.NottiNazione;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe che carica il dataset e effettua il parsing del file .tsv
 * Un file tsv è una tabella che ha come delimiter il carattere "\t" o ","
 */
@Service
public class Download {
    public static List<NottiNazione> record = new ArrayList<>();
    private final static String TAB_DELIMITER = "\t";
    public static List<Map> Lista = new ArrayList();

    /**
     * Costruttore della classe Download
     * @throws IOException
     */
    public Download() throws IOException {
        String fileTSV = "dataset.tsv";
        if (Files.exists(Paths.get(fileTSV)))
            System.out.println("Dataset ricaricato da locale");
        else {
            String url = "http://data.europa.eu/euodp/data/api/3/action/package_show?id=CLfXgIIz02XfA2MTjWgjSQ";
            try {
                URLConnection openConnection = new URL(url).openConnection();                           //Apro connessione ad url della mail
                openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
                InputStream inStream = openConnection.getInputStream();
                StringBuilder data = new StringBuilder();
                String line;
                try {
                    //Lettura JSON e salvataggio su stringa
                    InputStreamReader inR = new InputStreamReader(inStream);
                    BufferedReader buf = new BufferedReader(inR);
                    while ((line = buf.readLine()) != null) {                           //Basterebbe anche una sola lettura poichè il JSON è su una sola riga
                        data.append(line);
                    }
                } finally {
                    inStream.close();
                }
                //Conversione StringBuilder in oggetto JSON
                JSONObject obj = (JSONObject) JSONValue.parseWithException(data.toString());
                JSONObject objI = (JSONObject) (obj.get("result"));
                JSONArray objA = (JSONArray) (objI.get("resources"));

                for (Object o : objA) {                                                 //Scorro tutti gli oggetti fino a trovare quello di formato corretto
                    if (o instanceof JSONObject) {
                        JSONObject o1 = (JSONObject) o;                                 //Converto il generico Object in JSONObject
                        String format = (String) o1.get("format");                      //Mi sposto all'interno del JSON per trovare l'url desiderato
                        String urlD = (String) o1.get("url");

                        if (format.equals("http://publications.europa.eu/resource/authority/file-type/TSV")) { //Verifico che il formato sia quello richiesto
                            try (InputStream input = URI.create(url).toURL().openStream()) {
                                Files.copy(input, Paths.get(fileTSV));
                            }
                        }
                    }
                }
                System.out.println("Download del TSV effettuato");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Parsing(fileTSV); //Effettua il parsing tramite il metodo "Parsing"
            Metadata(fileTSV); //Richiama metodo per la creazione dei metadati
        }
    }


    /**
     * Metodo che effettua il parsing del file tsv
     * @param fileTSV  Stringa con il nome del file tsv
     */
    private void Parsing(String fileTSV){
        try(BufferedReader bRead = new BufferedReader(new FileReader(fileTSV))){
            bRead.readLine();                  //Legge una riga a vuoto per saltare l'intestazione
            String linea;
            while((linea = bRead.readLine()) != null) {                 //Ciclo che continua fintanto che non trova una linea nulla
                linea = linea.replace(",", TAB_DELIMITER);       //Sostituisce le virgole con i tab "\t"
                linea = linea.replace(":","0");      //Sostituisce i ":" con "0"
                linea = linea.replace("e","");       //Sostituisce "e" con ""
                linea = linea.replace("c", "");      //Sostituisce "c" con ""
                linea = linea.replace("u", "");      //Sostituisce "u" con ""
                String[] lineaSplittata = linea.trim().split(TAB_DELIMITER);    //Separa la linea tutte le volte che incontra il tab
                String c_resid = lineaSplittata[0].trim();              //Toglie gli spazi prima e dopo la stringa
                String unit = lineaSplittata[1].trim();
                String nace_r2 = lineaSplittata[2].trim();
                String geo = lineaSplittata[3].trim();
                double[] valori = new double[NottiNazione.differenza_anni];
                for(int i = 0; i < NottiNazione.differenza_anni; i++){
                    valori[i] = Double.parseDouble(lineaSplittata[4 + i].trim());       //Inserisce i valori della tabella dentro il vettore
                }
                NottiNazione oggettoParsato = new NottiNazione(c_resid, unit, nace_r2, geo, valori);
                record.add(oggettoParsato);         //Aggiungo oggettoParsato alla lista
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Metodo per la creazione dei metadati
     * @param fileTSV   Stringa con il nome del file tsv
     * @throws IOException
     */
    private void Metadata(String fileTSV) throws IOException {
        Field[] fields = NottiNazione.class.getDeclaredFields();    //Estrae le variabili della classe NottiNazione
        BufferedReader bR = new BufferedReader(new FileReader(fileTSV));         //Apre il bufferedReader
        String linea = bR.readLine();           //Legge la prima riga
        linea = linea.replace(",", TAB_DELIMITER);     //Sostituisce alla prima linea tutte le virgole con "\t"
        String[] lineaSplittata = linea.trim().split(TAB_DELIMITER);     //Separa la stringa tutte le volte che incontra tab
        lineaSplittata = linea.trim().split("\"");      //Separa la stringa quando c'è il back slash
        int i = 0;

        for (Field f : fields) {
            Map<String, String> map = new HashMap<>();
            map.put("Alias", f.getName());      //Aggiunge alla mappa la chiave alias
            map.put("sourceField", lineaSplittata[i]);    //Prende il nome del campo nel file tsv
            map.put("type", f.getType().getSimpleName());   //Prende il tipo di dato e lo inserisce nella mappa
            Lista.add(map);             //Aggiunge la mappa alla lista "Lista"
            i++;
        }
    }
}
