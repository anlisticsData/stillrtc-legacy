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

public class Picture implements Serializable {

    private static final long serialVersionUID = -2161110911377686463L;

    private int id;

    @SerializedName("uri")
    private String uri;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    private int picId;
    private int type;

    @SerializedName("device_id")
    private int deviceId;
    @SerializedName("delivery_id")
    private int deliveryId;
    private int sync;
    @SerializedName("created_at")
    private String created_at;
    private String updated_at;

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

     public void setPicId(int picId) {
        this.picId = picId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public int getPicId() {
        return picId;
    }

    public void setPicUid(int pic_uid) {
        this.picId = pic_uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public static class Result implements Serializable {

        private static final long serialVersionUID = -2643991617962935718L;

        @SerializedName("pictures")
        public ArrayList<Picture> ar;
    }
}