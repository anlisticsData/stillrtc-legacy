package br.com.stilldistribuidora.partners.Models;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class RouterInstruction {
    public  String id;
    public  String fk_routers;
    public  String points;
    public  String command;


    public String toJson() {
        Map<String,String > map = new HashMap<>();
        map.put("id",id);
        map.put("fk_routers",fk_routers);
        map.put("points",points);
        map.put("command",command);

        return new Gson().toJson(map);

    }
}

