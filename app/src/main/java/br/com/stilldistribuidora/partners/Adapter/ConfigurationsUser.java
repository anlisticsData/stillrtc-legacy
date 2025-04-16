package br.com.stilldistribuidora.partners.Adapter;

import br.com.stilldistribuidora.partners.Base.ConfigurationsBase;
import br.com.stilldistribuidora.partners.resources.Resources;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;

public class ConfigurationsUser extends ConfigurationsBase {
    private   String imeStill,userLogged;
    private ConfigDataAccess configDataAccess;
    private String url;
    private String token;
    private  String jwt;
    private String nivel;
    private double stillCoins;

    public String getImeStill() {
        return imeStill;
    }

    public String getUserLogged() {
        return userLogged;
    }

    public ConfigDataAccess getConfigDataAccess() {
        return configDataAccess;
    }

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

    public String getJwt() {
        return jwt;
    }

    public String getNivel() {
        return nivel;
    }

    public double getStillCoins() {

        return  this.getStillCoin();
    }

    public ConfigurationsUser(ConfigDataAccess configDataAccessDaoContext) {
        super(configDataAccessDaoContext);
        this.jwt=((Config) configDataAccessDao.getById(Resources.RC_API_TOKEN_TO_BASE)).getDataJson();
        this.token=((Config) configDataAccessDao.getById(Resources.RC_API_TOKEN_STATIC_TO_BASE)).getDataJson();
        this.imeStill=((Config) configDataAccessDao.getById(Resources.RC_API_URL_IME_BASE)).getDataJson();
        this.userLogged=((Config)configDataAccessDao.getById(Resources.RC_USER_LOGGED_TO_BASE)).getDataJson();
        this.stillCoins=this.getStillCoin();
    }

    private double getStillCoin() {
        return  Double.valueOf(((Config)configDataAccessDao.getById(Resources.RC_USER_LOGGED_STILL_CONIS)).getDataJson());
    }

    public long updateStillCoins(double stillCoins){
        Config configurationStillCoin = (Config) configDataAccessDao.getById(Resources.RC_USER_LOGGED_STILL_CONIS);
        configurationStillCoin.setDataJson(String.valueOf(stillCoins));
        long update = configDataAccessDao.update(configurationStillCoin, Constants.API_USER_LOGGED_STILL_COINS);

        return update;
    }



    public void updateOperationStat(){


    }







}
