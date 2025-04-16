package br.com.stilldistribuidora.partners.Base;

public class Locations {
    private final String direcao;
    private final double lat;
    private final double lng;
    private int id;


    public Locations(int id,String direcao, double lat, double lng) {
        this.id=id;
        this.direcao = direcao;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirecao() {
        return direcao;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
