package br.com.stilldistribuidora.partners.Casts;

public class CoordinateDto {

    public double lat=0,lng=0;

    public CoordinateDto(double lat,double lng){
        this.lat =  lat;
        this.lng = lng;
    }
    public  String larLngToString(){
        return String.format("%s,%s",this.lat,this.lng);
    }

    public  String larLngToString6(){
        return String.format("%s,%s",String.valueOf(this.lat).substring(0,10),String.valueOf(this.lng).substring(0,8));
    }

}
