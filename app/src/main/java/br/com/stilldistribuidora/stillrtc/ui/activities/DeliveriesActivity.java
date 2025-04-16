package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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
import br.com.stilldistribuidora.stillrtc.ui.adapters.OperationAdapter;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Still Technology and Development Team on 23/04/2017.
 */

public class DeliveriesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

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

    private Store.Result delivery;
    private int position;

    private Button btnDate;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int Year, Month, Day;
    private String currenDate;
    private ApiConfig appConfig;



    public static class ApiConfig{
        private AppCompatActivityBase Api;
        public   ApiConfig(Context ctx){
            Api = new AppCompatActivityBase(ctx);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);
        appConfig=new ApiConfig(this);

        try {
            Bundle extras = getIntent().getExtras();
            delivery = (Store.Result) getIntent().getSerializableExtra("object");
            position = extras.getInt("position");

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        prefsHelper = new PrefsHelper(this);
        deviceIdentifier = String.valueOf(prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER));
        setToolbar();
        initUI();
        listenerUI();

        currenDate = DateUtils.currentDateOnly();

    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initUI() {
        // [START]
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        btnDate = (Button) findViewById(R.id.btnDate);
        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = DatePickerDialog.newInstance(DeliveriesActivity.this, Year, Month, Day);
    }

    /**
     * Handles a click on the menu option to get a place.
     *
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
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

    @Override
    protected void onResume() {
        super.onResume();

        setBuildList(delivery.ar.get(position).getId(), currenDate);

    }

    public void listDeliveryFragments(int identifier, String dateSearch) {
        if(Utils.isNetworkAvailable(DeliveriesActivity.this) && Utils.isOnline()) {

            showProgress();

        }

        ApiEndpointInterface apiService =
                ApiClient.getClient(
                        appConfig.Api.getEndPointAccess().getDataJson()
                ).create(ApiEndpointInterface.class);

        Call<Operation.Result> call = apiService.getAllDeliveriesBy(
                appConfig.Api.getDeviceUuid().getDataJson(),
                appConfig.Api.getEndPointAccessToken().getDataJson(),
                appConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                identifier, dateSearch);

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

        dimissProgress();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = String.valueOf(monthOfYear + 1);

        currenDate  = year + "-" + month + "-" + dayOfMonth;

        btnDate.setText(DateUtils.convertDatetimeStringInDate(year + "-" + month + "-" + dayOfMonth + " 00:00:00"));

        setBuildList(delivery.ar.get(position).getId(), currenDate);
    }

    public void setBuildList(int userId, String dateSearch) {
        if (adapter != null) {
            ar.clear();
            listDeliveryFragments(userId, dateSearch);
            adapter.notifyDataSetChanged();
        } else {
            listDeliveryFragments(userId, dateSearch);
        }
    }
}