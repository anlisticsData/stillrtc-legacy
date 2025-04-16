package br.com.stilldistribuidora.partners.Base.navegador;


import java.util.List;

public class ProcessWaypoints {
    private final List<RoutePoints> waypoints;
    private final int EARTH_RADIUS_KM = 6371;
    public ProcessWaypoints(List<RoutePoints> waypoints) {
        this.waypoints = waypoints;
    }
    private double calcularDistance() {
        int size = waypoints.size();
        double distance = 0;
        for (int next = 0; next < size - 1; next++) {
            distance += geoDistanceInKm(
                    waypoints.get(next).getLatitude(), waypoints.get(next).getLatitude(),
                    waypoints.get(next + 1).getLatitude(), waypoints.get(next + 1).getLatitude()
            );
        }
        return distance;
    }
    public double checkDistanceFromRoute() {
        return calcularDistance();
    }
    public double kmDividedByPoints() {
        return (numberOfPointsOnTheRoute() != 0) ? calcularDistance() / numberOfPointsOnTheRoute() : 0;
    }
    public int numberOfPointsOnTheRoute() {
        return this.waypoints.size();
    }
    private double geoDistanceInKm(double firstLatitude,
                                   double firstLongitude, double secondLatitude, double secondLongitude) {
        // Conversão de graus pra radianos das latitudes
        double firstLatToRad = Math.toRadians(firstLatitude);
        double secondLatToRad = Math.toRadians(secondLatitude);

        // Diferença das longitudes
        double deltaLongitudeInRad = Math.toRadians(secondLongitude
                - firstLongitude);
        // Cálcula da distância entre os pontos
        return Math.acos(Math.cos(firstLatToRad) * Math.cos(secondLatToRad)
                * Math.cos(deltaLongitudeInRad) + Math.sin(firstLatToRad)
                * Math.sin(secondLatToRad))
                * EARTH_RADIUS_KM;
    }
}
