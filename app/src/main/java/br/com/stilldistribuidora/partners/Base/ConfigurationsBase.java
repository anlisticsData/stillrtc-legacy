package br.com.stilldistribuidora.partners.Base;

import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;

public class ConfigurationsBase {
    protected   ConfigDataAccess configDataAccessDao;
    public ConfigurationsBase( ConfigDataAccess configDataAccessDao){
        this.configDataAccessDao=configDataAccessDao;
    }
}
