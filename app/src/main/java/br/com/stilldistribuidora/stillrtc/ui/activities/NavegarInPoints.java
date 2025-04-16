package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.models.OnListClickInteractionListener;
import br.com.stilldistribuidora.stillrtc.db.models.PointsInMap;
import br.com.stilldistribuidora.stillrtc.ui.adapters.ListAdapterPoints;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Still Technology and Development Team on 17/08/2018.
 */

public class NavegarInPoints extends AppCompatActivity {
    ViewHolder viewHolderPoints = new ViewHolder();
    private static List<PointsInMap> pointInMaps = new ArrayList<>();
    private String name = "Navegar...";
    private PrefsHelper prefsHelper;
    private int userID;
    private String operationKey;
    private String avatar;
    private String dispositivos;
    private String ultimoPing;
    private Context mContext;
    private String dispositivososiosos;
    private List<String> listDispositivo;
    private List<String> listDispositivoOsiosos;
    private String operacao_status;
    private ApiConfig apiConfig;
    public static class ApiConfig{
        private  AppCompatActivityBase Api;
        public   ApiConfig(Context ctx){
            Api = new AppCompatActivityBase(ctx);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiConfig =new ApiConfig(getApplicationContext());
        mContext = this;



        listDispositivo = new ArrayList<>();
        listDispositivoOsiosos = new ArrayList<>();
        setUI(1);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        operationKey = bundle.getString("idOperation");
        avatar = bundle.getString("avatar");
        dispositivos = bundle.getString("dispositivos");
        dispositivososiosos = bundle.getString("dispositivososiosos");
        operacao_status = bundle.getString("operacao_status");
        Collections.addAll(listDispositivo, dispositivos.split(","));
        Collections.addAll(listDispositivoOsiosos, dispositivososiosos.split(","));


        int sizeDispOsioso = listDispositivo.size();
        dispositivos = "";
        for (int next = 0; next < sizeDispOsioso; next++) {
            if (listDispositivo.get(next).matches("[0-9]+")) {
                dispositivos += listDispositivo.get(next);
                if (next < (sizeDispOsioso - 1)) {
                    dispositivos += "|";
                }

            } else {
                sizeDispOsioso--;
                dispositivos = dispositivos.substring(0, (dispositivos.length() - 1));

            }
        }
        prefsHelper = new PrefsHelper(this);
        userID = prefsHelper.getPref(PrefsHelper.PREF_USER_ID);
        getUltimaPosicaoPointOperation(operationKey, operacao_status, dispositivos);


    }





    public void getUltimaPosicaoPointOperation(String operationKey, final String operacao_status, final String dispositivo) {
        final ProgressDialog progress = new ProgressDialog(mContext);
        progress.setMessage("Montando Lista.! Aguarde...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgressStyle(1);
        progress.show();

        ApiEndpointInterface apiService =
                ApiClient.getClient( apiConfig.Api.getEndPointAccess().getDataJson()).create(ApiEndpointInterface.class);
        final Call<ResponseBody> call = apiService.getOperationSupervisorUltimoPing(
                apiConfig.Api.getDeviceUuid().getDataJson(),
                apiConfig.Api.getEndPointAccessToken().getDataJson(),
                apiConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                operationKey, dispositivo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        final String operation = obj.getString("operacao");
                        if (!obj.getBoolean("error")) {
                            if (!operation.isEmpty()) {
                                setUI(1);
                                JSONArray arrayDados = new JSONArray(operation);
                                int itensInJsomArray = arrayDados.length();
                                int statistcInJsomArray = 0;
                                List<PointsInMap> dispositivoOperation = new ArrayList<>();
                                String link_avatar_loja = "";
                                if (itensInJsomArray > 0) {
                                    Boolean isPointsUltping = false;

                                    for (int next = 0; next < itensInJsomArray; next++) {
                                        isPointsUltping = true;
                                        JSONObject list = arrayDados.getJSONObject(next);
                                        dispositivoOperation.add(new PointsInMap(list.getString("Dispositivo"),
                                                list.getString("ultPoints"), listDispositivoOsiosos, listDispositivo, operacao_status));
                                    }
                                    //Criando Rota
                                    pointInMaps = dispositivoOperation;
                                    //Criando Rota
                                    onActionIntefacePoints();
                                    if (!isPointsUltping) {
                                        setUI(2);
                                    }
                                }

                            }

                        } else {
                            setUI(2);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    setUI(2);
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progress.dismiss();

            }
        });


    }


    private void onActionIntefacePoints() {

        OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(String tipo, int id) {

                //Toast.makeText(NavegarInPoints.this,R.string.str_navegar,Toast.LENGTH_SHORT).show();
                PointsInMap Pontos = pointInMaps.get(id);

                if (tipo.trim().equals("MAPS")) {
                    navegarViaGps(Pontos.dispositivoUltimaLocalizacao, "1");
                } else {
                    navegarViaGps(Pontos.dispositivoUltimaLocalizacao, "2");

                }


            }
        };

        ListAdapterPoints poitsAdapter = new ListAdapterPoints(pointInMaps, listener);
        this.viewHolderPoints.recyclerViewPoints.setAdapter(poitsAdapter);
        LinearLayoutManager linearLayoutManeger = new LinearLayoutManager(this);
        this.viewHolderPoints.recyclerViewPoints.setLayoutManager(linearLayoutManeger);
    }


    private void setUI(int i) {
        if (i == 1) {
            setContentView(R.layout.activity_navegar_points);
            this.viewHolderPoints.recyclerViewPoints = (RecyclerView) this.findViewById(R.id.sup_pointsInMaps);

        } else {
            setContentView(R.layout.row_supervisor_empty);
            TextView txt_detalhe = (TextView) findViewById(R.id.tvTitle);
            txt_detalhe.setText(getResources().getString(R.string.str_operation_empty_points));

        }
        setShowToolbar();
    }


    public void setShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.supervisor_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //showCurrentPlace();
            onBackPressed();

        }
        return true;
    }


    public void navegarViaGps(String Coordenadas, String TipoRecurso) {

        Log.i("NET_", Coordenadas + "   " + TipoRecurso);
        String uri = "";
        Intent intent;
        try {
            if (TipoRecurso.equals("1")) {
                try {
                    Uri mapAppUri = Uri.parse("geo:" + Coordenadas + "?q=" + Coordenadas + "(" + Uri.encode("Ir até...") + ")");
                    String url = "geo:" + Coordenadas;
                    intent = new Intent(Intent.ACTION_VIEW, mapAppUri);
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // If Google is not installed, open it in Google Play:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=apps.maps"));
                    startActivity(intent);
                }

            } else {

                try {
                    String url = "waze://?ll=" + Coordenadas + "&navigate=yes";
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);

                } catch (ActivityNotFoundException ex) {
                    // If Waze is not installed, open it in Google Play:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                    startActivity(intent);
                }
            }

        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Log.i("G", innerEx.getMessage());
                Toast.makeText(NavegarInPoints.this, "Wazer não Instalado do Celular..!", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private static class ViewHolder {
        RecyclerView recyclerViewPoints;

    }


}
