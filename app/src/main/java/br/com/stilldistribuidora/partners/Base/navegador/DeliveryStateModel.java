package br.com.stilldistribuidora.partners.Base.navegador;


public class DeliveryStateModel {
    private long id;
    private String uuidcomposta,latlng,batery;
    private  String deliveryid,ip,deviceid;
    private  int routerid,storeid,partnerid,nuvem;
    private int laps;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuidcomposta() {
        return uuidcomposta;
    }

    public void setUuidcomposta(String uuidcomposta) {
        this.uuidcomposta = uuidcomposta;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getBatery() {
        return batery;
    }

    public void setBatery(String batery) {
        this.batery = batery;
    }

    public String getDeliveryid() {
        return deliveryid;
    }

    public void setDeliveryid(String deliveryid) {
        this.deliveryid = deliveryid;
    }

    public int getRouterid() {
        return routerid;
    }

    public void setRouterid(int routerid) {
        this.routerid = routerid;
    }

    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }

    public int getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(int partnerid) {
        this.partnerid = partnerid;
    }

    public int getNuvem() {
        return nuvem;
    }

    public void setNuvem(int nuvem) {
        this.nuvem = nuvem;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public void setLap(int laps) {
        this.laps = laps;
    }

    public int getLaps() {
        return this.laps;
    }

    public void setLaps(int laps) {
        this.laps = laps;
    }
}
