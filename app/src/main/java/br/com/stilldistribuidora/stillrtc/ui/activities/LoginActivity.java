package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import br.com.stilldistribuidora.pco.ui.LoginPco;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.business.DeviceStatusBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.OperationBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.PictureBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.PrefsBusiness;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.db.models.Device;
import br.com.stilldistribuidora.stillrtc.db.models.Store;
import br.com.stilldistribuidora.stillrtc.db.models.User;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Desenvolvido por Cleidimar Viana
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private final GuiInterface Gui = new GuiInterface();
    private Context context;
    private ApiConfig apiConfig;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        apiConfig = new ApiConfig(context);
        InitPermisionGeral();

        setContentView(R.layout.activity_login);
        Gui.toolbar = findViewById(R.id.toolbar_login);
        this.setTitle(R.string.still_pco_title_menu);
        setSupportActionBar(Gui.toolbar);
        /*Inicializa o servico de fPings*/
        Gui.prefsHelper = new PrefsHelper(context);
        initUI();
        listenerUI();
        createAllDatabase();
        if (apiConfig.Api.getEndPointAccess() == null ||
                apiConfig.Api.getEndPointAccess().getDataJson() == null ||
                apiConfig.Api.getEndPointAccess().getDataJson().isEmpty()) {
            Intent intent = new Intent(context, InitAppActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        Gui.tvVersion.setText(Utils.getVersion(context));
    }

    public void initUI() {
        Gui.btnIdentifier = findViewById(R.id.etIdentifier);
        Gui.btnPassword = findViewById(R.id.etPassword);
        Gui.btnLogin = findViewById(R.id.btnLogin);
        Gui.mSwitch = findViewById(R.id.mSwitch);
        Gui.tvVersion = findViewById(R.id.tvVersion);
        Gui.tvResponse = findViewById(R.id.tv_response);
        Gui.progressBar = findViewById(R.id.progressBar);
    }

    public void listenerUI() {
        if (Gui.prefsHelper.getPref(PrefsHelper.PREF_IS_LOGINED) != null) {
            Gui.mSwitch.setChecked(true);
            Gui.btnIdentifier.setText(Gui.prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER).toString());
            Gui.btnPassword.setText(Gui.prefsHelper.getPref(PrefsHelper.PREF_DEVICE_PWD).toString());
        }

        Gui.btnIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gui.tvResponse.setVisibility(View.GONE);
            }
        });

        Gui.btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gui.tvResponse.setVisibility(View.GONE);
            }
        });


        Gui.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidenKeyboarde(v);
                String identifier = Gui.btnIdentifier.getText().toString();
                String password = Gui.btnPassword.getText().toString();
                if (identifier.trim().isEmpty() || password.trim().isEmpty()) {
                    //Toast.makeText(LoginActivity.this, getText(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();
                    Gui.tvResponse.setVisibility(View.VISIBLE);
                    Gui.tvResponse.setText(getText(R.string.error_fields_empty));
                } else {
                    showProgress();
                    if (Utils.isNetworkAvailable(LoginActivity.this)) {
                        if (identifier.substring(0, 1).matches("[0-9]")) {
                            //Dispositivo Log
                            doLoginIn(identifier, password);
                            Constants.IS_USER_IDENTIFIER = "D";
                        } else {
                            doClientLoginIn(identifier, password);
                            Constants.IS_USER_IDENTIFIER = "C";
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.alert_info_error_connection), Toast.LENGTH_LONG).show();
                        dimissProgress();
                    }
                }

            }
        });

        Gui.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    Gui.prefsHelper.delete(PrefsHelper.PREF_DEVICE_IDENTIFIER);
                    Gui.prefsHelper.delete(PrefsHelper.PREF_DEVICE_PWD);
                    Gui.prefsHelper.delete(PrefsHelper.PREF_IS_LOGINED);

                    Gui.btnIdentifier.setText("");
                    Gui.btnPassword.setText("");
                    Gui.btnIdentifier.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(Gui.btnIdentifier, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    private void createAllDatabase() {
        new PictureBusiness(this).createDataBase();
        new OperationBusiness(this).createDataBase();
        new PrefsBusiness(this).createDataBase();
        new DeviceStatusBusiness(this).createDataBase();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Gui.prefsHelper.getPref(PrefsHelper.PREF_IS_LOGINED) != null) {
            Gui.mSwitch.setChecked(true);
            Gui.btnIdentifier.setText(Gui.prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER).toString());
            Gui.btnPassword.setText(Gui.prefsHelper.getPref(PrefsHelper.PREF_DEVICE_PWD).toString());
        } else {
            Gui.btnPassword.setText("");
            Gui.btnIdentifier.setText("");
            if (Gui.prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER) != null) {
                Gui.btnIdentifier.setText(Gui.prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER).toString());
                Gui.btnPassword.requestFocus();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                break;
            case R.id.active_start_pco:
                Intent intent = new Intent(LoginActivity.this, LoginPco.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void showProgress() {
        Gui.progressBar.setVisibility(View.VISIBLE);
    }

    public void dimissProgress() {
        Gui.progressBar.setVisibility(View.GONE);
    }

    public void doLoginIn(String identifier, final String password) {
        ApiEndpointInterface apiService =
                ApiClient.getClient(apiConfig.Api.getEndPointAccess().getDataJson().trim()).create(ApiEndpointInterface.class);
        Call<Device.Result> call = apiService.autenticarDispositivo(
                apiConfig.Api.getDeviceUuid().getDataJson(),
                apiConfig.Api.getEndPointAccessToken().getDataJson()
                , identifier, password, 4);
        call.enqueue(new Callback<Device.Result>() {
            @Override
            public void onResponse(Call<Device.Result> call, Response<Device.Result> response) {
                if (response != null) {
                    if (response.body().ar.size() > 0) {
                        Device device = response.body().ar.get(0);
                        //Log.wtf(TAG, "" + device.getJwt());
                        Config configJWT = new Config();
                        configJWT.setDescricao(Constants.API_TOKEN_JWT);
                        configJWT.setDataJson(device.getJwt());
                        apiConfig.Api.getConfigDataAccess().update(configJWT, "");
                        if (Gui.mSwitch.isChecked()) {
                            Gui.prefsHelper.savePref(PrefsHelper.PREF_IS_LOGINED, true);
                            Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_PWD, password);
                        }
                        Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_ID, device.getId());
                        Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_IDENTIFIER, device.getIdentifier());
                        Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_OPERATOR, device.getOperator());
                        Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_API_KEY, device.getApiKey());
                        Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_UUID, device.getUuid());
                        overridePendingTransition(0, 0);
                        dimissProgress();
                        startActivity(new Intent(LoginActivity.this, PrefsActivity.class));
                        overridePendingTransition(0, 0);
                     /*
                        Intent intent=new Intent(LoginActivity.this,TiposDeAcessos.class);
                        startActivity(intent);
                      */
                    } else {

                        String str = getString(R.string.str_info_auth_incorrect);
                        Gui.tvResponse.setVisibility(View.VISIBLE);
                        Gui.tvResponse.setText(str);
                        dimissProgress();
                        //Toast.makeText(LoginActivity.this, "Erro de autenticação", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Device.Result> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.alert_info_error_connection), Toast.LENGTH_LONG).show();

//                String str = "Ocorreu um erro ao acessar o servidor!";
//                tvResponse.setVisibility(View.VISIBLE);
//                tvResponse.setText(str);

                dimissProgress();
            }
        });
    }

    public void doClientLoginIn(String identifier, final String password) {

        final String identifier_ = identifier;
        final String password_ = password;
        ApiEndpointInterface apiService =
                ApiClient.getClient(
                        apiConfig.Api.getEndPointAccess().getDataJson()
                ).create(ApiEndpointInterface.class);

        Call<User.Result> call = apiService.autenticarUsuario(
                apiConfig.Api.getDeviceUuid().getDataJson(),
                apiConfig.Api.getEndPointAccessToken().getDataJson(),
                identifier, password, 2);

        call.enqueue(new Callback<User.Result>() {
            @Override
            public void onResponse(Call<User.Result> call, Response<User.Result> response) {
                int isLoginUser;
                if (response != null) {
                    try {


                        try {
                            isLoginUser = response.body().ar.size();
                        } catch (Exception e) {
                            isLoginUser = 0;
                        }
                        if (isLoginUser > 0) {
                            User user = response.body().ar.get(0);
                            Config configJWT = new Config();
                            configJWT.setDescricao(Constants.API_TOKEN_JWT);
                            configJWT.setDataJson(user.getJwt());
                            apiConfig.Api.getConfigDataAccess().update(configJWT, "");

                            if (Gui.mSwitch.isChecked()) {
                                Gui.prefsHelper.savePref(PrefsHelper.PREF_IS_LOGINED, true);
                                Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_IDENTIFIER, user.getEmail());
                                Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_OPERATOR, user.getName());

                                Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_PWD, password);
                            } else {
                                Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_IDENTIFIER, user.getEmail());
                            }

                            Gui.prefsHelper.savePref(PrefsHelper.PREF_DEVICE_API_KEY, user.getApiKey());

                            if (user.getLevel() == 5) {
                                Gui.prefsHelper.savePref(PrefsHelper.PREF_USER_ID, user.getId());
                                startActivity(new Intent(LoginActivity.this, StoresActivity.class));

                            } else if (user.getLevel() == 8) {
                                try {
                                    Gui.prefsHelper.savePref(PrefsHelper.PREF_USER_ID, user.getId());
                                    Intent supervisor = new Intent(LoginActivity.this, SupervisorActivity.class);
                                    startActivity(supervisor);
                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }

                                //startActivity(new Intent(LoginActivity.this,SupervisorActivity.class ));
                            } else {
                                Gui.prefsHelper.savePref(PrefsHelper.PREF_USER_ID, user.getId());
                                Gui.prefsHelper.savePref(PrefsHelper.PREF_CLIENT_ID, user.getClientId());
                                Store.Result phraseResult = new Store.Result();
                                ArrayList<Store> arr = new ArrayList<Store>();
                                Store store = new Store();
                                store.setName(user.getName());
                                store.setId(user.getId());
                                arr.add(store);
                                phraseResult.ar = arr;
                                Intent intent;
                                intent = new Intent(LoginActivity.this, DeliveriesActivity.class);
                                intent.putExtra("object", phraseResult);
                                intent.putExtra("position", 0);
                                startActivity(intent);

                            }
                            dimissProgress();
                        } else {
                            /*Login no PCO PARCEIRO GRAFICA*/
                            String str = getString(R.string.str_info_auth_incorrect);
                            Gui.tvResponse.setVisibility(View.VISIBLE);
                            Gui.tvResponse.setText(str);
                            dimissProgress();
                        }

                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Ocorreu um erro ao acessar o servidor!", Toast.LENGTH_SHORT).show();

                        String str = "Ocorreu um erro ao acessar o servidor!";
                        Gui.tvResponse.setVisibility(View.VISIBLE);
                        Gui.tvResponse.setText(str);

                        e.printStackTrace();

                        dimissProgress();
                    }


                }
            }

            @Override
            public void onFailure(Call<User.Result> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    public void hidenKeyboarde(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static class GuiInterface {

        private EditText btnIdentifier;
        private EditText btnPassword;
        private Button btnLogin;
        private Switch mSwitch;
        private TextView tvVersion;
        private TextView tvResponse;
        private ProgressBar progressBar;
        private PrefsHelper prefsHelper;
        private Toolbar toolbar;

    }

    public static class ApiConfig {
        private final AppCompatActivityBase Api;
        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }



    /*Permisoes Gerais*/
    public void InitPermisionGeral() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
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
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.INSTALL_SHORTCUT,
                                Manifest.permission.LOCATION_HARDWARE,
                                Manifest.permission.SYSTEM_ALERT_WINDOW


                        }, 10000);
            }

        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



}
