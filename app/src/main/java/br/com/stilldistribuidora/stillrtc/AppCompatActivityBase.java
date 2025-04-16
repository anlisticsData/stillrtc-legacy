package br.com.stilldistribuidora.stillrtc;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;

public class AppCompatActivityBase extends AppCompatActivity {
    private ConfigDataAccess configDataAccess ;
    private Config endPointAccess,endPointAccessToken,endPointAccessTokenJwt,deviceUuid;

    public ConfigDataAccess getConfigDataAccess() {
        return configDataAccess;
    }

    public Config getEndPointAccess() {
        return endPointAccess;
    }

    public Config getEndPointAccessToken() {
        return endPointAccessToken;
    }

    public Config getEndPointAccessTokenJwt() {
        return endPointAccessTokenJwt;
    }

    public Config getDeviceUuid() {
        return deviceUuid;
    }

    public  AppCompatActivityBase(Context ctx){
        configDataAccess =new ConfigDataAccess(ctx);
        configDataAccess.createDataBase();
        endPointAccess = (Config) configDataAccess.getById(String.format("%s='%s'", Constants.CONFIG_DESCRICAO, Constants.API_URL));
        endPointAccessToken = (Config) configDataAccess.getById(String.format("%s='%s'", Constants.CONFIG_DESCRICAO, Constants.API_TOKEN));
        endPointAccessTokenJwt= (Config) configDataAccess.getById(String.format("%s='%s'", Constants.CONFIG_DESCRICAO, Constants.API_TOKEN_JWT));
        deviceUuid= (Config) configDataAccess.getById(String.format("%s='%s'", Constants.CONFIG_DESCRICAO, Constants.APP_IME_STILL));

    }



}
