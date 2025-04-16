package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.models.Store;
import br.com.stilldistribuidora.stillrtc.ui.adapters.StoreAdapter;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Still Technology and Development Team on 23/04/2017.
 */

public class StoresActivity extends AppCompatActivity {

    private static final String TAG = StoresActivity.class.getSimpleName();
    private PrefsHelper prefsHelper;

    /* list pictures incidents */
    private RecyclerView mRecyclerView;
    private StoreAdapter adapter;
    private ArrayList<Store> ar;
    private LinearLayoutManager linearlayoutManager;
    private Boolean error = false;
    private boolean empty = false;
    private int pos;
    private int userID;
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
        setContentView(R.layout.activity_stores);
        appConfig = new ApiConfig(this);

        prefsHelper = new PrefsHelper(this);
        setToolbar();
        initUI();
        listenerUI();

        userID = prefsHelper.getPref(PrefsHelper.PREF_USER_ID);


    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initUI() {
        // [START]
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    public void listenerUI() {
        ar = new ArrayList<>();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            getAllStoresByClient(userID);
        }

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
        adapter = new StoreAdapter(this, ar, empty);

        // divider
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                linearlayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setAdapter(adapter);


    }

    public void getAllStoresByClient(int client_id) {


        ApiEndpointInterface apiService =
                ApiClient.getClient(
                        appConfig.Api.getEndPointAccess().getDataJson()
                ).create(ApiEndpointInterface.class);

        Call<Store.Result> call = apiService.getAllStoreByClient(
                appConfig.Api.getDeviceUuid().getDataJson(),
                appConfig.Api.getEndPointAccessToken().getDataJson(),
                appConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                client_id, 1, "");

        call.enqueue(new Callback<Store.Result>() {
            @Override
            public void onResponse(Call<Store.Result> call, Response<Store.Result> response) {

                try {

                    List<Store> movies = response.body().ar;

                    if (movies.size() > 0) {
                        empty = false;
                        for (Store deliveryFragment : movies) {
                            ar.add(deliveryFragment);

                        }
                    } else {
                        empty = true;
                        Store store = new Store();
                        store.setName(getString(R.string.alert_info_client_no_stores));
                        ar.add(store);
                    }
                    recyclerViewDevelop();

                } catch (Exception e) {
                    e.printStackTrace();

                    empty = true;
                    Store store = new Store();
                    store.setName(getString(R.string.alert_info_client_no_stores));
                    ar.add(store);

                    recyclerViewDevelop();
                }
            }

            @Override
            public void onFailure(Call<Store.Result> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}