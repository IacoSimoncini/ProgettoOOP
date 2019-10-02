package com.momoiaco.progetto.servizi;

import java.io. BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class DownloadTSV {
    private  File TSV = new File("dataset.tsv");

    public DownloadTSV(String indirizzo){
        try{
            String data = "";
            URL url = new URL(indirizzo);
            URLConnection apriConnessione = url.openConnection();
            apriConnessione.addRequestProperty("User-Agent","Chrome");
            InputStream input = apriConnessione.getInputStream();
            try{
                BufferedReader bReader = new BufferedReader(new InputStreamReader(input));
                String riga = "";
                while(riga = bReader.readLine()) != null){
                    data += riga;
                }
            }
            finally{
                input.close();
            }
            JSONObject testo = (JSONObject) JSONValue.parsewithException(data);
            JSONObject result = (JSONObject) testo.get("result");
            JSONArray resources = (JSONArray) result.get("resources");

            for(Object o : resources){
                if(o instanceof JSONObject){
                    JSONObject jO = (JSONObject) x;
                    URL url1 = new URL((String) jO.get("url"));
                    String formato = (String) jO.get("format");
                    if(formato.equals("XXX")) {
                        FileUtils.copyURLtoFile(url1, TSV);
                    }
                }
            }
        }
        catch(IOException | ParseException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}
