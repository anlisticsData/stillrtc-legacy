package br.com.stilldistribuidora.partners.resources;

import android.content.Context;


import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;

public class ServerConfiguration {
    private   String imeStill,userLogged;
    private ConfigDataAccess configDataAccess;
    private String url;
    private String token;
    private  String jwt;
    private ApiEndpointInterface API;

    public String getUserLogged() {
        return userLogged;
    }

    public void setUserLogged(String userLogged) {
        this.userLogged = userLogged;
    }

    public ApiEndpointInterface getAPI() {
        return API;
    }

    public ServerConfiguration(Context context) {
        ConfigDataAccess configDataAccess = new ConfigDataAccess(context);
        this.url=((Config) configDataAccess.getById(Resources.RC_API_URL_BASE)).getDataJson();
        API = ApiClient.getClient(this.url.trim()).create(ApiEndpointInterface.class);
        this.jwt=((Config) configDataAccess.getById(Resources.RC_API_TOKEN_TO_BASE)).getDataJson();
        this.token=((Config) configDataAccess.getById(Resources.RC_API_TOKEN_STATIC_TO_BASE)).getDataJson();
        this.imeStill=((Config) configDataAccess.getById(Resources.RC_API_URL_IME_BASE)).getDataJson();
        this.userLogged=((Config)configDataAccess.getById(Resources.RC_USER_LOGGED_TO_BASE)).getDataJson();
        this.configDataAccess=configDataAccess;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
