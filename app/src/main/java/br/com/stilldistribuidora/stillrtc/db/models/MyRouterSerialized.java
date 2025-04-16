package br.com.stilldistribuidora.stillrtc.db.models;

import java.io.Serializable;

public class MyRouterSerialized implements Serializable {
    private String router_id;
    private String operation;
    private String created;
    private String store;
    private String storeName;
    private String pointStart;
    private String distancekm;
    private String distanceM;
    private String status;
    private String instruction_router;
    private  String deviceId;

    public  String zonas;



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getInstruction_router() {
        return instruction_router;
    }

    public void setInstruction_router(String instruction_router) {
        this.instruction_router = instruction_router;
    }

    public String getRouter_id() {
        return router_id;
    }

    public void setRouter_id(String router_id) {
        this.router_id = router_id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPointStart() {
        return pointStart;
    }

    public void setPointStart(String pointStart) {
        this.pointStart = pointStart;
    }

    public String getDistancekm() {
        return distancekm;
    }

    public void setDistancekm(String distancekm) {
        this.distancekm = distancekm;
    }

    public String getDistanceM() {
        return distanceM;
    }

    public void setDistanceM(String distanceM) {
        this.distanceM = distanceM;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
