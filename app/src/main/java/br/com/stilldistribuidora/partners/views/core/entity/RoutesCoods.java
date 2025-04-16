package br.com.stilldistribuidora.partners.views.core.entity;

import java.io.Serializable;

public class RoutesCoods implements Serializable {

    private  String latitude,longuitude,directions;

    public RoutesCoods(String latitude, String longuitude, String directions) {
        this.latitude = latitude;
        this.longuitude = longuitude;
        this.directions = directions;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLonguitude() {
        return longuitude;
    }

    public String getDirections() {
        return directions;
    }
}
