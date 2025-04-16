package br.com.stilldistribuidora.partners.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.stilldistribuidora.common.CastResponseApi;
import br.com.stilldistribuidora.httpService.partners.PartnersServices;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.db.models.MyRouterSerialized;
import br.com.stilldistribuidora.stillrtc.interfac.OnApiResponse;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;

public class PartnersFinishOperationActivity extends AppCompatActivity {
    private  static Components componentsStatics=new Components();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners_finish_operation);
        Button btnFinish = (Button) findViewById(R.id.btnFinish);
        Intent i = getIntent();
        componentsStatics.self=this;
        componentsStatics.selectedRoute = (MyRouterSerialized) i.getSerializableExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE);
        componentsStatics.apiConfig =new ApiConfig(this);
        componentsStatics.configModel = new ConfigDataAccess(PartnersFinishOperationActivity.this);
        componentsStatics.userLogged = (Config) componentsStatics.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));
        componentsStatics.token = (Config) componentsStatics.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_TOKEN_JWT));






        componentsStatics.partnersServices = new PartnersServices(
                new ConfigDataAccess(componentsStatics.self), componentsStatics.userLogged,
                componentsStatics.apiConfig.Api.getEndPointAccess(), componentsStatics.token,
                componentsStatics.apiConfig.Api.getEndPointAccessToken());


        componentsStatics.onApiResponse =new OnApiResponse() {
            @Override
            public void onSucess(Object responseObject) {

                CastResponseApi castResponseApi=(CastResponseApi)responseObject;
                StringBuffer mensage=new StringBuffer();
                for(String msn:castResponseApi.getMensage()){
                    mensage.append(msn);
                    mensage.append("\n");
                }

                //Cria o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(componentsStatics.self);
                //define o titulo
                builder.setTitle("Informação");
                //define a mensagem
                builder.setMessage(mensage);
                //define um botão como positivo
                builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE,componentsStatics.selectedRoute);
                        setResult(Constants.FINISH_OPERATION_OK_ACTIONS,intent);
                        finish();
                    }
                });
                //cria o AlertDialog
                componentsStatics.alerta = builder.create();
                //Exibe
                componentsStatics.alerta.show();




            }

            @Override
            public void onError(Object responseObject) {
                System.out.println("#4 ");

                StringBuffer mensage = new StringBuffer();
                CastResponseApi castResponseApi=(CastResponseApi)responseObject;
                if(castResponseApi.getState()!=200) {
                    for (String msn : castResponseApi.getMensage()) {
                        mensage.append(msn);
                        mensage.append("\n");
                    }
               }
                showBoxAlert(mensage.toString());
            }
        };






        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                componentsStatics.partnersServices.
                        usersSetOperationStatus(componentsStatics.selectedRoute.getRouter_id(),
                                componentsStatics.selectedRoute.getOperation(),"4",
                               "Finalizado Pelo Usuario",componentsStatics.onApiResponse);
            }
        });


        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }

    private void showBoxAlert(String mensage) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(componentsStatics.self);
        //define o titulo
        builder.setTitle("Informação");
        //define a mensagem
        builder.setMessage(mensage);
        //define um botão como positivo
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                componentsStatics.alerta.dismiss();
            }
        });
        //cria o AlertDialog
        componentsStatics.alerta = builder.create();
        //Exibe
        componentsStatics.alerta.show();

    }

    public static class ApiConfig {
        private final AppCompatActivityBase Api;
        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }

    public  static   class Components{
        public MyRouterSerialized selectedRoute;
        public ApiConfig apiConfig;
        public View view;
        public ConfigDataAccess configModel;
        public Config userLogged,token;
        public OnApiResponse onApiResponse;
        private Context self;
        private EditText reasonForCancellation;
        private Button btnReturn,btnCancelation;
        private  EditText reasonJustificationForCancellation;
        private TextView text_store,delivery_id,router_id;
        private AlertDialog alerta = null;

        private PartnersServices partnersServices;
        private ProgressDialog pd;

    }

}