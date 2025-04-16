package br.com.stilldistribuidora.partners.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import br.com.stilldistribuidora.httpService.partners.PartnersServices;
import br.com.stilldistribuidora.partners.Base.ActivityBaseApp;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.ui.activities.InitAppActivity;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword  extends ActivityBaseApp {
    private static ActivityComponents ActivityComponents = new ActivityComponents();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ActivityComponents.context=this;
        ActivityComponents.Api = new Api(ActivityComponents.context);
        ActivityComponents.inputEmail = (EditText) findViewById(R.id.input_email);
        ActivityComponents.inputCpf = (EditText) findViewById(R.id.input_cpf);
        ActivityComponents.newPassword = (EditText) findViewById(R.id.newPassword);
        ActivityComponents.containerInputNewPassword = (TextInputLayout) findViewById(R.id.containerInputNewPassword);



        ActivityComponents.newPassword = (EditText) findViewById(R.id.newPassword);
        ActivityComponents.btnActionValidateOrResetPassword = (Button) findViewById(R.id.btnActionValidateOrResetPassword);

        if (ActivityComponents.Api.Api.getEndPointAccess() == null ||
                ActivityComponents.Api.Api.getEndPointAccess().getDataJson() == null ||
                ActivityComponents.Api.Api.getEndPointAccess().getDataJson().isEmpty()) {
            Intent intent = new Intent(ActivityComponents.context, InitAppActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        startOnEventsCliks();
    }

    private void startOnEventsCliks() {
        ActivityComponents.btnActionValidateOrResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitPermissions();
                if(ActivityComponents.newPassword.getText().toString().isEmpty()){
                    validateInformation();
                }else{
                    changeUserPassword();
                }
            }
        });

    }

    private void changeUserPassword() {
        if (ActivityComponents.newPassword.getText().toString().isEmpty()) {
            resetEmptyFields(getString(R.string.newPasswordInvalid));
            return;
        }

        String endpoint = ActivityComponents.Api.Api.getEndPointAccess().getDataJson().trim();
        ApiEndpointInterface ServiceHttp = ApiClient.getClient(endpoint).create(ApiEndpointInterface.class);
        Call<ResponseBody> responseUpdatePassword  = ServiceHttp.updatePartnerPassword(
                ActivityComponents.inputEmail.getText().toString(),
                ActivityComponents.inputCpf.getText().toString(),
                ActivityComponents.newPassword.getText().toString()
        );

        responseUpdatePassword.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.isSuccessful()){
                        JSONObject jObjError = new JSONObject(response.body().string());


                        new AlertDialog
                                .Builder(ActivityComponents.context)
                                .setTitle(ActivityComponents.context.getString(R.string.sys))
                                .setMessage(jObjError.getJSONObject("data").getString("message"))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent  = new Intent(ActivityComponents.context, LoginAppActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }

                }catch (Exception exception){}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });





    }

    private void validateInformation() {
        if (ActivityComponents.inputEmail.getText().toString().isEmpty()) {
            resetEmptyFields(getString(R.string.emailinvalid));
            return;
        }

        if (ActivityComponents.inputCpf.getText().toString().isEmpty()) {
            resetEmptyFields(getString(R.string.cpfInvalide));
            return;
        }
        if (!Utils.isNetworkAvailable(getApplicationContext()) && !Utils.isOnline()) {
            resetEmptyFields(getString(R.string.not_conect));
            return;
        }

        String endpoint = ActivityComponents.Api.Api.getEndPointAccess().getDataJson().trim();
        ApiEndpointInterface ServiceHttp = ApiClient.getClient(endpoint).create(ApiEndpointInterface.class);
        Call<ResponseBody> responseBodyCall = ServiceHttp.validateUserInformation(
                ActivityComponents.inputEmail.getText().toString(),
                ActivityComponents.inputCpf.getText().toString()
        );

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ActivityComponents.containerInputNewPassword.setEnabled(true);
                    ActivityComponents.newPassword.requestFocus();

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        resetEmptyFields( jObjError.getJSONObject("data").getString("message"));
                    } catch (Exception e) {
                        resetEmptyFields(e.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t);
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void InitPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                    ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INSTALL_SHORTCUT) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.LOCATION_HARDWARE) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {

            } else {
                String[] permissions = {
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.INSTALL_SHORTCUT,
                        android.Manifest.permission.LOCATION_HARDWARE,
                        android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                        android.Manifest.permission.WRITE_SETTINGS,
                        android.Manifest.permission.WAKE_LOCK,
                        android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                };

                ActivityCompat.requestPermissions(this, permissions, 10000);
            }

        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }








    public static class Api {
        private final AppCompatActivityBase Api;

        public Api(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }

    public static void resetEmptyFields (String message) {
        new AlertDialog
                .Builder(ActivityComponents.context)
                .setTitle(ActivityComponents.context.getString(R.string.sys))
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityComponents.inputEmail.setText("");
                        ActivityComponents.inputCpf.setText("");
                        ActivityComponents.newPassword.setText("");
                        ActivityComponents.containerInputNewPassword.setEnabled(false);
                        ActivityComponents.inputEmail.requestFocus();
                    }
                })
                .show();
    }


    public  static class  ActivityComponents{
        public  Api Api;
        public Button btnActionValidateOrResetPassword;
        public ResetPassword context;
        public EditText inputEmail,inputCpf,newPassword;
        public PartnersServices partnersServices;
        public Button resetPassword;
        public TextInputLayout containerInputNewPassword;
    }



}
