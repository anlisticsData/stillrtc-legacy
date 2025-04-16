package br.com.stilldistribuidora.pco.db.model;

public class DevicesModel {


    private int Key_;
    private String cod_mov;
    private String lat_log;
    private String device;
    private String user;
    private String created_at;
    private String status;

    public DevicesModel(String cod_mov, String lat_log, String device, String user, String created_at, String status) {
        this.cod_mov = cod_mov;
        this.lat_log = lat_log;
        this.device = device;
        this.user = user;
        this.created_at = created_at;
        this.status = status;
    }

    public DevicesModel() {

    }

    public int getKey_() {
        return Key_;
    }

    public void setKey_(int key_) {
        Key_ = key_;
    }

    public String getCod_mov() {
        return cod_mov;
    }

    public void setCod_mov(String cod_mov) {
        this.cod_mov = cod_mov;
    }

    public String getLat_log() {
        return lat_log;
    }

    public void setLat_log(String lat_log) {
        this.lat_log = lat_log;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
