package br.com.stilldistribuidora.stillrtc.db.models;

import java.io.Serializable;

public class PartnerOperationAcception  implements Serializable {

    private String uuid;
    private String routerId;
    private String operationId;
    private String partnerId;
    private String storeId;
    private String createdAt;
    private String storeName;
    private String pontStart;
    private String address;
    private String distanceKm;
    private String distanceMeter;
    private String stateOperation;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPontStart() {
        return pontStart;
    }

    public void setPontStart(String pontStart) {
        this.pontStart = pontStart;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(String distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getDistanceMeter() {
        return distanceMeter;
    }

    public void setDistanceMeter(String distanceMeter) {
        this.distanceMeter = distanceMeter;
    }

    public String getStateOperation() {
        return stateOperation;
    }

    public void setStateOperation(String stateOperation) {
        this.stateOperation = stateOperation;
    }
}
