package br.com.stilldistribuidora.stillrtc.db.models;


import java.io.Serializable;

public class AvailableOperation implements Serializable {
    private String uuid;
    private int router_id;
    private String router_name;

    public String getRouter_id_instructions() {
        return router_id_instructions;
    }

    public void setRouter_id_instructions(String router_id_instructions) {
        this.router_id_instructions = router_id_instructions;
    }

    private String irPartrner;
    private String idUserSelectonOperation;
    private String operation;
    private String created;
    private String store;
    private String storeName;
    private String pointStart;
    private String distancekm;
    private String distanceM;
    private String state;
    private String router_id_instructions;
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRouterPoints() {
        return routerPoints;
    }

    public void setRouterPoints(String routerPoints) {
        this.routerPoints = routerPoints;
    }

    private String routerPoints;





    public String getIrPartrner() {
        return irPartrner;
    }

    public void setIrPartrner(String irPartrner) {
        this.irPartrner = irPartrner;
    }

    public String getIdUserSelectonOperation() {
        return idUserSelectonOperation;
    }

    public void setIdUserSelectonOperation(String idUserSelectonOperation) {
        this.idUserSelectonOperation = idUserSelectonOperation;
    }

    public String getStartAddressInit() {
        return startAddressInit;
    }

    public void setStartAddressInit(String startAddressInit) {
        this.startAddressInit = startAddressInit;
    }

    private String startAddressInit;




    public int getRouter_id() {
        return router_id;
    }

    public void setRouter_id(int router_id) {
        this.router_id = router_id;
    }

    public String getRouter_name() {
        return router_name;
    }

    public void setRouter_name(String router_name) {
        this.router_name = router_name;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
