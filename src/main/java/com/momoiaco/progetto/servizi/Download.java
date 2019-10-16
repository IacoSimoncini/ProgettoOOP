package com.momoiaco.progetto.servizi;

import com.momoiaco.progetto.modello.NottiNazione;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
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
    private static List<NottiNazione> record = new ArrayList<>();        //Lista di oggetti NottiNazione
    private final static String TAB_DELIMITER = "\t";
    private static List<Map> Lista = new ArrayList();                    //Lista per i Metadata
    static List<String> time = new ArrayList<>();

    /**
     * Costruttore della classe Download
     *
     * @throws IOException
     */
    public Download() throws IOException {
        String fileTSV = "dataset.tsv";
        for(int i = 0; i < NottiNazione.differenza_anni; i++)       //Inizializzo il vettore tempo
            time.add(Integer.toString(2007+i));
        if (Files.exists(Paths.get(fileTSV)))
            System.out.println("Dataset ricaricato da locale");
        else {
            String url = "http://data.europa.eu/euodp/data/api/3/action/package_show?id=CLfXgIIz02XfA2MTjWgjSQ";
            try {
                URLConnection openConnection = new URL(url).openConnection();
                openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                InputStream in = openConnection.getInputStream();

                String data = "";
                String line = "";
                try {
                    InputStreamReader inR = new InputStreamReader( in );
                    BufferedReader buf = new BufferedReader( inR );

                    while ( ( line = buf.readLine() ) != null ) {
                        data += line;
                        System.out.println( line );
                    }
                } finally {
                    in.close();
                }
                //Conversione StringBuilder in oggetto JSON
                JSONObject obj = (JSONObject) JSONValue.parseWithException(data.toString());
                JSONObject objI = (JSONObject) (obj.get("result"));
                JSONArray objA = (JSONArray) (objI.get("resources"));

                for (Object o : objA) {
                    if (o instanceof JSONObject) {
                        JSONObject o1 = (JSONObject) o;
                        String format = (String) o1.get("format");
                        String urlD = (String) o1.get("url");
                        System.out.println(format + " | " + urlD);
                        if (format.toLowerCase().contains("tsv")) {
                            //downloadTSV(urlD, fileName);
                            downloadTSV(urlD, fileTSV);
                        }
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Parsing(fileTSV);
        Metadata(fileTSV);
    }

    /**
     * Metodo che gestisce un problema di redirect del sito che gestisce i dati
     *
     * @param url url del sito dal quale scaricare il file
     * @param fileName nome del file
     * @throws Exception
     */
    public static void downloadTSV(String url, String fileName) throws Exception {
        HttpURLConnection openConnection = (HttpURLConnection) new URL(url).openConnection();
        openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        InputStream in = openConnection.getInputStream();
        String data = "";
        String line = "";
        try {
            if(openConnection.getResponseCode() >= 300 && openConnection.getResponseCode() < 400) {
                downloadTSV(openConnection.getHeaderField("Location"),fileName);        //Richiama il metodo downloadTSV
                in.close();
                openConnection.disconnect();
                return;
            }
            Files.copy(in, Paths.get(fileName));
            System.out.println("File size " + Files.size(Paths.get(fileName)));
            } finally {
                in.close();
            }
    }

    /**
     * Metodo che effettua il parsing del file tsv
     *
     * @param fileTSV  Stringa con il nome del file tsv
     */
    private void Parsing(String fileTSV){
        try(BufferedReader bRead = new BufferedReader(new FileReader(fileTSV))){
            bRead.readLine();                                  //Legge una riga a vuoto per saltare l'intestazione
            String linea;
            int a;
            while((linea = bRead.readLine()) != null) {                 //Ciclo che continua fintanto che non trova una linea nulla
                linea = linea.replace(",", TAB_DELIMITER);       //Sostituisce le virgole con i tab "\t"
                linea = linea.replace(":","0");      //Sostituisce i ":" con "0"
                linea = linea.replace("e","");       //Sostituisce "e" con ""
                linea = linea.replace("c", "");      //Sostituisce "c" con ""
                linea = linea.replace("u", "");      //Sostituisce "u" con ""
                linea = linea.replace("b","");
                String[] lineaSplittata = linea.trim().split(TAB_DELIMITER);                     //Separa la linea tutte le volte che incontra il tab
                String c_resid = lineaSplittata[0].trim();                                     //Trim toglie gli spazi prima e dopo la stringa
                String unit = lineaSplittata[1].trim();
                String nace_r2 = lineaSplittata[2].trim();
                String geo = lineaSplittata[3].trim();
                double[] valori = new double[NottiNazione.differenza_anni];
                for(int i = 0; i < NottiNazione.differenza_anni; i++) {
                    if (4 + i < lineaSplittata.length) {                                  //Gestione errore java.lang.ArrayIndexOutOfBoundsException
                        valori[i] = Double.parseDouble(lineaSplittata[4 + i].trim());       //Inserisce i valori della tabella dentro il vettore
                    }
                    else
                        valori[i] = 0;                                                      //Per i valori che non ci sono dopo lineaSplittata aggiunge "0"
                }
                NottiNazione oggettoParsato = new NottiNazione(c_resid, unit, nace_r2, geo, valori);
                record.add(oggettoParsato);                                          //Aggiungo oggettoParsato alla lista
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Metodo per la creazione dei metadati
     *
     * @param fileTSV   Stringa con il nome del file tsv
     * @throws IOException
     */
    private void Metadata(String fileTSV) throws IOException {
        Field[] fields = NottiNazione.class.getDeclaredFields();            //Estrae le variabili della classe NottiNazione
        BufferedReader bR = new BufferedReader(new FileReader(fileTSV));         //Apre il bufferedReader
        String linea = bR.readLine();                                        //Legge la prima riga
        linea = linea.replace(",", TAB_DELIMITER);                    //Sostituisce alla prima linea tutte le "," con "\t"
        linea = linea.replace("\\", TAB_DELIMITER);                   //Sostituisce alla prima linea \ con tab
        String[] lineaSplittata = linea.trim().split(TAB_DELIMITER);         //Separa la stringa tutte le volte che incontra "\t"
        int i = 0;

        for (Field f : fields) {
            Map<String, String> map = new HashMap<>();
            map.put("Alias", f.getName());                       //Aggiunge alla mappa la chiave alias
            map.put("SourceField", lineaSplittata[i]);          //Prende il nome del campo nel file tsv
            map.put("Type", f.getType().getSimpleName());         //Prende il tipo di dato e lo inserisce nella mappa
            Lista.add(map);                                    //Aggiunge la mappa alla lista "Lista"
            i++;
        }
    }

    /**
     * Metodo che restituisce il record
     *
     * @return record
     */
    public List<NottiNazione> getRecord(){
        return record;
    }

    /**
     * Metodo che restituisce una lista di String contenente gli anni
     *
     * @return anni
     */
    public List getTime(){ return time;  }

    /**
     * Metodo che restituisce la lista dei metadati
     *
     * @return Lista
     */
    public List<Map> getMetadata(){
        return Lista;
    }

    /**
     * Metodo che restituisce il record all'indice i
     *
     * @param i
     * @return
     */
    public NottiNazione getRecord(int i){
        if(i < record.size()) return record.get(i);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oggetto di indice " + i + " non esiste!");
    }

    /**
     * Metodo che restituisce la lista con i valori di un certo campo dei record
     *
     * @param nomeCampo nome del campo rispetto a cui si vogliono ottenere i valori
     * @return lista con i valori del campo
     */
    public List getField(String nomeCampo) {
        List<Object> listField = new ArrayList<>(); //inizializzo lista che conterrà i valori del campo
        try {
            /*
            Gestisco il caso in cui il nome del campo sia un anno:
            In questo caso verifico se sia uno degli anni all'interno del vettore time
            Se questo è vero allora inserisco dentro dentro "ob" i valori relativi al nome del campo inserito
             */
            if(time.contains(nomeCampo)){
                for(NottiNazione notti : record){
                    Object ob= notti.getValori()[Integer.parseInt(nomeCampo)-2000]; //considero solo l'elemento che mi interessa del metodo get
                    listField.add(ob);
                }
            }
            /*
            Nel caso in cui il nome del campo non sia un anno:
            Scorro tutti gli oggetti all'interno della classe record e vado ad estrarre
            i valori del campo relativo al nome del campo inserito dall'utente.
            All'interno del ciclo viene caricato l'oggetto relativo al campo da
             */
            else {
                //serve per scorrere tutti gli oggetti ed estrarre i valori del campo nomeCampo
                for (NottiNazione notti : record) {
                    Method getter = NottiNazione.class.getMethod("get" + nomeCampo.substring(0, 1).toUpperCase() + nomeCampo.substring(1)); //costruisco il metodo get del modello di riferimento
                    Object value = getter.invoke(notti); //invoco il metodo get sull'oggetto della classe modellante
                    listField.add(value); //aggiungo il valore alla lista
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, nomeCampo + " non esiste.");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return listField; //ritorno la lista
    }

    /**
     * Metodo che restituisce la lista di tutte le statistiche relative a tutti i campi
     *
     * @return lista delle statistiche
     */
    public List<Map> getAllFieldStatistics(){
        Field[] fields = NottiNazione.class.getDeclaredFields();
        List<Map> list = new ArrayList<>();
        for(Field campo : fields){
            String fieldName = campo.getName();
            if(fieldName.equals("record"))
                for(int i = 0; i < NottiNazione.differenza_anni; i++)
                    list.add(Statistics.getAllStatistics(fieldName, getField(fieldName)));
            else
                list.add(Statistics.getAllStatistics(fieldName, getField(fieldName)));
        }
        return list;
    }

    /**
     * Metodo che restituisce il record filtrato rispetto al nome del campo, operatore e reference
     *
     * @param nameField nome del campo passato dall'utente
     * @param operator operatore passato dall'utente
     * @param reference oggetto passato dall'utente
     * @return restituisce la lista con gli oggetti filtrati
     */
    public List<NottiNazione> getFilteredRecord(String nameField, String operator, Object reference) {
        List<Integer> filteredList = Filtri.filtra(getField(nameField), operator, reference);
        List<NottiNazione> filtered = new ArrayList<>();
        for (int i : filteredList) {
            filtered.add(record.get(i));
        }
        return filtered;
    }
}