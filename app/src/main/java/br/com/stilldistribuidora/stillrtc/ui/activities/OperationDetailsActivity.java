package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.business.OperationBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.PictureBusiness;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;
import br.com.stilldistribuidora.stillrtc.db.models.Picture;
import br.com.stilldistribuidora.stillrtc.ui.adapters.PictureAdapter;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;

/**
 * Created by Still Technology and Development Team on 23/04/2017.
 */

public class OperationDetailsActivity extends AppCompatActivity {

    private static final String TAG = OperationDetailsActivity.class.getSimpleName();

    private String titleBar = "";
    private int deliveryId;


    /* */
    private OperationBusiness dfBusiness;
    private PictureBusiness pictureBusiness;
    private String dIdentifier;

    SimpleDateFormat myFormatHour = new SimpleDateFormat("HH:mm:ss", Locale.US);
    SimpleDateFormat myFormatDate = new SimpleDateFormat(DateUtils.FORMAT_DATE_TIME_ZONE, Locale.US);

    /* list pictures incidents */
    private RecyclerView mRecyclerView;
    private PictureAdapter adapter;
    private ArrayList<Picture> ar = new ArrayList<>();
    private LinearLayoutManager linearlayoutManager;
    private Boolean error = false;
    private boolean empty = false;
    private int pos;

    private PrefsHelper prefsHelper;

    private int deviceId;

    private Operation operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_delivery_fragments);

        prefsHelper = new PrefsHelper(this);
        deviceId = prefsHelper.getPref(PrefsHelper.PREF_DEVICE_ID);

        try {
            Bundle extras = getIntent().getExtras();
            Operation.Result phraseReult = (Operation.Result) getIntent().getSerializableExtra("object");
            int position = extras.getInt("position");
            operation = phraseReult.ar.get(position);

            titleBar = operation.getAreaName();
            deliveryId = operation.getDeliveryId();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        pictureBusiness = new PictureBusiness(this);
        dfBusiness = new OperationBusiness(this);

        setToolbar(titleBar);
        initUI();
        listenerUI();

    }

    public void setToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(title);
    }

    public void initUI() {

        // [START] pictures
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        try {

            TextView tvDate = (TextView) findViewById(R.id.tvDate);
            TextView tvStart = (TextView) findViewById(R.id.tvStart);
            TextView tvEnd = (TextView) findViewById(R.id.tvEnd);
            TextView tvTimeAll = (TextView) findViewById(R.id.tvTimeAll);
            TextView tvQntPic = (TextView) findViewById(R.id.tvPictures);
            //TextView tvKm = (TextView) findViewById(R.id.tvKm);


            OperationBusiness operationBusiness = new OperationBusiness(this);

            List<Operation> list = (ArrayList<Operation>) operationBusiness.getList("","");

            Operation op = (Operation) operationBusiness.getById( Constants.KEY_DELIVERY_FRAGMENT_ID+ "="+operation.getDeliveryId());
            if(op!=null){

                Date newDate = myFormatDate.parse(op.getCreatedAt());
                SimpleDateFormat format = new SimpleDateFormat("dd MMM,yyyy hh:mm", Locale.ENGLISH);
                String date = format.format(newDate);
                tvDate.setText(date);

                tvTimeAll.setText(DateUtils.compararDataRetornarMinutos(op.getDatetimeStart(), op.getDatetimeEnd()));

                tvStart.setText(String.valueOf(myFormatHour.format(myFormatDate.parse(op.getDatetimeStart()))));
                tvEnd.setText(String.valueOf(myFormatHour.format(myFormatDate.parse(op.getDatetimeEnd()))));
                tvQntPic.setText(String.valueOf(getQntPicturesByFragment()));
            } else {
                finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listenerUI() {
        listPictures();
        recyclerViewDevelop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private int getQntPicturesByFragment() {
        return pictureBusiness.getList(Constants.KEY_DELIVERY_ID + "=" + deliveryId +" AND "+Constants.KEY_DEVICE_ID + "="+deviceId, "").size();
    }

    /**
     * This method is set recycler view develop
     */
    private void recyclerViewDevelop() {

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        linearlayoutManager = new GridLayoutManager(this, 4);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(linearlayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Create the recyclerViewAdapter
        adapter = new PictureAdapter(this, ar, empty);

        mRecyclerView.setAdapter(adapter);

    }

    public void listPictures() {
        ar = (ArrayList<Picture>) pictureBusiness.getList(Constants.KEY_DELIVERY_ID + "=" + deliveryId, "");
    }
}