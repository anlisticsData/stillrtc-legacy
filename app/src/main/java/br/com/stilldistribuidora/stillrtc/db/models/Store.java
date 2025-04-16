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

public class Store implements Serializable {

    private static final long serialVersionUID = -2161110911377686463L;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("name_manager")
    private String nameManager;

    @SerializedName("email")
    private String email;

    @SerializedName("client_id")
    private String clientID;

    @SerializedName("phone")
    private String phone;

    @SerializedName("status")
    private int status;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameManager() {
        return nameManager;
    }

    public void setNameManager(String nameManager) {
        this.nameManager = nameManager;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static class Result implements Serializable {

        private static final long serialVersionUID = -2643991617962935718L;

        @SerializedName("stores")
        public ArrayList<Store> ar;
    }
}