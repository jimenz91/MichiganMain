package com.magally.michiganmain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Magally on 13-09-2015.
 */
public class Materias {
    public static Map<String,String> MATERIAS = null;
    static{
        Map<String, String> map = new HashMap<String, String>();
        map.put("2","Física I");
        map.put("4", "Cálculo I");
        map.put("5","Calculo II");

        MATERIAS = Collections.unmodifiableMap(map);
    }
}
