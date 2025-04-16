package br.com.stilldistribuidora.partners.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONObject;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.models.PaternsModel;
import br.com.stilldistribuidora.stillrtc.ui.activities.InitAppActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity {
    private Button btnRegister;
     private PaternsModel paternsModel = new PaternsModel();
    private ApiConfig apiConfig;
    private CheckBox checkbox_meat;

    private EditText termos;
    private boolean termRead=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        InitPermisionGeral();

        this.apiConfig = new ApiConfig(this);

        if (apiConfig.Api.getEndPointAccess() == null ||
                apiConfig.Api.getEndPointAccess().getDataJson() == null ||
                apiConfig.Api.getEndPointAccess().getDataJson().isEmpty()) {
            Intent intent = new Intent(this, InitAppActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        initEvents();
    }

    private void initEvents() {
        btnRegister=(Button) findViewById(R.id.btnadvence);
        loadDataReceived();
        checkbox_meat=(CheckBox) findViewById(R.id.checkbox_meat);
        checkbox_meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) view).isChecked()){
                    btnRegister.setVisibility(View.VISIBLE);
                } else {
                    btnRegister.setVisibility(View.INVISIBLE);
                }
            }
        });

        this.termos=(EditText) findViewById(R.id.termos);
        termos.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (!termos.canScrollVertically(1)) {
                    if(!termRead)
                         checkbox_meat.setVisibility(View.VISIBLE);
                }
                if (!termos.canScrollVertically(-1)) {
                    // top of scroll view
                }

            }
        });




        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processando(true);

                String pix=paternsModel.getPixJson();

                ApiEndpointInterface apiService =
                        ApiClient.getClient(apiConfig.Api.getEndPointAccess().getDataJson().trim()).create(ApiEndpointInterface.class);

                Call<ResponseBody> responseRegisterParteners = apiService.registerPartners(
                        apiConfig.Api.getEndPointAccessToken().getDataJson(),
                        paternsModel.getName(),
                        paternsModel.getLastName(),
                        paternsModel.getMobliePhone(),
                        paternsModel.getCpf(),
                        paternsModel.getEmail(),
                        paternsModel.getPassword(),
                        getResources().getString(R.string.termos_aceitos),
                        paternsModel.getAddress(),pix);


                responseRegisterParteners.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       try{
                           if (response.isSuccessful()) {
                               if(response.body()!=null){
                                   String rawData=response.body().string();
                                   JSONObject data = new JSONObject(rawData);
                                   if(data.getString("status").equals("201")){
                                       new AlertDialog.Builder(TermsActivity.this)
                                               .setTitle(getString(R.string.register_sucesso))
                                               .setMessage(getString(R.string.register_sucesso_mensagen))
                                               // Specifying a listener allows you to take an action before dismissing the dialog.
                                               // The dialog is automatically dismissed when a dialog button is clicked.
                                               .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       resetLogin();
                                                   }
                                               })
                                               // A null listener allows the button to dismiss the dialog and take no further action.
                                               .setIcon(android.R.drawable.ic_dialog_alert)
                                               .show();
                                   }else{
                                       new AlertDialog.Builder(TermsActivity.this)
                                               .setTitle(getString(R.string.error_processed_register))
                                               .setMessage(data.getString("erros"))
                                               // Specifying a listener allows you to take an action before dismissing the dialog.
                                               // The dialog is automatically dismissed when a dialog button is clicked.
                                               .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       resetLogin();
                                                   }
                                               })
                                               .setIcon(android.R.drawable.ic_dialog_alert)
                                               .show();
                                   }
                               }
                           } else {
                               new AlertDialog.Builder(TermsActivity.this)
                                       .setTitle(getString(R.string.error_processed_register))
                                       .setMessage(getString(R.string.enterContactSuport))
                                       // Specifying a listener allows you to take an action before dismissing the dialog.
                                       // The dialog is automatically dismissed when a dialog button is clicked.
                                       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {
                                               resetLogin();
                                           }
                                       })
                                       // A null listener allows the button to dismiss the dialog and take no further action.
                                       .setIcon(android.R.drawable.ic_dialog_alert)
                                       .show();
                           }
                       }catch (Exception e){
                           new AlertDialog.Builder(TermsActivity.this)
                                   .setTitle(getString(R.string.error_processed_register))
                                   .setMessage(getString(R.string.enterContactSuport))
                                   // Specifying a listener allows you to take an action before dismissing the dialog.
                                   // The dialog is automatically dismissed when a dialog button is clicked.
                                   .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int which) {
                                           resetLogin();
                                       }
                                   })
                                   // A null listener allows the button to dismiss the dialog and take no further action.
                                   .setIcon(android.R.drawable.ic_dialog_alert)
                                   .show();


                       }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        new AlertDialog.Builder(TermsActivity.this)
                                .setTitle(getString(R.string.error_processed_register))
                                .setMessage(getString(R.string.enterContactSuport))
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        resetLogin();
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.

                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();



                    }
                });



            }
        });

    }

    private void resetLogin() {
        Intent intent = new Intent(TermsActivity.this, LoginAppActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void processando(boolean isProcessed) {
        if(isProcessed){
            btnRegister.setText("Processando...!");
            btnRegister.setClickable(false);
            checkbox_meat.setClickable(false);
        }else{
            btnRegister.setText(getString(R.string.str_parceiro));
            btnRegister.setClickable(true);
            checkbox_meat.setClickable(true);
        }
    }

    private void loadDataReceived() {
        if (getIntent().hasExtra("name")) {
            paternsModel.setName(getIntent().getStringExtra("name"));
        }
        if (getIntent().hasExtra("lastName")) {
            paternsModel.setLastName(getIntent().getStringExtra("lastName"));
        }
        if (getIntent().hasExtra("cpf")) {
            paternsModel.setCpf(getIntent().getStringExtra("cpf"));
        }
        if (getIntent().hasExtra("mobliePhone")) {
            paternsModel.setMobliePhone(getIntent().getStringExtra("mobliePhone"));
        }

        if (getIntent().hasExtra("email")) {
            paternsModel.setEmail(getIntent().getStringExtra("email"));
        }

        if (getIntent().hasExtra("password")) {
            paternsModel.setPassword(getIntent().getStringExtra("password"));
        }

        if (getIntent().hasExtra("address")) {
            paternsModel.setAddress(getIntent().getStringExtra("address"));
        }

        if (getIntent().hasExtra("pix")) {
            paternsModel.setPixJson(getIntent().getStringExtra("pix"));
        }


    }




    /*Permisoes Gerais*/
    public void InitPermisionGeral() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
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
                ActivityCompat.requestPermissions(this,
                        new String[]{
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




                        }, 10000);
            }

        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



    public static class ApiConfig {
        private final AppCompatActivityBase Api;
        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }





}