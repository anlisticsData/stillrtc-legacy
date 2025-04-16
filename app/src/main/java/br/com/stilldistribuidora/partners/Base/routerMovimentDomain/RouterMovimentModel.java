package br.com.stilldistribuidora.partners.Base.routerMovimentDomain;

import java.io.Serializable;

public class RouterMovimentModel implements Serializable {
    private String id;
    private String create_at;
    private String idKeys;
    private String startPoints;
    private String sync;
    private  String action;
    private String deviceID;
    private String deliveryId;

    public int getLap() {
        return lap;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    private int lap;


    public RouterMovimentModel(long itemId, String created_at, String keys, String startPoint, String sync, String action, String deviceId,String deliveryId) {
        this.id=String.valueOf(itemId);
        this.create_at=created_at;
        this.idKeys=keys;
        this.startPoints=startPoint;
        this.sync=sync;
        this.action=action;
        this.deviceID=deviceId;
        this.deliveryId=deliveryId;
    }


    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public RouterMovimentModel() {

    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getIdKeys() {
        return idKeys;
    }

    public void setIdKeys(String idKeys) {
        this.idKeys = idKeys;
    }

    public String getStartPoints() {
        return startPoints;
    }

    public void setStartPoints(String startPoints) {
        this.startPoints = startPoints;
    }


}
