package com.momoiaco.progetto.servizi;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Filtri {
    private static final List<String> operatori = Arrays.asList("$not", "$in", "$nin", "$and", "$or", "$gt", "$gte", "$lt", "$lte", "$bt");


    private static boolean check(Object value, String operation, Object reference) {
        if (operatori.contains(operation)) {
            if (value instanceof Number) {
                double valueNum = ((Number) value).doubleValue();
                if (reference instanceof Number) {
                    double rifNum = ((Number) reference).doubleValue();
                    switch (operation) {
                        case "$not":
                            return valueNum != rifNum;
                        case "$and":
                            return  ;
                        case "$or":
                            return  ;
                        case "$gt":
                            return valueNum > rifNum;
                        case "$gte":
                            return valueNum >= rifNum;
                        case "$lt":
                            return valueNum < rifNum;
                        case "$lte":
                            return valueNum <= rifNum;
                        default:
                            String erroreOper = "L'operatore: '" + operation + "' risulta inadatto per gli operandi: '" + value + "' , '" + reference + "'";
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, erroreOper);
                    }
                } else if (reference instanceof List) {
                    List rifL = ((List) reference);
                    if (!rifL.isEmpty() && rifL.get(0) instanceof Number) {

                        List<Double> leftReferenceNum = new ArrayList<>();
                        for (Object elem : rifL) {
                            leftReferenceNum.add(((Number) elem).doubleValue());
                        }
                        switch (operation) {
                            case "$in":
                                return leftReferenceNum.contains(valueNum);
                            case "$nin":
                                return !leftReferenceNum.contains(valueNum);
                            case "$bt":
                                double first = leftReferenceNum.get(0);
                                double second = leftReferenceNum.get(1);
                                return valueNum >= first && valueNum <= second;
                            default:
                                String erroreOper = "L'operatore: '" + operation + "' risulta inadatto per gli operandi: '" + value + "' , '" + reference + "'";
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, erroreOper);
                        }
                    } else
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lista vuota o non numerica");
                } else
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il riferimento: '" + reference + "' non è compatibile con il valore: '" + value + "'");
            } else if (value instanceof String || value instanceof Character) {
                if(value instanceof Character)
                    value=String.valueOf(value);
                String valueStr = ((String) value);
                if (reference instanceof String) {
                    String rifStr = ((String) reference);
                    if (operation == "$not") {
                        return  !valueStr.equals(rifStr);
                    }
                            String erroreOper = "L'operatore:'" + operation + "' risulta inadatto per gli operandi: '" + value + "' , '" + reference + "'";
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, erroreOper);
                    }
                } else {
                    if (reference instanceof List) {
                    List rifList = ((List) reference);
                    if (!rifList.isEmpty() && rifList.get(0) instanceof String) {
                           List<String> rifLStr = new ArrayList<>();
                        String valueStr = ((String) value);
                        for (Object elem : rifList) {
                            rifLStr.add((String) elem);
                        }
                        switch (operation) {
                            case "$in":
                                return rifLStr.contains(valueStr);
                            case "$nin":
                                return !rifLStr.contains(valueStr);
                            default:
                                String message = "L'operatore: '" + operation + "' risulta inadatto per gli operandi: '" + value + "' , '" + reference + "'";
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                        }
                    } else
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La lista è vuota o non contiene stringhe");
                } else
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Riferimento:'" + reference + "' non compatibile con il valore'" + value + "'");           } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valore da controllare non valido: '" + value + "'");
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operatore non valido: " + operation);
    }

    public static List<String> getOperatori() {
        return operatori;
    }

}


    public static List<Integer> filtra(List val, String oper, Object rif) {
        List<Integer> filtrati = new ArrayList<>();
        for (int i = 0; i < val.size(); i++) {
            if (check(val.get(i), oper, rif))
                filtrati.add(i);
        }
        return filtrati;
    }
}

