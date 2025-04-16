package br.com.stilldistribuidora.partners.Base.navegador;


public class RoutePoints {
    private   Float latitude,longitude;

    public RoutePoints(Float latitude, Float longitude) {
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }
}
