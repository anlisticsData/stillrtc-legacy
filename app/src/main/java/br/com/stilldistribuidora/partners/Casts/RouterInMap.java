package br.com.stilldistribuidora.partners.Casts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import br.com.stilldistribuidora.partners.Models.RouterMap;
import br.com.stilldistribuidora.partners.Models.Zonas;

public class RouterInMap {
    public void RouterInMap(){

    }


    public List<RouterMap> getInstructions(String routerMap) {
        List<RouterMap> routers = new ArrayList<>();
        try{
            JSONArray jsonArrays = new JSONArray(routerMap);
            int size=jsonArrays.length();
            for(int next=0;next<size;next++){
                RouterMap route = new RouterMap();
                route.id=jsonArrays.getJSONObject(next).getInt("id");
                route.command=jsonArrays.getJSONObject(next).getString("command");
                route.pontos=jsonArrays.getJSONObject(next).getString("points");
                route.fk_routers=jsonArrays.getJSONObject(next).getString("fk_routers");
                routers.add(route);
            }
        }catch (Exception e){
        }
        return routers;
    }

    public  List<Zonas> getAreasStill(String zonasJson) {
        List<Zonas> zonas = new ArrayList<>();
        try{
            JSONArray jsonArrays = new JSONArray(zonasJson);
            int size=jsonArrays.length();
            for(int next=0;next<size;next++){
                JSONObject row = jsonArrays.getJSONObject(next);
                Zonas zona = new Zonas();
                zona.name=row.getString("name");
                zona.casas=row.getInt("casas");
                zona.domTotal=row.getInt("domTotal");
                zona.aptos=row.getInt("aptos");
                zona.aptos=row.getInt("aptos");
                zona.content=row.getString("content");
                zonas.add(zona);
            }
        }catch (Exception e){
        }
        return zonas;
    }
}
