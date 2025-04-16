package br.com.stilldistribuidora.pco.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.com.stilldistribuidora.pco.api.ApiPco;
import br.com.stilldistribuidora.pco.config.Constante;
import br.com.stilldistribuidora.pco.db.dao.AutoLogin;
import br.com.stilldistribuidora.pco.db.dao.wsConfig;
import br.com.stilldistribuidora.pco.db.model.WsConfig;
import br.com.stilldistribuidora.pco.servicos.GpsPco;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.ui.activities.LoginActivity;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPco extends AppCompatActivity    {

    private static final int CAMERA = 3;
    private GpsPco gps;
    private AutoLogin autoLogin;
    private Toolbar toolbar;
    private final int WAIT_TIME = 5000;
    private Context self;
    private Boolean isConect = false;
    private EditText usuario;
    private EditText passowd;
    private Button btnLogin;
    private Switch save_login;
    private ProgressBar progressBar;
    private wsConfig wsConfigCotroller;
    private int progressStatus = 0;
    private boolean in_progress = false;
    private Handler handler = new Handler();
    private String tokenApi;
    private String tokenApiAtivo;
    private String deviceApi;
    private TextView tv_response;
    private AlertDialog alerta;
    private Switch mSwitchs;
    private ProgressDialog barProgressDialog=null;
    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_login_pco);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar_login_rtc);
        this.setTitle(R.string.still_pco_title_menu);
        setSupportActionBar(this.toolbar);
        wsConfigCotroller = new wsConfig(self);
        initComponents();
        initActionGui();
        initCongisApp();
        getStatusServidor();
        InitPermisionGeral();

        //this.usuario.setText("b1@b.c");
        //this.passowd.setText("1");

       // this.usuario.setText("M1@still");
        //this.passowd.setText("1");



    }

    private void initCongisApp() {
        //Configura e cria a Base de Dados
        String api = (String) wsConfigCotroller.getBy(new WsConfig(Constante.API_ACTIVE_URL));
        if (api.trim().isEmpty()) {
            wsConfigCotroller.insert(new WsConfig(Constante.API_ACTIVE_URL, "-"));
        }
        String users_active_pco = (String) wsConfigCotroller.getBy(new WsConfig(Constante.DEVICE_ACESS_USER_LOGIN));
        if (users_active_pco.trim().isEmpty()) {
            wsConfigCotroller.insert(new WsConfig(Constante.DEVICE_ACESS_USER_LOGIN, "-"));
        } else {
            String[] split = users_active_pco.split("\\|");
            if (split.length == 2) {
                usuario.setText(split[0]);
                passowd.setText(split[1]);
                mSwitchs.setChecked(true);
            }
        }



        new Thread(new Runnable() {
            @Override
            public void run() {


                String devices_configs = (String) wsConfigCotroller.getBy(new WsConfig(Constante.DEVICE_ACESS));
                if (devices_configs.trim().isEmpty()) {
                    //cria um dispositivo para indentificar no sistema
                    new ApiPco().api().ws_app_generats(ApiPco.API_PATH_PRIVATE_INSTALL).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String resp = response.body().string();
                                    JSONObject jsonObject = new JSONObject(resp);
                                    String error = jsonObject.getString("error");
                                    String http = jsonObject.getString("http");
                                    if (error.equals("false") && http.equals("200")) {
                                        String data = jsonObject.getString("data");
                                        JSONArray array_data = new JSONArray(data);
                                        JSONObject dados = new JSONObject(array_data.get(0).toString());
                                        String device = dados.optString("appidentic");
                                        wsConfigCotroller.insert(new WsConfig(Constante.DEVICE_ACESS, device));
                                        messagenBox(self,getResources().getString(R.string.sys_msn_aviso),
                                                getResources().getString(R.string.sys_device_regs_sucess), false);

                                    } else {
                                        String data = jsonObject.getString("data");
                                        JSONArray array_data = new JSONArray(data);
                                        JSONObject dados = new JSONObject(array_data.get(0).toString());
                                        messagenBox(self,getResources().getString(R.string.sys_msn_aviso), dados.getString("msn"), false);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {


                            messagenBox(self,getResources().getString(R.string.sys_msn_aviso),
                                    getResources().getString(R.string.sys_error_msn_devices), false);

                        }
                    });
                    //cria um dispositivo para indentificar no sistema
                }
            }
        }).start();


    }

    private void initActionGui() {
        //set o botao de login


        mSwitchs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    if (usuario.getText().toString().trim().isEmpty()) {
                        tv_response.setText(getString(R.string.preenchidos_corretamente));
                        tv_response.setVisibility(View.VISIBLE);
                        mSwitchs.setChecked(false);
                        return;
                    }
                    if (passowd.getText().toString().trim().isEmpty()) {
                        tv_response.setText(getString(R.string.preenchidos_corretamente));
                        tv_response.setVisibility(View.VISIBLE);
                        mSwitchs.setChecked(false);
                        return;
                    }
                    String user_active = String.format("%s|%s", usuario.getText().toString().trim(), passowd.getText().toString().trim());
                    wsConfigCotroller.update(new WsConfig(Constante.DEVICE_ACESS_USER_LOGIN, user_active));
                } else {
                    usuario.setText("");
                    passowd.setText("");
                    usuario.requestFocus();
                    String user_active = String.format("%s|%s", usuario.getText().toString().trim(), passowd.getText().toString().trim());
                    wsConfigCotroller.update(new WsConfig(Constante.DEVICE_ACESS_USER_LOGIN, user_active));
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String devices_configs = (String) wsConfigCotroller.getBy(new WsConfig(Constante.DEVICE_ACESS));

                if (usuario.getText().toString().trim().isEmpty() || passowd.getText().toString().trim().isEmpty()) {
                    tv_response.setText(getString(R.string.preenchidos_corretamente));
                    tv_response.setVisibility(View.VISIBLE);
                    return;
                }
                tv_response.setVisibility(View.GONE);


                    //processando(self,getString(R.string.grf_msn_systema_alert),getString(R.string.sys_processando_agarde));
                    /*Realiza o Login com Servidor*/


                    barProgressDialog = new ProgressDialog(self);
                    barProgressDialog.setTitle(getString(R.string.grf_msn_systema_alert));
                    barProgressDialog.setMessage(getString(R.string.sys_processando_agarde));
                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                    barProgressDialog.setCancelable(false);
                    barProgressDialog.show();

                    System.out.println("TT:" + devices_configs);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new ApiPco().api().ws_gf_login_parceiro(ApiPco.API_PATH_PRIVATE_INSTALL,
                                    devices_configs, usuario.getText().toString(), passowd.getText().toString()).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.body().string());
                                            String error = jsonObject.getString("error");
                                            String http = jsonObject.getString("http");
                                            if (error.equals("false") && http.equals("200")) {
                                                String data = jsonObject.getString("data");
                                                JSONArray array_data = new JSONArray(data);
                                                JSONObject dados = new JSONObject(array_data.get(0).toString());
                                                String user_login = dados.optString("us_codigo") + "|" + dados.optString("us_perfil");
                                                String JWT = dados.optString("jwt_acess");
                                                //Salva o Usuario Logado
                                                String user_ativo = (String) wsConfigCotroller.getBy(new WsConfig("USER_ATIVO"));
                                                if (user_ativo.trim().isEmpty()) {
                                                    wsConfigCotroller.insert(new WsConfig(Constante.USER_ATIVO, user_login));
                                                    wsConfigCotroller.insert(new WsConfig(Constante.TOKEN_JWT, JWT));
                                                    wsConfigCotroller.insert(new WsConfig(Constante.SERVICE_IS_ACTIVE, "0"));
                                                } else {
                                                    wsConfigCotroller.update(new WsConfig(Constante.USER_ATIVO, user_login));
                                                    wsConfigCotroller.update(new WsConfig(Constante.TOKEN_JWT, JWT));
                                                    wsConfigCotroller.update(new WsConfig(Constante.SERVICE_IS_ACTIVE, "0"));
                                                }
                                                try {
                                                    /*Inicia a Interface Principal*/
                                                    Intent list_grafica = new Intent(self, movimentos_list_grf.class);
                                                    list_grafica.putExtra("id_use", dados.optString("us_codigo"));
                                                    list_grafica.putExtra("qt_a_retirar", dados.optString("qt_a_retira"));
                                                    list_grafica.putExtra("device", devices_configs);
                                                    list_grafica.putExtra("token_ativo", JWT);
                                                    barProgressDialog.cancel();
                                                    barProgressDialog.dismiss();
                                                    startActivity(list_grafica);
                                                    //finish();
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                String data = jsonObject.getString("data");
                                                JSONArray array_data = new JSONArray(data);
                                                JSONObject dados = new JSONObject(array_data.get(0).toString());
                                                messagenBox(self,getResources().getString(R.string.sys_msn_aviso), dados.getString("msn"), false);
                                                tv_response.setText(dados.getString("msn"));
                                                tv_response.setVisibility(View.VISIBLE);

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        messagenBox(self,getResources().getString(R.string.sys_msn_aviso),
                                                getResources().getString(R.string.sys_not_valid_user), false);
                                    }
                                    barProgressDialog.cancel();
                                    barProgressDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    messagenBox(self,getResources().getString(R.string.sys_msn_aviso),
                                            getResources().getString(R.string.sys_not_valid_user), false);

                                    barProgressDialog.cancel();
                                    barProgressDialog.dismiss();

                                }
                            });
                        }
                    }).start();

                    /*Realiza o Login com Servidor*/



            }
        });
        //set o botao de login


        //Set o botao de Switch

        save_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                long sv = 0;
                if (isChecked && !usuario.getText().toString().isEmpty()) {

                    String senha = autoLogin.getSenha(usuario.getText().toString());
                    if (senha.isEmpty()) {
                        sv = autoLogin.insert(usuario.getText().toString(), passowd.getText().toString());

                    }
                    Toast.makeText(self, "ddd1122", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(self, "ddd1122adsasdasdas", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Set o botao de Switch
    }

    private void initComponents() {
        usuario = (EditText) findViewById(R.id.etIdentifier);
        passowd = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        save_login = (Switch) findViewById(R.id.mSwitch);
        tv_response = (TextView) findViewById(R.id.tv_response);
        progressBar = findViewById(R.id.progressBar);
        mSwitchs = (Switch) findViewById(R.id.mSwitchs);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.setText(String.format("%S",Constante.AppPcoVersion));

    }

    private void getStatusServidor() {
          /*  new Thread( new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            invalidateOptionsMenu();
                            Thread.sleep( WAIT_TIME );
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }


                }
            } ).start();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.current_place_menu, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rtc, menu);
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

            case R.id.active_start_rtc:
                Intent intent = new Intent(LoginPco.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);


                break;

            default:

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem status = menu.findItem(R.id.menu_status_server);
        if (Utils.isNetworkAvailable(self) && Utils.isOnline()) {

            new ApiPco().api().api().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            System.out.println("##-1");
                            status.setIcon(getResources().getDrawable(R.drawable.ic_wifi_black_conect_32dp));
                            isConect = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            status.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_no_conect_24dp));
                            isConect = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                            status.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_no_conect_24dp));
                            isConect = false;
                        }
                    } else {
                        //Toast.makeText(MainActivity.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                        status.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_no_conect_24dp));
                        isConect = false;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    status.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_no_conect_24dp));
                }
            });
        } else {
            if (!isConect) {
                status.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_no_conect_24dp));
                System.out.println("##-2");
            }
        }
        //status.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_no_conect_24dp));

        // status.setIcon(getResources().getDrawable(R.drawable.ic_wifi_black_conect_32dp));
        return true;
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
                ActivityCompat.requestPermissions(LoginPco.this,
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


     public void processando(Context context, String title, String msn) {

        try{

            barProgressDialog = new ProgressDialog(self);
            barProgressDialog.setTitle(title);
            barProgressDialog.setMessage(msn);
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
            barProgressDialog.setCancelable(false);
            barProgressDialog.show();
           }catch (Exception e){
            e.printStackTrace();
        }


    }

     public void messagenBox(Context context, String Titulo, String menssagen, boolean cancelar) {
//Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //define o titulo
        builder.setTitle(Titulo);
        //define a mensagem
        builder.setMessage(menssagen);
        //define um botão como positivo
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText( self, "positivo=" + arg1, Toast.LENGTH_SHORT ).show();
            }
        });
        if (cancelar) {
            //define um botão como negativo.
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //Toast.makeText( self, "negativo=" + arg1, Toast.LENGTH_SHORT ).show();
                }
            });
        }
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
