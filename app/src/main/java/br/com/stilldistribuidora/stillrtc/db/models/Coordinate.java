package br.com.stilldistribuidora.stillrtc.db.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Still Technology and Development Team on 24/06/2017.
 */

public class Coordinate implements Serializable {



    private static final long serialVersionUID = -2161110911377686463L;

    private double lat;
    private double lng;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Coordinate(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
