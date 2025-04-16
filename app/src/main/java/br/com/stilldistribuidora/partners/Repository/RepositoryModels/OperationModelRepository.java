package br.com.stilldistribuidora.partners.Repository.RepositoryModels;

import java.io.Serializable;

import br.com.stilldistribuidora.partners.Casts.CabinAudio;
import br.com.stilldistribuidora.partners.Repository.Contracts.ITEntity;


class Entity implements Serializable{


}

public class OperationModelRepository extends Entity implements ITEntity {

    private Integer id;
    private Integer operationID;
    private Integer routerID;
    private Integer storeID;
    private Integer parternID;
    private Integer state;
    private String distanceKm;
    private String distanceMm;
    private String zonasJson;
    private String routerJson;
    private String routerMap;
    private String storename;
    private String pontStart;
    private String createdAt;
    private String updateAt;
    private String justification;
    private String acceptOperation;
    private int typeOperation;

    public CabinAudio cabinAudio;



    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    private String deviceID;




    public String getAcceptOperation() {
        return acceptOperation;
    }

    public void setAcceptOperation(String acceptOperation) {
        this.acceptOperation = acceptOperation;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOperationID() {
        return operationID;
    }

    public void setOperationID(Integer operationID) {
        this.operationID = operationID;
    }

    public Integer getRouterID() {
        return routerID;
    }

    public void setRouterID(Integer routerID) {
        this.routerID = routerID;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public Integer getParternID() {
        return parternID;
    }

    public void setParternID(Integer parternID) {
        this.parternID = parternID;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(String distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getDistanceMm() {
        return distanceMm;
    }

    public void setDistanceMm(String distanceMm) {
        this.distanceMm = distanceMm;
    }

    public String getZonasJson() {
        return zonasJson;
    }

    public void setZonasJson(String zonasJson) {
        this.zonasJson = zonasJson;
    }

    public String getRouterJson() {
        return routerJson;
    }

    public void setRouterJson(String routerJson) {
        this.routerJson = routerJson;
    }

    public String getRouterMap() {
        return routerMap;
    }

    public void setRouterMap(String routerMap) {
        this.routerMap = routerMap;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getPontStart() {
        return pontStart;
    }

    public void setPontStart(String pontStart) {
        this.pontStart = pontStart;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public   int type(){
        return this.typeOperation;
    }
    public void setTypeOperation(int typeOperation) {
        this.typeOperation = typeOperation;
    }


}
