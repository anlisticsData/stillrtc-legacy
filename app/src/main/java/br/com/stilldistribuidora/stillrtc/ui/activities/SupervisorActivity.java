package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.models.OnListClickInteractionListener;
import br.com.stilldistribuidora.stillrtc.db.models.StatisticDispositivo;
import br.com.stilldistribuidora.stillrtc.db.models.SupervisorOperacao;
import br.com.stilldistribuidora.stillrtc.ui.adapters.ListSupervisorAdapter;
import br.com.stilldistribuidora.stillrtc.utils.Const;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Still Technology and Development Team on 07/08/2018.
 */

public class SupervisorActivity extends AppCompatActivity {
    private PrefsHelper prefsHelper;
    private int userID;
    private static List<SupervisorOperacao> ListSupervisor;
    private Context mContext;
    ViewHolder lViewOperacao = new ViewHolder();
    public SupervisorActivity() {
        SupervisorActivity.ListSupervisor = new ArrayList<>();
    }

    private ApiConfig apiConfig;
    public static class ApiConfig{
        private  AppCompatActivityBase Api;
        public   ApiConfig(Context ctx){
            Api = new AppCompatActivityBase(ctx);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiConfig= new ApiConfig(this);
        mContext = this;

        setUI(1);
        prefsHelper = new PrefsHelper(this);
        userID = prefsHelper.getPref(PrefsHelper.PREF_USER_ID);
        //Busca as Informacões no Servidor
        OperationAtivaSupervisor(lViewOperacao, userID);


    }


    public void OperationAtivaSupervisor(final ViewHolder ViewOperacao, int user) {
        // Toast.makeText(SupervisorActivity.this, Constants.CHECK_SERVICE, Toast.LENGTH_SHORT).show();
        // Set up progress before call

        final ProgressDialog progress = new ProgressDialog(mContext);
        progress.setMessage(Constants.CHECK_SERVICE);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgressStyle(0);
        progress.show();
        final List<SupervisorOperacao> operacoes = new ArrayList<>();
        final OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(String tipo, int id) {

                if (tipo.trim().equals("ULTIMA_ROTA")) {


                    SupervisorOperacao supervisorOperacao = operacoes.get(id);
                    Bundle bundle = new Bundle();
                    bundle.putInt("keySuspervisor", Integer.valueOf(supervisorOperacao.operacao_id));
                    bundle.putString("areasLimitsOperation", supervisorOperacao.areaLimitOperation);
                    bundle.putString("idOperation", supervisorOperacao.operacao_id);
                    bundle.putString("operacao_status", supervisorOperacao.statusOperacao);
                    bundle.putString("clientLoja", supervisorOperacao.clienteLoja + " ");
                    bundle.putString("avatar", supervisorOperacao.avatar + " ");
                    bundle.putString("dispositivos", supervisorOperacao.getToStringDispositivo());
                    bundle.putString("dispositivososiosos", supervisorOperacao.getDispositivoOsiosos());

                    if(!supervisorOperacao.getToStringDispositivo().trim().isEmpty() ||
                            !supervisorOperacao.getDispositivoOsiosos().trim().isEmpty()) {

                        Intent intent = new Intent(mContext, NavegarInPoints.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{

                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setTitle("Atenção");
                        alertDialog.setMessage("Não há Atividades dos Dispositivos no Momento.");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Entendi",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();



                    }

                } else {
                    try {


                        SupervisorOperacao supervisorOperacao = operacoes.get(id);
                        Bundle bundle = new Bundle();
                        bundle.putInt("keySuspervisor", Integer.valueOf(supervisorOperacao.operacao_id));
                        bundle.putString("areasLimitsOperation", supervisorOperacao.areaLimitOperation);
                        bundle.putString("idOperation", supervisorOperacao.operacao_id);
                        bundle.putString("clientLoja", supervisorOperacao.clienteLoja + " ");
                        bundle.putString("avatar", supervisorOperacao.avatar + " ");
                        Intent intent = new Intent(mContext, Supervisor_maps.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        ApiEndpointInterface apiService =
                ApiClient.getClient(apiConfig.Api.getEndPointAccess().getDataJson())
                        .create(ApiEndpointInterface.class);
        Call<ResponseBody> call = apiService.getOperationSupervisorGrupo(
                apiConfig.Api.getDeviceUuid().getDataJson(),
                apiConfig.Api.getEndPointAccessToken().getDataJson(),
                apiConfig.Api.getEndPointAccessTokenJwt().getDataJson()
                ,String.valueOf(user));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response != null && response.body() != null) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        if (!obj.getBoolean("error")) {
                            final String operation = obj.getString("operacao");
                            if (!operation.isEmpty()) {
                                setUI(1);
                                try {
                                    JSONArray arrayDados = new JSONArray(operation);
                                    int itensInJsomArray = arrayDados.length();

                                    int statistcInJsomArray = 0;
                                    List<StatisticDispositivo> dispositivoOperation = new ArrayList<StatisticDispositivo>();
                                    String link_avatar_loja = "";
                                    if (itensInJsomArray > 0) {

                                        for (int next = 0; next < itensInJsomArray; next++) {
                                            JSONObject list = arrayDados.getJSONObject(next);
                                            JSONArray arrayDadosAnaliseDispositivo = new JSONArray(list.getString("statusDispositivoEmOperaction"));
                                            statistcInJsomArray = arrayDadosAnaliseDispositivo.length();
                                            for (int nextDispositivo = 0; nextDispositivo < statistcInJsomArray; nextDispositivo++) {
                                                JSONObject analiseDispositivo = arrayDadosAnaliseDispositivo.getJSONObject(nextDispositivo);
                                                dispositivoOperation.add(new StatisticDispositivo(analiseDispositivo.getString("dispositivo"),
                                                        analiseDispositivo.getString("ociosidadeDispositivo"), list.getString("operacaokey")));
                                            }
                                            if (list.getString("lojaavatar") != "null") {
                                                link_avatar_loja = list.getString("lojaavatar");
                                            } else {
                                                link_avatar_loja = "";
                                            }
                                            operacoes.add(new SupervisorOperacao(list.getString("operacaokey"), link_avatar_loja,
                                                    list.getString("loja"), list.getString("cliente"),
                                                    list.getInt("quantidadeDispositivo"),
                                                    list.getString("areaLimitOperation"),
                                                    dispositivoOperation, list.getString("statusoperacao")));
                                        }
                                    }
                                    SupervisorActivity.ListSupervisor = operacoes;


                                } catch (Exception e) {

                                }
                            }

                            ListSupervisorAdapter supervisorApapter = new ListSupervisorAdapter(operacoes, listener);
                            ViewOperacao.recyclerViewOperacao.setAdapter(supervisorApapter);
                            LinearLayoutManager linerlayoutmaneger = new LinearLayoutManager(SupervisorActivity.this);
                            ViewOperacao.recyclerViewOperacao.setLayoutManager(linerlayoutmaneger);
                        } else {
                            setUI(2);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }


                progress.dismiss();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progress.dismiss();

            }
        });


    }


    public interface ProgressBar {
        public void onProgressUpdate(ProgressDialog barra, int percentage);
    }

    private void setUI(int i) {
        if (i == 1) {
            setContentView(R.layout.activity_supervisor);
            lViewOperacao.recyclerViewOperacao = (RecyclerView) findViewById(R.id.recycler_view_supervisor);


        } else {
            setContentView(R.layout.row_supervisor_empty);
            TextView txt_detalhe = (TextView) findViewById(R.id.tvTitle);
            txt_detalhe.setText(getResources().getString(R.string.str_operation_empty));

        }
        setShowToolbar();
    }


    public void setShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.supervisor_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //habilitando Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_supervisor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Acoões do Menu
        if (item.getItemId() == R.id.action_close_app) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Const.FINALIZA_APP);
            builder.setCancelable(true);
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(SupervisorActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();


        } else if (item.getItemId() == R.id.action_refresh) {


            //Check e traz as Operações..
            OperationAtivaSupervisor(lViewOperacao, userID);


        }


        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    private static class ViewHolder {
        //Classe Para o List RecycleView
        RecyclerView recyclerViewOperacao;

    }


}
