package br.com.stilldistribuidora.partners.views.core.entity;

public class PartnersOperationControlStateEntity {

    private int id;
    private String id_parteners;
    private String delivery_fragment_id;
    private String route_id;
    private String time_current;
    private String delivery_state;
    private String created_at;
    private  String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_parteners() {
        return id_parteners;
    }

    public void setId_parteners(String id_parteners) {
        this.id_parteners = id_parteners;
    }

    public String getDelivery_fragment_id() {
        return delivery_fragment_id;
    }

    public void setDelivery_fragment_id(String delivery_fragment_id) {
        this.delivery_fragment_id = delivery_fragment_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getTime_current() {
        return time_current;
    }

    public void setTime_current(String time_current) {
        this.time_current = time_current;
    }

    public String getDelivery_state() {
        return delivery_state;
    }

    public void setDelivery_state(String delivery_state) {
        this.delivery_state = delivery_state;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
