package br.com.stilldistribuidora.partners.views;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import br.com.stilldistribuidora.common.CastResponseApi;
import br.com.stilldistribuidora.common.LoadProcess;
import br.com.stilldistribuidora.httpService.partners.PartnersServices;
import br.com.stilldistribuidora.partners.Adapter.ConfigurationsUser;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.interfac.OnApiResponse;




public class CancelOperationActivity extends AppCompatActivity {
    private  static Components components=new Components();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        setContentView(R.layout.activity_cancel_operation);
        Intent i = getIntent();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        components.selectedRoute = (OperationModelRepository) i.getSerializableExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE);
        components.contextActivity=this;
        components.conf = new ServerConfiguration(components.contextActivity);
        components.servicesHttp = new ServicesHttp(components.contextActivity, components.conf, CallBackOnResponseGlobalApi());
        components.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(this));
        components.userLoginCast = gson.fromJson(components.conf.getUserLogged(), UserLoginCast.class);
        components.deliveryRepository = new AppOpeationsRepository(components.contextActivity);

        components.loadProcess=new LoadProcess(components.contextActivity,"Carregando Informação ...",false);
        components.btnCancelation=(Button) findViewById(R.id.canceled_confirmed);
        components.router_id=(TextView)findViewById(R.id.router_id);
        components.text_store=(TextView) findViewById(R.id.text_store);
        components.delivery_id=(TextView)findViewById(R.id.delivery_id);
        components.btnReturn=(Button) findViewById(R.id.canceled_last);
        components.reasonJustificationForCancellation=(EditText)findViewById(R.id.reasonJustificationForCancellation);
        components.configModel = new ConfigDataAccess(CancelOperationActivity.this);
        components.userLogged = (Config) components.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));

        components.token = (Config) components.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_TOKEN_JWT));

        components.router_id.setText(String.valueOf(components.selectedRoute.getRouterID()));
        components.text_store.setText(String.valueOf(components.selectedRoute.getStorename()));
        components.delivery_id.setText(String.valueOf(components.selectedRoute.getOperationID()));




        components.btnCancelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnApiResponse onApiResponse=new OnApiResponse() {
                    @Override
                    public void onSucess(Object responseObject) {
                        try{
                            CastResponseApi castResponseApi=(CastResponseApi)responseObject;
                            StringBuffer mensage=new StringBuffer();
                            for(String msn:castResponseApi.getMensage()){
                                mensage.append(msn);
                                mensage.append("\n");
                            }

                            //Cria o gerador do AlertDialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
                            //define o titulo
                            builder.setTitle("Informação");
                            //define a mensagem
                            builder.setMessage(mensage.toString());
                            //define um botão como positivo
                            builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent();
                                    intent.putExtra("result","");
                                    setResult(Constants.UPDATE_OK_ACTIONS,intent);
                                    finish();
                                    if(   components.alerta!=null){
                                        components.alerta.dismiss();
                                    }



                                }
                            });

                            //cria o AlertDialog
                            components.alerta = builder.create();
                            //Exibe
                            components.alerta.show();

                        }catch (Exception e){
                            messageBoxDialog(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Object responseObject) {
                        try{
                            CastResponseApi castResponseApi=(CastResponseApi)responseObject;
                            if(castResponseApi.getState()!=200){
                                StringBuffer mensage=new StringBuffer();
                                for(String msn:castResponseApi.getMensage()){
                                    mensage.append(msn);
                                    mensage.append("\n");
                                }

                                //Cria o gerador do AlertDialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
                                //define o titulo
                                builder.setTitle("Informação");
                                //define a mensagem
                                builder.setMessage(mensage.toString());
                                //define um botão como positivo
                                builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if( components.alerta !=null){
                                            components.alerta.dismiss();
                                        }
                                    }
                                });
                                //cria o AlertDialog
                                components.alerta = builder.create();
                                //Exibe
                                components.alerta.show();
                            }

                        }catch (Exception e){
                            messageBoxDialog(e.getMessage());
                        }finally {

                        }
                    }
                };


                if(components.reasonJustificationForCancellation.getText().toString().trim().length()>0){
                   components.deliveryRepository.
                            finalizePartnerTransaction(
                                    String.valueOf(components.selectedRoute.getOperationID()),
                                    String.valueOf(components.selectedRoute.getParternID()),
                                    components.reasonJustificationForCancellation.getText().toString().trim(),onApiResponse);


                }else{
                    CastResponseApi castResponseApi=new CastResponseApi();
                    castResponseApi.setState(500);
                    castResponseApi.setMensage("Campo de Justificativa Obrigatório.");
                    onApiResponse.onError(castResponseApi);
                }


            }
        });



        components.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Constants.UPDATE_OK_ACTIONS,intent);
                finish();


            }
        });


/*
        Config user = (Config) components.apiConfig.Api.getConfigDataAccess().getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));
        try {
            JSONObject userJson = new JSONObject(user.getDataJson());
            components.uuid = userJson.getString("uuid");
        } catch (Exception e) {
        }
*/

        components.loadProcess.loadActivityClose();



    }

    private OnResponseHttp CallBackOnResponseGlobalApi() {
        return new OnResponseHttp() {
            @Override
            public void onResponse(Object data) {

            }

            @Override
            public void onError(Object error) {

            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_available, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.system_last:
                this.onBackPressed();
                return true;
            case R.id.system_close:

                Intent i = new Intent(getApplicationContext(), LoginAppActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void messageBoxDialog(String mensage) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
        //define o titulo
        builder.setTitle("Informação");
        //define a mensagem
        builder.setMessage(mensage);
        //define um botão como positivo
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if(components.alerta!=null ){
                    components.alerta.dismiss();
                }


            }
        });

        //cria o AlertDialog
        components.alerta = builder.create();
        //Exibe
        components.alerta.show();

    }


    public static class ApiConfig {
        private final AppCompatActivityBase Api;
        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }

    public  static   class Components{
        public OperationModelRepository selectedRoute;
        public ApiConfig   apiConfig;
        public View view;
        public ConfigDataAccess configModel;
        public Config userLogged,token;
        public String uuid;
        public LoadProcess loadProcess;

        public ServerConfiguration conf;
        public ServicesHttp servicesHttp;
        public ConfigurationsUser configurationsAdapter;
        public Object userLoginCast;
        public AppOpeationsRepository deliveryRepository;
        private Context contextActivity;
        private EditText reasonForCancellation;
        private Button btnReturn,btnCancelation;
        private  EditText reasonJustificationForCancellation;
        private TextView text_store,delivery_id,router_id;
        private AlertDialog alerta = null;

        private PartnersServices partnersServices;
        private ProgressDialog pd;

    }
}
