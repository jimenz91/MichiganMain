package com.magally.michiganmain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Magally on 12-09-2015.
 */
public class Carreras {
    public static Map<String,String> CARRERAS = null;
    static{
        Map<String, String> map = new HashMap<String, String>();
        map.put("1","Telecomunicaciones");
        map.put("2","Informatica");
        map.put("3","Civil");
        map.put("4","Industrial");
        map.put("5","Administración");
        map.put("6","Contaduría");
        map.put("7","Economía");
        map.put("8","Comunicación Social");
        map.put("9","Psicología");

        CARRERAS = Collections.unmodifiableMap(map);

    }
}
