package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;
import br.com.stilldistribuidora.stillrtc.db.models.Store;
import br.com.stilldistribuidora.stillrtc.interfac.IClickListSimultaneas;
import br.com.stilldistribuidora.stillrtc.services.ServiceApp;
import br.com.stilldistribuidora.stillrtc.ui.adapters.OperationAdapterSimultaneas;
import br.com.stilldistribuidora.stillrtc.utils.Const;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Still Technology and Development Team on 29/01/2019.
 */

public class OpslistagenActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{



    private Toolbar toolbar;
    private int userID;
    private static final String TAG = OpslistagenActivity.class.getSimpleName();
    private PrefsHelper prefsHelper;
    private ProgressDialog progress;
    private String Tipo="P";

    /* list pictures incidents */

    private RecyclerView mRecyclerView;
    private OperationAdapterSimultaneas adapter;
    private ArrayList<Operation> ar;
    private LinearLayoutManager linearlayoutManager;
    private Boolean error = false;
    private boolean empty = false;
    private int pos;

    private String deviceIdentifier;

    private Store.Result delivery;
    private int position;

    private Button btnDate;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int Year, Month, Day;
    private String currenDate;
    private IClickListSimultaneas onActionSelects;
    public    ArrayList<String> operacoesSelecionadas;

    private Boolean habilitarBotao=false;
    MenuItem save_btn;



    private ApiConfig appConfig;
    public static class ApiConfig{
        private  AppCompatActivityBase Api;
        public   ApiConfig(Context ctx){
            Api = new AppCompatActivityBase(ctx);
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        appConfig=new ApiConfig(this);
        setContentView(R.layout.activity_ops_list);
        this.toolbar =(Toolbar)findViewById(R.id.supervisor_toolbar);
        this.toolbar.setTitle(R.string.ops_simultaneas);
        setSupportActionBar(this.toolbar); //diz para o android executar as ações do menu
        this.operacoesSelecionadas = new ArrayList<>();

        onActionSelects =  new IClickListSimultaneas() {
            @Override
            public void initOperacoesSelecionadas(ArrayList<String> s) {
                 //Toast.makeText(OpslistagenActivity.this, "dfd2222222222222sfsdf", Toast.LENGTH_SHORT).show();
                 operacoesSelecionadas.clear();
                 operacoesSelecionadas.addAll(s);


                 if(operacoesSelecionadas.size() > 1){
                     save_btn.setVisible(true);
                 }else{
                     save_btn.setVisible(false);
                 }





            }
        };




        prefsHelper = new PrefsHelper(this);
        deviceIdentifier = String.valueOf(prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER));
        initUI();
        listenerUI();
        currenDate = DateUtils.currentDateOnly();
        // inicia serviço de envio de imagens e operações
        startService(new Intent(this, ServiceApp.class));
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //habilitando Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_simultaneas, menu);
        save_btn = (MenuItem) menu.findItem(R.id.action_refresh);

        menu.getItem(0).setVisible(false); // here pass the index of save menu item
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i("###",item.toString());

        //Acoões do Menu
        if (item.getItemId() == R.id.action_close_app) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Const.FINALIZA_APP);
            builder.setCancelable(true);
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(OpslistagenActivity.this, LoginActivity.class);
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


        }else if (item.getItemId() == R.id.action_refresh) {


            System.out.println(operacoesSelecionadas);


            //Toast.makeText(this, "dfdsfsdf", Toast.LENGTH_SHORT).show();

        }



        return true;
    }



    public void initUI() {
        // [START]
        try {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_simultaneas);
        btnDate = (Button) findViewById(R.id.btnDate);
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = DatePickerDialog.newInstance(OpslistagenActivity.this, Year, Month, Day);
        }catch (Exception e){

        }
    }


    public void listItensOperation(String identifier, String date) {

      //  Toast.makeText(this,Tipo,Toast.LENGTH_LONG).show();

        //showProgress();

        ApiEndpointInterface apiService =
                ApiClient.getClient(appConfig.Api.getEndPointAccess().getDataJson())
                        .create(ApiEndpointInterface.class);

        Call<Operation.Result> call = apiService.
                recuperarOperacoesPorIdentificador(
                        appConfig.Api.getDeviceUuid().getDataJson(),
                        appConfig.Api.getEndPointAccessToken().getDataJson(),
                        appConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                        identifier, date);


        call.enqueue(new Callback<Operation.Result>() {
            @Override
            public void onResponse(Call<Operation.Result> call, Response<Operation.Result> response) {

                try {

                    List<Operation> movies = response.body().ar;

                    if (movies.size() > 0) {
                        empty = false;
                        for (Operation deliveryFragment : movies) {
                            ar.add(deliveryFragment);
                        }
                    } else {
                        empty = true;
                        Operation deliveryFragment = new Operation();
                        deliveryFragment.setAreaName(getString(R.string.alert_info_no_delivery));
                        ar.add(deliveryFragment);
                    }
                    recyclerViewDevelop();

                } catch (Exception e) {
                    e.printStackTrace();

                    empty = true;
                    Operation deliveryFragment = new Operation();
                    deliveryFragment.setAreaName(getString(R.string.alert_info_no_delivery));
                    ar.add(deliveryFragment);

                    recyclerViewDevelop();
                }

            }

            @Override
            public void onFailure(Call<Operation.Result> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void recyclerViewDevelop() {

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        linearlayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(linearlayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Create the recyclerViewAdapter

        adapter = new OperationAdapterSimultaneas(this, ar, empty,onActionSelects);

        mRecyclerView.setAdapter(adapter);

       // dimissProgress();
    }


    public void listenerUI() {
        ar = new ArrayList<>();
        btnDate.setText(DateUtils.currentDate());
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                //datePickerDialog.setAccentColor(Color.parseColor("#009688"));
                datePickerDialog.setTitle(getString(R.string.str_title_datepicker));
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {

            listItensOperation(deviceIdentifier, currenDate);
        }


    }





    public void showProgress() {


        Toast.makeText(this,R.string.alert_info_loading_operations,Toast.LENGTH_LONG ).show();

/*
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.alert_info_loading_operations));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        */

    }

    public void dimissProgress() {
        //  progress.dismiss();

        Toast.makeText(this,"Operação Completa..!",Toast.LENGTH_LONG ).show();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = String.valueOf(monthOfYear + 1);
       currenDate = year + "-" + month + "-" + dayOfMonth;
        btnDate.setText(DateUtils.convertDatetimeStringInDate(year + "-" + month + "-" + dayOfMonth + " 00:00:00"));
        setBuildList(deviceIdentifier, currenDate);
    }

    public void setBuildList(String userId, String dateSearch) {
        operacoesSelecionadas.clear();
        save_btn.setVisible(false);
        if (adapter != null) {
            ar.clear();
            listItensOperation(userId, dateSearch);
            adapter.notifyDataSetChanged();
        } else {
            listItensOperation(userId, dateSearch);
        }




    }


}
