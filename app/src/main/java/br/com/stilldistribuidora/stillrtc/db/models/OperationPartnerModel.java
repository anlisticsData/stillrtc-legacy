package br.com.stilldistribuidora.stillrtc.db.models;


import java.io.Serializable;

import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.OperationPartner;

public class OperationPartnerModel implements Serializable {
    private int router_id;
    private String router_name;
    private String router_id_instructions;

    private String operation;
    private String created;
    private String store;
    private String storeName;
    private String pointStart;
    private String distancekm;
    private String distanceM;

    private int state;

    public String getRouter_id_instructions() {
        return router_id_instructions;
    }

    public void setRouter_id_instructions(String router_id_instructions) {
        this.router_id_instructions = router_id_instructions;
    }

    public String getStartAddressInit() {
        return startAddressInit;
    }

    public void setStartAddressInit(String startAddressInit) {
        this.startAddressInit = startAddressInit;
    }

    private String startAddressInit;



    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

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



    public static String[] columns = new String[]{
            Constants.PARTNER_OPERATION_UUID,
            Constants.PARTNER_OPERATION_ROUTER_ID,
            Constants.PARTNER_OPERATION_ROUTER_INSTRUCTIONS,
            Constants.PARTNER_OPERATION_ID,
            Constants.PARTNER_OPERATION_STORE_ID,
            Constants.PARTNER_OPERATION_UUID_USER_SELECTED,
            Constants.PARTNER_OPERATION_UUID_PARTNER,
            Constants.PARTNER_OPERATION_CREATED_AT,
            Constants.PARTNER_OPERATION_STORE_NAME,
            Constants.PARTNER_OPERATION_PONT_START,
            Constants.PARTNER_OPERATION_ADDRESS,
            Constants.PARTNER_OPERATION_DISTANCE_KM,
            Constants.PARTNER_OPERATION_DISTANCE_METERS,
            Constants.PARTNER_OPERATION_STATE_OPERATION,
            Constants.PARTNER_OPERATION_DEVICE_SELECIONADO
    };


    public static String script_db_tbL_operation_partern = " CREATE TABLE IF NOT EXISTS  " + OperationPartner.class.getSimpleName() + "("
            + " " + Constants.PARTNER_OPERATION_UUID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
            + " " + Constants.PARTNER_OPERATION_ROUTER_ID + " INTEGER NOT NULL, "
            + " " + Constants.PARTNER_OPERATION_ROUTER_INSTRUCTIONS + " TEXT, "
            + " " + Constants.PARTNER_OPERATION_ID + " INTEGER NOT NULL, "
            + " " + Constants.PARTNER_OPERATION_UUID_USER_SELECTED + " INTEGER , "
            + " " + Constants.PARTNER_OPERATION_UUID_PARTNER + " INTEGER NOT NULL, "
            + " " + Constants.PARTNER_OPERATION_STORE_ID + " INTEGER NOT NULL, "
            + " " + Constants.PARTNER_OPERATION_CREATED_AT + " TEXT, "
            + " " + Constants.PARTNER_OPERATION_STORE_NAME + " TEXT, "
            + " " + Constants.PARTNER_OPERATION_PONT_START + " TEXT, "
            + " " + Constants.PARTNER_OPERATION_ADDRESS + " TEXT, "
            + " " + Constants.PARTNER_OPERATION_DISTANCE_KM + " TEXT, "
            + " " + Constants.PARTNER_OPERATION_DISTANCE_METERS + " TEXT, "
            + " " + Constants.PARTNER_OPERATION_STATE_OPERATION + " INTEGER,"
            + "  "+ Constants.PARTNER_OPERATION_DEVICE_SELECIONADO + " TEXT) ";






}
