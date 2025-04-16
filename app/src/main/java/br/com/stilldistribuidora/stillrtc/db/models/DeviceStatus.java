package br.com.stilldistribuidora.stillrtc.db.models;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ack Lay (Cleidimar Viana) on 3/16/2017.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

public class DeviceStatus implements Serializable {

    private static final long serialVersionUID = -2161110911377686463L;

    @SerializedName("id")
    private int id;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("accuracy")
    private float accuracy;

    @SerializedName("altitude")
    private int altitude;

    @SerializedName("batery")
    private float batery;

    @SerializedName("ip")
    private String ip;

    @SerializedName("device_identifier")
    private String device_identifier;

    @SerializedName("device_fragment_id")
    private int device_fragment_id;

    private int sync;
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public float getBatery() {
        return batery;
    }

    public void setBatery(float batery) {
        this.batery = batery;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceIdentifier() {
        return device_identifier;
    }



    public void setDeviceIdentifier(String device_identifier) {
        this.device_identifier = device_identifier;
    }

    public int getDeviceFragmentId() {
        return device_fragment_id;
    }

    public void setDeviceFragmentId(int device_fragment_id) {
        this.device_fragment_id = device_fragment_id;
    }

    public static class Result implements Serializable {

        private static final long serialVersionUID = -2643991617962935718L;

        @SerializedName("users")
        public ArrayList<DeviceStatus> ar;
    }
}