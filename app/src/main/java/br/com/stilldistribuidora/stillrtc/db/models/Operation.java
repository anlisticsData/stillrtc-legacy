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

public class Operation implements Serializable {

    private static final long serialVersionUID = -2161110911377686463L;


    private int id;

    @SerializedName("delivery_id")
    private int deliveryId;

    @SerializedName("op_name")
    private String areaName;

    @SerializedName("op_id")
    private int areaId;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    @SerializedName("gr_name")
    private String group;

    @SerializedName("st_name")
    private String storeName;

    @SerializedName("st_id")
    private int storeId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("client_id")
    private int clientId;

    @SerializedName("client_name")
    private String clientName;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private String datetimeStart;
    private String datetimeEnd;
    private int distance;
    private int status;
    private int qntPictures;
    private int routeId;
    private int sync;

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    private int deviceId;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getQntPictures() {
        return qntPictures;
    }

    public void setQntPictures(int qntPictures) {
        this.qntPictures = qntPictures;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getDatetimeStart() {
        return datetimeStart;
    }

    public void setDatetimeStart(String datetimeStart) {
        this.datetimeStart = datetimeStart;
    }

    public String getDatetimeEnd() {
        return datetimeEnd;
    }

    public void setDatetimeEnd(String datetimeEnd) {
        this.datetimeEnd = datetimeEnd;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getDeliveryFragmentId() {
        return deliveryId;
    }

    public void setDeliveryFragmentId(int delivery_fragment_id) {
        this.deliveryId = delivery_fragment_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public static class Result implements Serializable {

        private static final long serialVersionUID = -2643991617962935718L;

        @SerializedName("deliveries")
        public ArrayList<Operation> ar;

    }

    @SerializedName("zones")
    public ArrayList<KMLS> ids;


    public class KMLS implements Serializable {

        private static final long serialVersionUID = -2161110911377686463L;

        @SerializedName("id")
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }


}