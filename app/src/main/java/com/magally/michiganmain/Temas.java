package com.magally.michiganmain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Magally on 13-09-2015.
 */
public class Temas {
    public static Map<String, String> TEMAS = null;
    static{
        Map<String, String> map = new HashMap<String, String>();
        map.put("3","Derivación");
        map.put("4","Optimización");
        map.put("5","Límites");
        map.put("6","LAnzamiento Horizontal");
        map.put("7","Movimiento Circular");
        map.put("8","Cónicas");

        TEMAS = Collections.unmodifiableMap(map);
    }
}
