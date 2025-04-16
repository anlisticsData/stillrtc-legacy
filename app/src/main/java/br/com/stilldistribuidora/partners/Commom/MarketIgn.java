package br.com.stilldistribuidora.partners.Commom;



public class MarketIgn{
    public Double lan,lng;
    public String direcao;

    public int position;

    public MarketIgn(double lat, double lng, String direcao, int id) {
        this.lan = lat;
        this.lng = lng;
        this.direcao = direcao;
        this.position =  id;

    }
}