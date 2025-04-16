package br.com.stilldistribuidora.partners.Base.Repository.RoutesDomain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*"idrotas":"10",
"created_at":"2021-08-23 16:09:42",
"rotas"*/
public class RoutesModel implements Serializable {
    private long id;
    private int idrotas;
    private String created_at;
    private String routes;
    private String keis;
    private String deviceId;
    private List<RoutesCoods> coods = new ArrayList<>();
    public RoutesModel(int idrotas, String created_at) {
        this.idrotas = idrotas;
        this.created_at = created_at;
    }
    public RoutesModel(long itemId, String created_at, int idrotas) {
        this.id = itemId;
        this.idrotas = idrotas;
        this.created_at = created_at;
    }

    public RoutesModel(long itemId, String created_at, int idrotas, String routes) {
        this.id = itemId;
        this.idrotas = idrotas;
        this.created_at = created_at;
        this.routes = routes;
    }


    public RoutesModel(long itemId, String created_at, int idrotas, String routes, String keys) {
        this.id = itemId;
        this.idrotas = idrotas;
        this.created_at = created_at;
        this.routes = routes;
        this.keis = keys;
    }



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getKeis() {
        return keis;
    }

    public void setKeis(String keis) {
        this.keis = keis;
    }

    public void addCoords(RoutesCoods coods) {
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
