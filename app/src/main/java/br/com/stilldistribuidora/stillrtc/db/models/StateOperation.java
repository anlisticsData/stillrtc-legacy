package br.com.stilldistribuidora.stillrtc.db.models;

public class StateOperation {

    private String operation;
    private String router;
    private String state;
    private String deviceId;


    public StateOperation(String operation, String router, String state, String deviceId) {
        this.operation=operation;
        this.router=router;
        this.state=state;
        this.deviceId=deviceId;

    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
