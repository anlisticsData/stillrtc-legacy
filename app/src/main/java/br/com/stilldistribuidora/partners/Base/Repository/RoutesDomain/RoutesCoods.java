package br.com.stilldistribuidora.partners.Base.Repository.RoutesDomain;


import java.io.Serializable;

/*{\"lat\":-23.4483727307472,\"lng\":-46.677749204321294,\"direction\":\"F\"}
* */
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
