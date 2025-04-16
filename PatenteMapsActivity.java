package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.business.OperationBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.PictureBusiness;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;
import br.com.stilldistribuidora.stillrtc.db.models.Picture;
import br.com.stilldistribuidora.stillrtc.db.models.Zone;
import br.com.stilldistribuidora.stillrtc.services.ServiceApp;
import br.com.stilldistribuidora.stillrtc.services.ServiceDeviceStatus;
import br.com.stilldistribuidora.stillrtc.ui.adapters.PictureAdapter;
import br.com.stilldistribuidora.stillrtc.utils.Const;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.DialogResultListener;
import br.com.stilldistribuidora.stillrtc.utils.Dialogs;
import br.com.stilldistribuidora.stillrtc.utils.PermissManager;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import br.com.stilldistribuidora.stillrtc.utils.UploadFile;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener, LocationListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_AND_COARSE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    public static double lastLat;
    public static double lastLng;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private ImageButton ibtnPicture;
    private ImageButton ibtnAllPicture;
    private Button btnStart;
    private Button btnStop;
    private Button btnSave;
    private Chronometer chronometer;
    private ViewFlipper viewSwitcher;
    private boolean clickStart = false;

    SimpleDateFormat myFormatHour = new SimpleDateFormat("HH:mm:ss", Locale.US);
    SimpleDateFormat myFormatDate = new SimpleDateFormat(DateUtils.FORMAT_DATE_TIME_ZONE, Locale.US);
    private String dataInicioOperacao, dataHoraInicioOperacao;
    private String dataFimOperacao, dataHoraFimOperacao;

    /* These variables are meant to images */
    private File mFileTemp;
    private UploadFile uploadFile;


    /* list pictures incidents */
    private RecyclerView mRecyclerView;
    private PictureAdapter adapter;
    private ArrayList<Picture> ar;
    private LinearLayoutManager linearlayoutManager;
    private Boolean error = false;
    private boolean empty = false;
    private int pos;

    private PictureBusiness pictureBusiness;

    private Operation.Result phraseReult;
    private int position;
    private String titleBar = "";
    private int deliveryID;


    /* */
    private OperationBusiness dfBusiness;

    private int deviceId;

    private PrefsHelper prefsHelper;

    private LinearLayout llPictures;

    private Intent intentService;

    //private KmlLayer kmlLayer;
    ArrayList<Operation.KMLS> ids = new ArrayList<>();

    String nameFile;

    int deliveryFragmentId;
    String deviceIdentifier;

    NotificationCompat.Builder builder;
    Intent intent;
    NotificationManager notificationManager;
    int NOTIFICATION = 1;


    private long mLastStopTime;

    String values = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content wb that renders the map.
        setContentView(R.layout.activity_maps);

        pictureBusiness = new PictureBusiness(this);
        dfBusiness = new OperationBusiness(this);
        prefsHelper = new PrefsHelper(this);


        initUI();

        mostrarProgresso(getString(R.string.alert_info_loading_current_localization));

        try {
            Bundle extras = getIntent().getExtras();
            phraseReult = (Operation.Result) getIntent().getSerializableExtra(Const.OBJECT);
            position = extras.getInt(Const.POSITION);

            titleBar = phraseReult.ar.get(position).getAreaName();
            deliveryID = phraseReult.ar.get(position).getDeliveryId();
            deliveryFragmentId = phraseReult.ar.get(position).getDeliveryFragmentId();

            ids = phraseReult.ar.get(position).ids;


            for (Operation.KMLS id : ids) {
                values += id.getId() + ",";
            }

            values = values.substring(0, values.length() - 1);

            FloatingActionButton fab = new FloatingActionButton(this);
            fab.setSize(FloatingActionButton.SIZE_MINI);

            fab.setSize(FloatingActionButton.SIZE_NORMAL);


            if (extras.getLong(Const.TIME) != 0) {

                iniciarOperacao(extras.getLong(Const.TIME));
                dataInicioOperacao = extras.getString(Const.DT_START);
                dataHoraInicioOperacao = extras.getString(Const.DT_HOUR_START);

                mMap = (GoogleMap) extras.get(Const.OBJECT);

                Log.i(TAG, "\n------------------------------------------------\ndataInicioOperacao: " + dataInicioOperacao + " dtHourStart: " + dataInicioOperacao + " time: " + extras.getLong(Const.TIME));
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        definirToolbar(titleBar);
        listenerUI();

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();


        uploadFile = new UploadFile(this);

        deviceId = Integer.parseInt(prefsHelper.getPref(PrefsHelper.PREF_DEVICE_ID).toString());
        deviceIdentifier = prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER).toString();

    }

    ArrayList<ArrayList<LatLng>> coordinates;

    public void recuperarZonasStills(String ids) {

        if(Utils.isNetworkAvailable(context) && Utils.isOnline()){

            final ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.alert_info_loading_current_area));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();


        }

        coordinates = new ArrayList<ArrayList<LatLng>>();

        ApiEndpointInterface apiService =
                ApiClient.getClient().create(ApiEndpointInterface.class);

        Call<Zone.Result> call = apiService.resgatarZonasPorIdsAPI(ids);

        call.enqueue(new Callback<Zone.Result>() {
            @Override
            public void onResponse(Call<Zone.Result> call, Response<Zone.Result> response) {

                if (response != null) {

                    if (response.body().getAr().size() > 0) {

                    List<Zone> listZone = response.body().getAr();
                    String[] parts = new String[0];
                    int index = 0;
                    for (Zone z : listZone) {
                        parts = z.getContent().split("\\|");
                        ArrayList<LatLng> arrCoord = new ArrayList<>();
                        for (String part : parts) {
                            String[] latlng = part.split(",");
                            try {
                                arrCoord.add(new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1])));
                            }catch (NumberFormatException e){

                                Log.e("error","Falha de coordenada de areas Still");

                            }

                        }
                        coordinates.add(arrCoord);
                        index++;
                    }
                    gerarPoligono(mMap);

                }  else {
                    Toast.makeText(getApplicationContext(),"Erro ao definir área de operação.", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }

                    progress.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(),"Erro ao definir área de operação.", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Zone.Result> call, Throwable t) {

                Log.e(TAG, t.toString());
                Toast.makeText(getApplicationContext(),"Falha ao montar área de operação.", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }

    public void gerarPoligono(GoogleMap map){
        if (!isGeneratePoligon) {
            try {
                for (int i = 0; i < coordinates.size(); i++) {
                    PolygonOptions pol = new PolygonOptions();
                    for (LatLng latlng : coordinates.get(i)) {

                        pol.add(latlng);
                    }
                    map.addPolygon(pol.strokeColor(0x88008DD2)
                            .fillColor(0x33008DD2).strokeWidth(4).clickable(true));
                }
                isGeneratePoligon = true;
            } catch (Exception e) {
                Log.wtf(TAG, "Não foi possivel carregar Poligono");
            }

            // Set listeners for click events.
            // map.setOnPolylineClickListener(this);
            map.setOnPolygonClickListener(this);
        }
    }

    public void definirToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(title);
    }

    public void initUI() {
        ibtnAllPicture = (ImageButton) findViewById(R.id.ibtnAllPicture);
        ibtnPicture = (ImageButton) findViewById(R.id.ibtnPicture);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnSave = (Button) findViewById(R.id.btnSave);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        viewSwitcher = (ViewFlipper) findViewById(R.id.viewSwitcher);

        // [START] pictures
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        llPictures = (LinearLayout) findViewById(R.id.llPictures);
    }

    public void listenerUI() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                iniciarOperacao(mLastStopTime);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pausarCronometro();

                dataFimOperacao = myFormatDate.format(new Date());
                dataHoraFimOperacao = myFormatHour.format(new Date());

                mostrarDialogoListaOpcoes();

                cancelarNotificacao();
                stopService(intentService);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoPersonalizado();
            }
        });

        ibtnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissManager permissManager = new PermissManager(MapsActivity.this);
                if (permissManager.requestPermissCamera()) {
                    nameFile = uploadFile.generateNameFile();
                    mFileTemp = uploadFile.pathPictures(nameFile);
                    new UploadFile(MapsActivity.this).openCamera(mFileTemp);
                }
            }
        });

        ibtnAllPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecyclerView.getVisibility() == View.GONE)
                    mRecyclerView.setVisibility(View.VISIBLE);
                else mRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Este metodo inicia operação dependendo do valor que estará no parâmetro.
     * Caso long time seja diferente de 0, o app resgata o tempo que esta rolando na notificação
     *
     * @param time
     */
    public void iniciarOperacao(long time) {

        prefsHelper.savePref(PrefsHelper.PREF_TEMP_OPERATION_ID, deliveryFragmentId);

        if (time != 0) {
            llPictures.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.VISIBLE);

            viewSwitcher.showNext();
            iniciarCronometro(time);
            clickStart = true;
            intentService = new Intent(MapsActivity.this, ServiceDeviceStatus.class);

        } else {

            dataInicioOperacao = myFormatDate.format(new Date());
            dataHoraInicioOperacao = myFormatHour.format(new Date());

            llPictures.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.VISIBLE);

            viewSwitcher.showNext();

            iniciarCronometro(time);

            clickStart = true;


            // use this to start and trigger a service
            intentService = new Intent(MapsActivity.this, ServiceDeviceStatus.class);
            intentService.putExtra("deviceIdentifier", deviceIdentifier);
            intentService.putExtra("deliveryFragmentId", deliveryFragmentId);
            startService(intentService);
            startService(new Intent(this, ServiceApp.class));




        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void atualizarNotificacao(long value) {
        NotificationCompat.Builder notification = criarNotificacao(this, value);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION, notification.build());

    }

    public void cancelarNotificacao() {
        builder.setAutoCancel(true);
        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.cancel(NOTIFICATION);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public NotificationCompat.Builder criarNotificacao(Context context, long value) {
        intent = new Intent(this, MapsActivity.class);

        intent.putExtra(Const.OBJECT, phraseReult);
        intent.putExtra(Const.POSITION, position);
        intent.putExtra(Const.TIME, value);
        intent.putExtra(Const.DT_START, dataInicioOperacao);
        intent.putExtra(Const.DT_HOUR_START, dataHoraInicioOperacao);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews removeWidget = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        removeWidget.setChronometer(R.id.noteChronometer, value, null, true);
        removeWidget.setOnClickPendingIntent(R.id.title, pendingIntent);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        boolean running = true;


        builder = new NotificationCompat.Builder(context)
                .setContentText("Content")
                .setContentTitle("Title")
                .setSmallIcon(R.drawable.ic_timelapse_white_24dp)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(running)
                .setContentIntent(pendingIntent);
//                .addAction(running ? R.drawable.ic_stop_black_24dp
//                                : R.drawable.ic_play_arrow_black_24dp,
//                        running ? "Pause"
//                                : "play",
//                        pendingIntent)
//                .addAction(R.drawable.ic_stop_black_24dp, "Stop",
//                        pendingIntent);

//        PendingIntent.getActivity(context, 10,
//                new Intent(context, MapsActivity.class)
//                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
//                0)

        builder.setCustomContentView(removeWidget);

        notificationManager.notify(NOTIFICATION, builder.build());

        return builder;
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended");
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
            //showCurrentPlace();

            if (clickStart)
                mostrarDialogoDescartarOperacao(this);
            else
                onBackPressed();

        }
        return true;
    }

    boolean isGeneratePoligon = false;

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Turn on the My Location layer and the related control on the map.
        atualizarLocalizacao();

        // Get the current location of the device and set the position of the map.
        regatarLocalizacaoDispositivo();

        recuperarZonasStills(values);
    }

    public void desenharMarcador(Picture picture) {

        Drawable circleDrawable = ContextCompat.getDrawable(this, R.drawable.pictures);
        BitmapDescriptor markerIcon = resgatarIconeMarcador(circleDrawable);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(picture.getLatitude(), picture.getLongitude()))
                .title("My Marker")
                .icon(markerIcon)
        );
    }


    private BitmapDescriptor resgatarIconeMarcador(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void regatarLocalizacaoDispositivo() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_AND_COARSE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            try {
                lastLat = mLastKnownLocation.getLatitude();
                lastLng = mLastKnownLocation.getLongitude();

            } catch (Exception e) {
                Log.wtf(TAG, "Não foi possivel resgatar ultima localizacao!!!");
            }
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_AND_COARSE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }

                break;

            case PermissManager.WRITE_CAMERA:

                new UploadFile(this).openCamera(mFileTemp);

                break;
        }
        atualizarLocalizacao();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */

    private void atualizarLocalizacao() {
        if (mMap == null) {
            return;
        } else {
            ocultarProgresso();
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_AND_COARSE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        // Flip the values of the red, green, and blue components of the polygon's color.
        int color = polygon.getStrokeColor() ^ 0x00ffffff;
        polygon.setStrokeColor(color);
        color = polygon.getFillColor() ^ 0x00ffffff;
        polygon.setFillColor(color);
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    public void mostrarDialogoDescartarOperacao(Context context) {
        String[] array = {
                getString(R.string.alert_title_discard),
                getString(R.string.alert_content_discard),
                getString(R.string.btn_cancel),
                getString(R.string.btn_discard)};

        new Dialogs(context).showDialog(array, new DialogResultListener() {
            @Override
            public void onResult(boolean result) {
                if (result) {


                    finish();
                    cancelarNotificacao();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (clickStart)
            mostrarDialogoDescartarOperacao(this);
        else finish();
    }

    public void mostrarDialogoPersonalizado() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialog_layout = inflater.inflate(R.layout.dialog_info_delivery, null);
        AlertDialog.Builder db = new AlertDialog.Builder(this);
        db.setView(dialog_layout);

        db.setTitle(getString(R.string.alert_title_delivery));
        db.setPositiveButton(getString(R.string.btn_save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                salvarDadosOperacaoSqlite();
            }
        });
        db.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        TextView tvDate = (TextView) dialog_layout.findViewById(R.id.tvDate);
        TextView tvStart = (TextView) dialog_layout.findViewById(R.id.tvStart);
        TextView tvEnd = (TextView) dialog_layout.findViewById(R.id.tvEnd);
        TextView tvTimeAll = (TextView) dialog_layout.findViewById(R.id.tvTimeAll);
        TextView tvTimeCurrent = (TextView) dialog_layout.findViewById(R.id.tvTimeCurrent);
        TextView tvQntPic = (TextView) dialog_layout.findViewById(R.id.tvPictures);
        TextView tvKm = (TextView) dialog_layout.findViewById(R.id.tvKm);

        String str = DateUtils.formateT();
        tvDate.setText(str);

        try {
            tvTimeAll.setText(DateUtils.compararDataRetornarMinutos(dataInicioOperacao, dataFimOperacao));

        } catch (Exception e) {
            Log.wtf(TAG, "Erro nas dataInicioOperacao: " + e.toString());
        }

        tvStart.setText(dataHoraInicioOperacao);
        tvEnd.setText(dataHoraFimOperacao);
        tvQntPic.setText(String.valueOf(recuperarQuantidadeFotoPorDispositivo()));
        tvTimeCurrent.setText(converterLongtimeEmHoras(mLastStopTime));

        AlertDialog dialog = db.show();
    }

    public void salvarDadosOperacaoSqlite() {
        final ProgressDialog progress = new ProgressDialog(this);

        progress.setMessage(getString(R.string.alert_info_send_date));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        Operation operation = new Operation();
        operation.setDatetimeStart(dataInicioOperacao);
        operation.setDatetimeEnd(dataFimOperacao);
        operation.setDeliveryFragmentId(phraseReult.ar.get(position).getDeliveryFragmentId());
        operation.setRouteId(phraseReult.ar.get(position).getRouteId());
        operation.setDeviceId(deviceId);
        operation.setDeliveryId(deliveryID);
        operation.setDistance(recuperarDistanciaPercorrida());
        operation.setQntPictures(recuperarQuantidadeFotoPorDispositivo());
        dfBusiness.insert(operation);

        progress.dismiss();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {

            return;
        }

        switch (requestCode) {

            case UploadFile.REQUEST_CODE_TAKE_PICTURE:

                try {

                    uploadFile.compressImages(nameFile, Uri.fromFile(mFileTemp).getPath());

                    Picture picture = new Picture();
                    picture.setUri(Uri.fromFile(mFileTemp).getPath());
                    picture.setLatitude(lastLat);
                    picture.setLongitude(lastLng);
                    picture.setType(1);
                    picture.setDeviceId(deviceId);
                    picture.setDeliveryId(deliveryID);
                    picture.setSync(0);

                    pictureBusiness.insert(picture);

                    carregarImagensOperacao();


                } catch (Exception e) {

                    Log.e("Activity", "Error while creating temp file", e);
                }

                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This method is set recycler wb develop
     */
    private void recyclerViewDevelop() {

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        linearlayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(linearlayoutManager);
        linearlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Create the recyclerViewAdapter
        adapter = new PictureAdapter(this, ar, empty);

        mRecyclerView.setAdapter(adapter);

    }

    public void listarFotosGravadas() {
        ar = (ArrayList<Picture>) pictureBusiness.getList(Constants.KEY_DELIVERY_ID + "=" + deliveryID + " AND " + Constants.KEY_DEVICE_ID + "=" + deviceId, "");

        if (ar.size() > 0) {
            for (Picture picture : ar) {
                desenharMarcador(picture);
            }
        }
    }

    public int recuperarDistanciaPercorrida() {
        return 15;
    }

    private int recuperarQuantidadeFotoPorDispositivo() {
        return pictureBusiness.getList(Constants.KEY_DELIVERY_ID + "=" + deliveryID, "").size();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLat = location.getLatitude();
        lastLng = location.getLongitude();

        definirNovaLocalizacaoDispositivo(location);
    }

    private void definirNovaLocalizacaoDispositivo(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();


        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
    }

    public void mostrarDialogoListaOpcoes() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(getString(R.string.alert_title_choose_a_option));
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        arrayAdapter.add(getString(R.string.btn_continue));
        arrayAdapter.add(getString(R.string.btn_finish));

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);

                if (strName != null) {

                    if (strName.equals(getString(R.string.btn_continue))) {

                        iniciarCronometro(0);
                        startService(intentService);

                    } else {

                        viewSwitcher.showNext();

                        dataFimOperacao = myFormatDate.format(new Date());
                        dataHoraFimOperacao = myFormatHour.format(new Date());

                        stopService(intentService);
                        mostrarDialogoPersonalizado();
                    }
                }
            }
        });
        builderSingle.show();
    }

    private void iniciarCronometro(long time) {

        if (time != 0) {
            chronometer.setBase(time);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                atualizarNotificacao(time);
            }
        } else {
            // on first start
            if (mLastStopTime == 0) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                // on resume after pause
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    atualizarNotificacao(SystemClock.elapsedRealtime());
                }
            } else {
                long intervalOnPause = (SystemClock.elapsedRealtime() - mLastStopTime);
                chronometer.setBase(chronometer.getBase() + intervalOnPause);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    atualizarNotificacao(chronometer.getBase() + intervalOnPause);
                }
            }
        }


        chronometer.start();
    }

    private void pausarCronometro() {
        chronometer.stop();
        mLastStopTime = SystemClock.elapsedRealtime();
    }

    public String converterLongtimeEmHoras(long time) {

        long timeElapsed = time - chronometer.getBase();
        int hours = (int) (timeElapsed / 3600000);
        int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
        int seconds = (int) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;

        return hours + "h " + minutes + "m " + seconds + "s";
    }

    public void definirLocalizacao(double lat, double lng) {
        LatLng coordinate = new LatLng(lat, lng); //Store these lat lng values somewhere. These should be constant.
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                coordinate, 15);
        mMap.animateCamera(location);
    }

    /**
     * Este método carrega as imagens da operação na lista de imagens na vertical
     */
    public void carregarImagensOperacao() {
        listarFotosGravadas();
        recyclerViewDevelop();

        if (recuperarQuantidadeFotoPorDispositivo() > 0)
            ibtnAllPicture.setVisibility(View.VISIBLE);
        else ibtnAllPicture.setVisibility(View.GONE);
    }

    private ProgressDialog progress;

    public void mostrarProgresso(String msg) {
        progress = new ProgressDialog(this);
        progress.setMessage(msg);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    public void ocultarProgresso() {
        progress.dismiss();
    }

}