package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import br.com.stilldistribuidora.partners.resources.Resources;
import br.com.stilldistribuidora.partners.views.LoginAppActivity;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.OperationPartner;
import br.com.stilldistribuidora.stillrtc.db.models.Config;


public class InitAppActivity extends AppCompatActivity {

    private final ComponentsGui GUI = new ComponentsGui();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        GUI.configDataAccess = new ConfigDataAccess(getApplicationContext());
        GUI.operationPartnerDataAcess = new OperationPartner(getApplicationContext());
        GUI.operationPartnerDataAcess.createDataBase();
        GUI.etToken = findViewById(R.id.token);
        GUI.etApiUrlConection = findViewById(R.id.uriAPI);
        GUI.etApiUrlConectionBackoffice= findViewById(R.id.uriBackOffice);
        GUI.etApiUrlConection.setText("https://stillrtc.com/api/");
        GUI.etApiUrlConectionBackoffice.setText("https://stillrtc.com/still/rtc/");
        GUI.etToken.setText("dHVkbyBtZSDDqSBsw61jaXRvIG1hcyBuZW0gdHVkbyBtZSBjb252w6lt_");

/*

        GUI.etApiUrlConection.setText("http://10.0.2.2/still-rtc/api/");
        GUI.etApiUrlConectionBackoffice.setText("http://10.0.2.2/still-rtc/");
        GUI.etToken.setText("dHVkbyBtZSDDqSBsw61jaXRvIG1hcyBuZW0gdHVkbyBtZSBjb252w6lt_");
*/

        GUI.buttonNext =(Button) findViewById(R.id.buttonNext);
        GUI.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySettings(v);
            }
        });

        GUI.buttonNext.callOnClick();
    }

    public void applySettings(View view) {
        try {
            Config configUrl = new Config();
            Config configUrlBackOffice = new Config();
            Config configToken = new Config();
            Config configIME = new Config();
            Config configJWT = new Config();
            Config configJWTUSER = new Config();
            Config configUSERLogged = new Config();
            Config configStillCoins = new Config();
            Config configUSERIdOperationLastCurrent = new Config();
            configUSERIdOperationLastCurrent.setDescricao(String.valueOf(Resources.LAST_ID_AND_POSSITION_OPERATION_CURRENTR));
            configUSERIdOperationLastCurrent.setDataJson("");
            configUSERLogged.setDescricao(Constants.API_USER_LOGGED);
            configUSERLogged.setDataJson("");
            configJWTUSER.setDescricao(Constants.API_TOKEN_USER);
            configJWTUSER.setDataJson("");
            configJWT.setDescricao(Constants.API_TOKEN_JWT);
            configJWT.setDataJson("");
            configUrl.setDescricao(Constants.API_URL);
            configUrl.setDataJson(GUI.etApiUrlConection.getText().toString());
            configUrlBackOffice.setDescricao(Constants.API_URL_BACKOFFICE);
            configUrlBackOffice.setDataJson(GUI.etApiUrlConectionBackoffice.getText().toString());
            configStillCoins.setDescricao(Constants.API_USER_LOGGED_STILL_COINS);
            configStillCoins.setDataJson("0");
            configToken.setDescricao(Constants.API_TOKEN);
            configToken.setDataJson(GUI.etToken.getText().toString());
            configIME.setDescricao(Constants.APP_IME_STILL);
            configIME.setDataJson(UUID.randomUUID().toString().replace("-", ""));
            GUI.configDataAccess.insert(configUrl);
            GUI.configDataAccess.insert(configToken);
            GUI.configDataAccess.insert(configIME);
            GUI.configDataAccess.insert(configUrlBackOffice);
            GUI.configDataAccess.insert(configJWT);
            GUI.configDataAccess.insert(configJWTUSER);
            GUI.configDataAccess.insert(configUSERLogged);
            GUI.configDataAccess.insert(configStillCoins);
            GUI.configDataAccess.insert(configUSERIdOperationLastCurrent);
            Intent intent = new Intent(getApplicationContext(), LoginAppActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (Exception ex) {
            System.out.println(ex);
            Toast.makeText(getApplicationContext(), "Não foi possível continuar, informe à Still App", Toast.LENGTH_SHORT);
        }
    }

    private static class ComponentsGui {
        private ConfigDataAccess configDataAccess;
        private OperationPartner operationPartnerDataAcess;
        private EditText etApiUrlConection;
        private EditText etApiUrlConectionBackoffice;
        private EditText etToken;
        private Button buttonNext;
    }
}
