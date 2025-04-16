package br.com.stilldistribuidora.stillrtc.db.models;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ack Lay (Cleidimar Viana) on 3/16/2017.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

public class Device implements Serializable {

    private static final long serialVersionUID = -2161110911377686463L;

    @SerializedName("id")
    private int id;

    @SerializedName("identifier")
    private String identifier;

    @SerializedName("operator")
    private String operator;

    @SerializedName("api_key")
    private String api_key;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("jwt")
    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getApi_key() {
        return api_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getApiKey() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static class Result implements Serializable {

        private static final long serialVersionUID = -2643991617962935718L;

        @SerializedName("devices")
        public ArrayList<Device> ar;
    }
}