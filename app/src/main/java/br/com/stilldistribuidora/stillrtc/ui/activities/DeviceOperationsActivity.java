package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;
import br.com.stilldistribuidora.stillrtc.services.ServiceApp;
import br.com.stilldistribuidora.stillrtc.ui.adapters.OperationAdapter;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Still Technology and Development Team on 23/04/2017.
 */

public class DeviceOperationsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DeviceOperationsActivity.class.getSimpleName();
    private PrefsHelper prefsHelper;

    /* list pictures incidents */

    private RecyclerView mRecyclerView;
    private OperationAdapter adapter;
    private ArrayList<Operation> ar;
    private LinearLayoutManager linearlayoutManager;
    private Boolean error = false;
    private boolean empty = false;
    private int pos;

    private String deviceIdentifier;

    private Button btnDate;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int Year, Month, Day;
    private String currenDate;
    private  ApiConfig appConfig;


    public static class ApiConfig{
        private  AppCompatActivityBase Api;
        public   ApiConfig(Context ctx){
            Api = new AppCompatActivityBase(ctx);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appConfig = new ApiConfig(this);
        setContentView(R.layout.activity_operations);

        prefsHelper = new PrefsHelper(this);
        deviceIdentifier = String.valueOf(prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER));
        setToolbar();
        initUI();
        listenerUI();

        currenDate = DateUtils.currentDateOnly();

        // inicia serviço de envio de imagens e operações
        startService(new Intent(this, ServiceApp.class));
    }


    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initUI() {
        // [START]
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        btnDate = (Button) findViewById(R.id.btnDate);
        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = DatePickerDialog.newInstance(DeviceOperationsActivity.this, Year, Month, Day);


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

    private ProgressDialog progress;

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




    public void listItensOperation(String identifier, String date) {

      //  showProgress();



        Toast.makeText(this,R.string.alert_info_loading_operations,Toast.LENGTH_LONG ).show();

        ApiEndpointInterface apiService =
                ApiClient.getClient(appConfig.Api.getEndPointAccess().getDataJson()).create(ApiEndpointInterface.class);

        Call<Operation.Result> call = apiService.recuperarOperacoesPorIdentificador(
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

    /**
     * This method is set recycler view develop
     */
    private void recyclerViewDevelop() {

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        linearlayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(linearlayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Create the recyclerViewAdapter
        adapter = new OperationAdapter(this, ar, empty);

        mRecyclerView.setAdapter(adapter);

        //dimissProgress();
        Toast.makeText(this,"Listagem Carregado..!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = String.valueOf(monthOfYear + 1);

        currenDate = year + "-" + month + "-" + dayOfMonth;

        btnDate.setText(DateUtils.convertDatetimeStringInDate(year + "-" + month + "-" + dayOfMonth + " 00:00:00"));

        setBuildList(deviceIdentifier, currenDate);
    }

    public void setBuildList(String userId, String dateSearch) {
        if (adapter != null) {
            ar.clear();
            listItensOperation(userId, dateSearch);
            adapter.notifyDataSetChanged();
        } else {
            listItensOperation(userId, dateSearch);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {
            case R.id.action_change_password:
                alertChangePassword();
                break;
            case R.id.action_logout:
                prefsHelper.delete(PrefsHelper.PREF_DEVICE_PWD);
                prefsHelper.delete(PrefsHelper.PREF_IS_LOGINED);
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alertChangePassword() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle(getString(R.string.btn_change_password));
        LayoutInflater inflater = this.getLayoutInflater();
        //this is what I did to added the layout to the alert dialog
        View layout = inflater.inflate(R.layout.dialog_change_password, null);
        alert.setView(layout);

        final EditText pwdCurrent = (EditText) layout.findViewById(R.id.et_pwd_current);
        final EditText etNewPwd = (EditText) layout.findViewById(R.id.et_new_pwd);
        final EditText etRepeatPwd = (EditText) layout.findViewById(R.id.et_repeat_pwd);
        final TextView tvResponse = (TextView) layout.findViewById(R.id.tv_response);
        Button btnCancel = (Button) layout.findViewById(R.id.btnCancel);
        Button btnChangePassword = (Button) layout.findViewById(R.id.btnChangePassword);

        final AlertDialog alertDialog = alert.show();


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPwd = pwdCurrent.getText().toString();
                String newPwd = etNewPwd.getText().toString();
                final String repeatPwd = etRepeatPwd.getText().toString();
                if (currentPwd.length() > 0 && newPwd.length() > 0 && repeatPwd.length() > 0) {
                    if (!newPwd.equals(repeatPwd)) {
                        tvResponse.setVisibility(View.VISIBLE);
                        tvResponse.setText(getString(R.string.str_info_pws_not_match));
                    } else {
                        tvResponse.setVisibility(View.GONE);

                        ApiEndpointInterface apiService =
                                ApiClient.getClient(
                                        appConfig.Api.getEndPointAccess().getDataJson()
                                ).create(ApiEndpointInterface.class);

                        String uuid = prefsHelper.getPref(PrefsHelper.PREF_DEVICE_UUID).toString();
                        Call<ResponseBody> call = apiService.alterarSenhaDispositivoAPI(
                                appConfig.Api.getDeviceUuid().getDataJson(),
                                appConfig.Api.getEndPointAccessToken().getDataJson(),
                                appConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                                uuid, currentPwd, newPwd);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response != null) {

                                    try {
                                        JSONObject obj = new JSONObject(response.body().string());

                                        if (Boolean.parseBoolean(obj.getString("error"))) {

                                            tvResponse.setVisibility(View.VISIBLE);
                                            tvResponse.setText(obj.getString("message"));

                                        } else {

                                            prefsHelper.delete(PrefsHelper.PREF_DEVICE_PWD);
                                            prefsHelper.delete(PrefsHelper.PREF_IS_LOGINED);
                                            finish();
                                        }
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.wtf(TAG, "Não esta salvando as coordenadas:" + t.toString());
                            }
                        });
                    }

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    public static void changePasswordDevice(String pwdCurrent, String pwdNew) {

//        ApiEndpointInterface apiService =
//                ApiClient.getClient().create(ApiEndpointInterface.class);
//
//        Call<ResponseBody> call = apiService.alterarSenhaDispositivoAPI(pwdCurrent, pwdNew);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if (response != null) {
//
//                    Log.wtf(TAG, "Operação finalizada");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.wtf(TAG, "Não esta salvando as coordenadas:"+t.toString());
//            }
//        });
    }
}