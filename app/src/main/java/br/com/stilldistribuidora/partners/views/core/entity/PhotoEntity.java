package br.com.stilldistribuidora.partners.views.core.entity;

public class PhotoEntity {
    private int id;
    private String uri;
    private String latitude;
    private String longitude;
    private String pic_uid;
    private String type;
    private String delivery_id;
    private String sync;
    private String created_at;
    private String update_at;
    private String partners_id;
    private  String deviceId;



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getPath_serve_url() {
        return path_serve_url;
    }

    public void setPath_serve_url(String path_serve_url) {
        this.path_serve_url = path_serve_url;
    }

    private String path_serve_url;





    public String getPartners_id() {
        return partners_id;
    }

    public void setPartners_id(String partners_id) {
        this.partners_id = partners_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public String getPic_uid() {
        return pic_uid;
    }

    public void setPic_uid(String pic_uid) {
        this.pic_uid = pic_uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
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

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }
}
