package br.com.stilldistribuidora.partners.Models;


import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public  class Router{
    public  int id;
    public  String name;
    public String observation;
    public String createAt;

    public Router(int routerId, String routerName) {
        this.id=routerId;
        this.name=routerName;
    }


    public String toJson(){
        Map<String,String> map =new HashMap<>();
        map.put("id",String.valueOf(this.id));
        map.put("name",this.name);

        return new Gson().toJson(map);
    }
}

