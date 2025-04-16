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

public class User implements Serializable {

    private static final long serialVersionUID = -2161110911377686463L;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("apiKey")
    private String apiKey;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("name_manager")
    private String name_manager;

    @SerializedName("level")
    private int level;

    @SerializedName("client_id")
    private int clientId;


    @SerializedName("jwt")
    private String jwt;

    public String getJwt() {
        return jwt;
    }


    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getName_manager() {
        return name_manager;
    }

    public void setName_manager(String name_manager) {
        this.name_manager = name_manager;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static class Result implements Serializable {

        private static final long serialVersionUID = -2643991617962935718L;

        @SerializedName("users")
        public ArrayList<User> ar;
    }
}