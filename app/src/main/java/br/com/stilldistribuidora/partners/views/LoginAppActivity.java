package br.com.stilldistribuidora.partners.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.com.stilldistribuidora.Libs.Libs;
import br.com.stilldistribuidora.common.views.NetworkActivity;
import br.com.stilldistribuidora.partners.Base.LocationInforUserDialog;
import br.com.stilldistribuidora.partners.Base.OnResponse;
import br.com.stilldistribuidora.partners.Casts.Niveis;
import br.com.stilldistribuidora.partners.Repository.EntitySqlite.AppOpeationEntitySqlite;
import br.com.stilldistribuidora.partners.Repository.EntitySqlite.AudioEntitySqlite;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.business.DeviceStatusBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.OperationBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.PictureBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.PrefsBusiness;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.ui.activities.InitAppActivity;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAppActivity extends AppCompatActivity {
    private static Components ContextFrame;
    private ApiConfig apiConfig;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_app);
        ContextFrame = new Components(this);
        apiConfig = new ApiConfig(ContextFrame.self);
        Intent it = getIntent();

        this.initEvents();
        try{


            LocationInforUserDialog locationPermission = new LocationInforUserDialog(LoginAppActivity.this, new OnResponse() {
                @Override
                public void OnResponseType(String type, Object object) {
                    Intent intent  = new Intent(LoginAppActivity.this, PrivacyPolicyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);


                }
            });

           if(!it.hasExtra("agree") ){
               locationPermission.show();
           }
        this.createAllDatabase();
        if (apiConfig.Api.getEndPointAccess() == null ||
                apiConfig.Api.getEndPointAccess().getDataJson() == null ||
                apiConfig.Api.getEndPointAccess().getDataJson().isEmpty()) {
            Intent intent = new Intent(ContextFrame.self, InitAppActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        ContextFrame.configUSERActived = new Config();
        ContextFrame.configModel = new ConfigDataAccess(ContextFrame.self);
        Config userActivySave = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED_SAVE));
        if (userActivySave == null) {
            ContextFrame.configUSERActived.setDescricao(Constants.API_USER_LOGGED_SAVE);
            ContextFrame.configUSERActived.setDataJson("");
            ContextFrame.configModel.insert(ContextFrame.configUSERActived);
            userActivySave = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED_SAVE));
        }
        ContextFrame.userActivySaveStillCoins = new Config();
        Config userActivySaveStillCoins = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED_STILL_COINS));
        if (userActivySaveStillCoins == null) {
            ContextFrame.userActivySaveStillCoins.setDescricao(Constants.API_USER_LOGGED_STILL_COINS);
            ContextFrame.userActivySaveStillCoins.setDataJson("");
            ContextFrame.configModel.insert(ContextFrame.userActivySaveStillCoins);
            userActivySaveStillCoins = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED_STILL_COINS));
        }



            Config userdevice = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE));
            if (userdevice == null) {
                ContextFrame.userActivyDevice = new Config();
                ContextFrame.userActivyDevice.setDescricao(Constants.API_USER_DEVICE);
                ContextFrame.userActivyDevice.setDataJson("");
                ContextFrame.configModel.insert(ContextFrame.userActivyDevice);
                ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE));
            }

            Config userdevicePhoto = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE_SYNC_PHOTO));
            if (userdevicePhoto == null) {
                ContextFrame.userdevicePhoto = new Config();
                ContextFrame.userdevicePhoto.setDescricao(Constants.API_USER_DEVICE_SYNC_PHOTO);
                ContextFrame.userdevicePhoto.setDataJson("0");
                ContextFrame.configModel.insert(ContextFrame.userdevicePhoto);
                ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE_SYNC_PHOTO));
            }else{
                userdevicePhoto.setDataJson("0");
                ContextFrame.configModel.update(userdevicePhoto,String.valueOf(userActivySave.getId()));
            }




        String userSaved = userActivySave.getDataJson();
        if (userSaved.length() > 0) {
            String[] loginPassword = userSaved.split("\\|");
            if (loginPassword.length > 0) {
                ContextFrame.txtPassword.setText(loginPassword[1]);
                ContextFrame.txtLogin.setText(loginPassword[0]);
                ContextFrame.keepConnected.setChecked(true);
            } else {
                ContextFrame.keepConnected.setChecked(false);
            }
        }
        }catch (Exception e){
            System.out.println("#3 "+e.getMessage());

        }


    }


    public boolean isOnline() {
       return  Libs.isOnlineApplication(LoginAppActivity.this);
    }


    private boolean failinternet() {



        if (!isOnline()) {
            Intent intent = new Intent(LoginAppActivity.this, NetworkActivity2.class);
            startActivity(intent);
            return false;
        }

        return true;



    }
















    public void onLogin (View view) {
        failinternet();
      /*  ContextFrame.txtLogin.setText("maatheus_moreiira@hotmail.com");
        ContextFrame.txtPassword.setText("1");*/
        if (ContextFrame.txtLogin.getText().toString().isEmpty()) {
            resetEmptyFields("Email obrigatório");
            return;
        }

        if (ContextFrame.txtPassword.getText().toString().isEmpty()) {
            resetEmptyFields("Senha obrigatória");
            return;
        }

        if (!Utils.isNetworkAvailable(getApplicationContext()) && !Utils.isOnline()) {
            resetEmptyFields("Sem conexão com à internet");
            return;
        }





        String endpoint = apiConfig.Api.getEndPointAccess().getDataJson().trim();
        ApiEndpointInterface apiService = ApiClient.getClient(endpoint).create(ApiEndpointInterface.class);

        Call<ResponseBody> partnerServicesAuth = apiService.loginParceiro(
            apiConfig.Api.getEndPointAccessToken().getDataJson(),
            ContextFrame.txtLogin.getText().toString(),
            ContextFrame.txtPassword.getText().toString()
        );

        ContextFrame.pd.setTitle("Aguarde");
        ContextFrame.pd.setMessage("Verificando suas informações");
        ContextFrame.pd.setCancelable(false);
        ContextFrame.pd.show();

        partnerServicesAuth.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String user = "";

                if (response.isSuccessful()) {
                    ContextFrame.pd.dismiss();
                    try {
                        user = response.body().string();
                        JSONObject userParserJson = new JSONObject(user);
                        Boolean isActive = (!userParserJson.isNull("active") && userParserJson.getString("active").equals("I"));
                        Boolean isError = !userParserJson.isNull("error") && userParserJson.getBoolean("error");
                        if ( isActive || isError) {
                            String message = userParserJson.getString("message");
                            resetEmptyFields(message);
                        } else {
                            String nivel = userParserJson.getString("nivel");
                            String device = userParserJson.getString("device");
                            Config userDevice = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE));
                            Config userLogged = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));
                            Config userLoggedStillCoins = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED_STILL_COINS));
                            Niveis niveis =new Gson().fromJson(nivel,Niveis.class);
                            userLoggedStillCoins.setDataJson(String.valueOf(niveis.stillCoins));
                            Config config = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_TOKEN_JWT));
                            config.setDataJson(userParserJson.getString("token"));
                            userLogged.setDataJson(user);
                            userDevice.setDataJson(device);

                            ContextFrame.configModel.update(config, String.format("%s='%s'", "descricao", Constants.API_TOKEN_JWT));
                            ContextFrame.configModel.update(userLogged, String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));
                            ContextFrame.configModel.update(userLoggedStillCoins, String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED_STILL_COINS));
                            ContextFrame.configModel.update(userDevice, String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE));


                            Intent intent = new Intent(ContextFrame.self, HomeActivity.class);

                            intent.putExtra("loggedInUser", user);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } catch (Exception e) {{
                        ContextFrame.pd.dismiss();
                        e.printStackTrace();
                        resetEmptyFields(e.getMessage().toString());
                    }
                        ContextFrame.pd.dismiss();
                        e.printStackTrace();
                        resetEmptyFields(e.getMessage().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("#7 Error");
                ContextFrame.pd.dismiss();
            }
        });
    }

    public void onKeepConnected (View view) {
        if (ContextFrame.txtLogin.getText().toString().trim().isEmpty()
                || ContextFrame.txtPassword.getText().toString().trim().isEmpty()) {
            return;
        }
        if (!ContextFrame.keepConnected.isChecked()) {
            ContextFrame.keepConnected.setChecked(false);
            ContextFrame.configUSERActived.setDescricao(Constants.API_USER_LOGGED_SAVE);
            ContextFrame.configUSERActived.setDataJson(String.format("%s|%s", "", ""));
            ContextFrame.txtLogin.setText("");
            ContextFrame.txtPassword.setText("");
        } else {
            ContextFrame.keepConnected.setChecked(true);
            ContextFrame.configUSERActived.setDescricao(Constants.API_USER_LOGGED_SAVE);
            ContextFrame.configUSERActived.setDataJson(
                String.format("%s|%s", ContextFrame.txtLogin.getText().toString(),
                ContextFrame.txtPassword.getText().toString())
            );
        }

        ContextFrame.configModel.update(ContextFrame.configUSERActived,
                String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED_SAVE));
    }

    public void onRegister (View view) {
        failinternet();
        startActivity(new Intent(ContextFrame.self, RegisterPartnersActivity.class));
    }

    public static void resetEmptyFields (String message) {
        new AlertDialog
            .Builder(ContextFrame.self)
            .setTitle("Atenção")
            .setMessage(message)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ContextFrame.txtLogin.setText("");
                    ContextFrame.txtPassword.setText("");
                    ContextFrame.keepConnected.setChecked(false);
                    ContextFrame.txtLogin.requestFocus();
                }
            })
            .show();
    }

    private void initEvents() {
        ContextFrame.pd = new ProgressDialog(ContextFrame.self);
        ContextFrame.txtLogin = (EditText) findViewById(R.id.inputname_text);
        ContextFrame.txtPassword = (EditText) findViewById(R.id.inputname_text_password);
        ContextFrame.keepConnected = (CheckBox) findViewById(R.id.keepConnected);
        ContextFrame.btnResetPassword = (Button)  findViewById(R.id.btnResetPassword);

    }

    private void createAllDatabase() {
        new PictureBusiness(this).createDataBase();
        new OperationBusiness(this).createDataBase();
        new PrefsBusiness(this).createDataBase();
        new DeviceStatusBusiness(this).createDataBase();
        new AppOpeationEntitySqlite(this);
        new AudioEntitySqlite(this);
    }


    public static class ApiConfig {
        private final AppCompatActivityBase Api;
        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }


    public  void  onResetPassword(View view){
        failinternet();
        startActivity(new Intent(ContextFrame.self,ResetPassword.class));
    }

    public class Components {
        public Config configUSERActived;
        public Config userActivySaveStillCoins;
        public Button btnReset,btnResetPassword;
        public Config userActivyDevice;
        public Config userdevicePhoto;
        ProgressDialog pd;
        private Context self;
        public  Boolean privacityRead=false;
        private ConfigDataAccess configModel;
        private EditText txtLogin, txtPassword;
        private CheckBox keepConnected;
        private Boolean keepConnectedStatus = false;

        public Components(Context self) {
            this.self = self;
        }

        public Boolean getKeepConnectedStatus() {
            return keepConnectedStatus;
        }

        public void setKeepConnectedStatus(Boolean keepConnectedStatus) {
            this.keepConnectedStatus = keepConnectedStatus;
        }
    }
}
