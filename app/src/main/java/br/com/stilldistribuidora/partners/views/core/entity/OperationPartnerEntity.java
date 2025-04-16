package br.com.stilldistribuidora.partners.views.core.entity;

import br.com.stilldistribuidora.partners.views.core.contract.partnersOperationContract;

public class OperationPartnerEntity {
    private String id;
    private String id_parteners;
    private String datetime_start;
    private String datetime_end;
    private String delivery_fragment_id;
    private String route_id;
    private String distance;
    private String qnt_pictures;
    private String status;
    private String sync;
    private String created_at;
    private  String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String latitude;
    private String longitude;






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_parteners() {
        return id_parteners;
    }

    public void setId_parteners(String id_parteners) {
        this.id_parteners = id_parteners;
    }

    public String getDatetime_start() {
        return datetime_start;
    }

    public void setDatetime_start(String datetime_start) {
        this.datetime_start = datetime_start;
    }

    public String getDatetime_end() {
        return datetime_end;
    }

    public void setDatetime_end(String datetime_end) {
        this.datetime_end = datetime_end;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getQnt_pictures() {
        return qnt_pictures;
    }

    public void setQnt_pictures(String qnt_pictures) {
        this.qnt_pictures = qnt_pictures;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
