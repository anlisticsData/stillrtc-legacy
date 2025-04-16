package br.com.stilldistribuidora.partners.Models;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public  class Zonas{
    public  String name;
    public  String description;
    public  int aptos;
    public  int casas;
    public  int domTotal;
    public  String content;


    public String toJson() {
        Map<String,String > map = new HashMap<>();
        map.put("name",name);
        map.put("description",description);
        map.put("content",content);
        map.put("aptos",String.valueOf(aptos));
        map.put("casas",String.valueOf(casas));
        map.put("domTotal",String.valueOf(domTotal));
        return new Gson().toJson(map);

    }
}