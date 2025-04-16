package br.com.stilldistribuidora.partners.Base;

public class Markers {
    private final String lat;
    private final String lng;
    private final String name;

    public Markers(String lat, String lng, String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }
    
}
