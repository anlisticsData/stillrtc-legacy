package br.com.stilldistribuidora.partners.Casts;

import android.content.Intent;

import java.util.List;

import br.com.stilldistribuidora.partners.Models.Router;
import br.com.stilldistribuidora.partners.Models.RouterInstruction;
import br.com.stilldistribuidora.partners.Models.Zonas;
import br.com.stilldistribuidora.partners.Services.SynchronizationOfOperations;

public class HelperParser {


    public String getRouterJson(int routerId,String routerName){
        return new Router(routerId,routerName).toJson();

    }

    public StringBuffer getToJsonZonas(List<Zonas> zonasJson) {
        StringBuffer zonas=new StringBuffer();
        zonas.append("[");
        for(Zonas zn : zonasJson){
            zonas.append(zn.toJson());
            zonas.append(",");
        }
        zonas.append("]");
        return zonas;
    }


    public StringBuffer getToJsonRouter(List<RouterInstruction> router_instruction) {
        StringBuffer routers=new StringBuffer();
        routers.append("[");
        for(RouterInstruction rti : router_instruction){
            routers.append(rti.toJson());
            routers.append(",");
        }
        routers.append("]");
        return routers;

    }



}
