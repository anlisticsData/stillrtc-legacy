package br.com.stilldistribuidora.partners.Base.Repository.RouterMovimentDomain;

import java.io.Serializable;

public class RouterMovimentModel implements Serializable {
    private String id;
    private String create_at;
    private String idKeys;
    private String startPoints;
    private String sync;
    private  String action;
    private String deviceID;

    public RouterMovimentModel(long itemId, String created_at, String keys, String startPoint, String sync, String action, String deviceId) {
        this.id=String.valueOf(itemId);
        this.create_at=created_at;
        this.idKeys=keys;
        this.startPoints=startPoint;
        this.sync=sync;
        this.action=action;
        this.deviceID=deviceId;

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

    public RouterMovimentModel(long itemId, String created_at, String keys, String startPoint, String sync, String action) {

        this.id=String.valueOf(itemId);
        this.create_at=created_at;
        this.idKeys=keys;
        this.startPoints=startPoint;
        this.sync=sync;
        this.action=action;



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
