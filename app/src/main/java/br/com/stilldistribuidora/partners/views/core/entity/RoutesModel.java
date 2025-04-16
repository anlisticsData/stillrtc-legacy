package br.com.stilldistribuidora.partners.views.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoutesModel implements Serializable {
    private  long id;
    private  int idrotas;
    private  String created_at;
    private  String routes;

    private List<RoutesCoods> coods = new ArrayList<>();

    public RoutesModel(int idrotas, String created_at) {
        this.idrotas = idrotas;
        this.created_at = created_at;
    }

    public RoutesModel(long itemId, String created_at, int idrotas) {
        this.id=itemId;
        this.idrotas = idrotas;
        this.created_at = created_at;
    }

    public RoutesModel(long itemId, String created_at, int idrotas,String routes) {
        this.id=itemId;
        this.idrotas = idrotas;
        this.created_at = created_at;
        this.routes=routes;
    }


    public void addCoords(RoutesCoods coods){
        this.coods.add(coods);

    }

    public String getRoutes() {
        return routes;
    }

    public int getIdrotas() {
        return idrotas;
    }

    public String getCreated_at() {
        return created_at;
    }

    public List<RoutesCoods> getCoods() {
        return coods;
    }
}
