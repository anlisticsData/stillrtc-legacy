package br.com.stilldistribuidora.partners.Models;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class PointStartAddress {
    public int  id;
    public String   lat_long;
    public String   endereco;
    public String   create_at;
    public String   update_at;
    public String   atualizar;
    public String   local;


    public String toJson(){
            Map<String,String> map = new HashMap<>();
            map.put("id",String.valueOf(id));
            map.put("lat_long",lat_long);
            map.put("endereco",endereco);
            map.put("create_at",create_at);
            map.put("update_at",update_at);
            map.put("atualizar",atualizar);
            map.put("local",local);
        return new Gson().toJson(map);
    }
}
