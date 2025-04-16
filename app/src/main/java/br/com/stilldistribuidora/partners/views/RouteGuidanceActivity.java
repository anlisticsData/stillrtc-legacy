package br.com.stilldistribuidora.partners.views;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.PictureInPictureParams;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Rational;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import br.com.stilldistribuidora.partners.Adapter.ConfigurationsUser;
import br.com.stilldistribuidora.partners.Base.ActivityBaseApp;
import br.com.stilldistribuidora.partners.Base.GeoCoordinate;
import br.com.stilldistribuidora.partners.Base.Locations;
import br.com.stilldistribuidora.partners.Base.Markers;
import br.com.stilldistribuidora.partners.Base.OnResponse;
import br.com.stilldistribuidora.partners.Base.PhoneInformations;
import br.com.stilldistribuidora.partners.Base.Playler;
import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateModel;
import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateReaderDbHelper;
import br.com.stilldistribuidora.partners.Base.navegador.NavegationModel;
import br.com.stilldistribuidora.partners.Base.navegador.NavegationsReaderDbHelper;
import br.com.stilldistribuidora.partners.Base.navegador.ProcessWaypoints;
import br.com.stilldistribuidora.partners.Base.navegador.RoutePoints;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentBusiness;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentHelper;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentModel;
import br.com.stilldistribuidora.partners.Casts.RouterInMap;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Commom.Enum;
import br.com.stilldistribuidora.partners.Commom.Functions;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Models.RouterMap;
import br.com.stilldistribuidora.partners.Models.Zonas;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.Services.ServicesApiStill;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.partners.resources.Resources;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.lib.PartenerHelpes;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;

public class RouteGuidanceActivity extends ActivityBaseApp implements OnMapReadyCallback {
    private static final ContextFrame ContextFrame = new ContextFrame();
    private class NavegationState{
        private  int sizeInformation=4;
        private String delimiterSeparated="\\|";
        public  String operation="";
        public String routerId="";
        public int lastPoint=0;
        public  String latLng ="";

        public NavegationState(){
            List<NavegationModel> lastNavegations = ContextFrame.navegationsReaderDbHelper.getByProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
            String[] routerIdGeoSize = null;
            if (lastNavegations.size() > 0) {
                String content = lastNavegations.get(0).getContent();
                routerIdGeoSize = Functions.split(this.delimiterSeparated,content);
                if (routerIdGeoSize != null && routerIdGeoSize.length == this.sizeInformation){
                    this.operation =routerIdGeoSize[0];
                    this.routerId=routerIdGeoSize[1];
                    this.lastPoint = Integer.parseInt(routerIdGeoSize[4]);
                    this.latLng  =routerIdGeoSize[3];
                }

            }
        }

    }


    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            ContextFrame.batteryTxt = String.valueOf(level);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_operation_router_2);
        ContextFrame.contextActivity = this;
        ConfigDataAccess configModel = new ConfigDataAccess(ContextFrame.contextActivity);
        ContextFrame.userDevice = (Config) configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE));
        ContextFrame.toolbar= (Toolbar) findViewById(R.id.toolbar);
        ContextFrame.actionBar = getActionBar();
        ContextFrame.uncheckedlayot = (ConstraintLayout)findViewById(R.id.uncheckedlayot);
        ContextFrame.unlock =(LinearLayout)findViewById(R.id.unlock);
        ContextFrame.main = (LinearLayout)findViewById(R.id.main);
        ContextFrame.loadding = (LinearLayout)findViewById(R.id.loadding);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        ContextFrame.phoneInformationPhoneId = new PhoneInformations(ContextFrame.contextActivity).getPhoneId();
        InitPermisionGeral();
        keepScreenOn();
        retrieveReceivedData();
        initMapGoogle();
        initPlayer();
        configureCompatibiliteApp();
        setConfigurationCronometro();
        initContextFrame();
        initGps();
        initCamera();
        initEvents();
        setComandsPainel(View.GONE, View.GONE, View.GONE);
        initModelDataBase();
        initModelDataBaseConfiruations();
        publicReceivedInterfaceEvents();
        actionPhoto(false);
        ContextFrame.navegationsReaderDbHelper.initInfo();
        setToolbar();
        isOperationToOpen();
        isResetOperation();
    }


    public void loadding(boolean isLoadding){
        if(isLoadding){

            ContextFrame.loadding.setVisibility(View.VISIBLE);
        }else{

            ContextFrame.loadding.setVisibility(View.GONE);
        }
    }




    private  void isResetOperation(){
        List<NavegationModel> lastNavegations = ContextFrame.navegationsReaderDbHelper.getByProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
        String[] routerIdGeoSize = null;
        if (lastNavegations.size() > 0) {
            String content = lastNavegations.get(0).getContent();
            routerIdGeoSize = Functions.split("\\|",content);
        }
        String deliveryCode=  String.valueOf(ContextFrame.operationModelRepository.getOperationID());


        System.out.println("#344 "+deliveryCode.trim()+" => "+routerIdGeoSize[0].trim());
        if(deliveryCode.trim()!=routerIdGeoSize[0].trim()){
            resetStateOperation();
            return;
        }
        if (routerIdGeoSize != null && routerIdGeoSize.length == 4  &&
                deliveryCode.trim().equals(routerIdGeoSize[0].trim()) ) {

            String[] finalRouterIdGeoSize = routerIdGeoSize;
            ContextFrame.previousSequence = Integer.parseInt(finalRouterIdGeoSize[3]);
        }



       /* if (routerIdGeoSize != null && routerIdGeoSize.length == 4  &&
                deliveryCode.trim().equals(routerIdGeoSize[0].trim()) ) {

            String[] finalRouterIdGeoSize = routerIdGeoSize;
            ContextFrame.previousSequence = Integer.parseInt(finalRouterIdGeoSize[3]);
        }*/



        double distance = 0;
        distance = (int) distanceBetweenTwoPoints(ContextFrame.latiudeActive, ContextFrame.longitudeActive,
                         ContextFrame.myPath.get(ContextFrame.previousSequence).getLat(),ContextFrame.myPath.get(ContextFrame.previousSequence).getLng());


        Integer operationCurrent = ContextFrame.operationModelRepository.getOperationID();
        Integer operationSelected =(routerIdGeoSize[0].trim().isEmpty()) ? 0 :  Integer.valueOf(routerIdGeoSize[0]);
        boolean isEquals = operationCurrent.equals(operationSelected);
        if (ContextFrame.previousSequence > 0 && isEquals && distance < ContextFrame.raio) {
            ContextFrame.initPoint = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(ContextFrame.contextActivity);
            String[] finalRouterIdGeoSize = routerIdGeoSize;
            double finalDistance = distance;
            builder.setPositiveButton("Continuar >> ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContextFrame.running = true;
                    ContextFrame.ultimo_comando = "I";
                    performAudioGuidance();
                    startRouterGo(finalDistance);
                    initStartServices();


                }
            });
            builder.setTitle("Continuar Operação..!");
            AlertDialog dialog = builder.create();
            dialog.show();
        }




    }

    private  void isOperationToOpen(){
        List<OperationModelRepository> operationAr = ContextFrame.appOpeationsRepository.getAll(String.valueOf(Enum.STATUS_IS_OPEN_OPERATION));
        boolean dif=true;
        OperationModelRepository  operationOpen =  new OperationModelRepository();

        for(OperationModelRepository m : operationAr){
            operationOpen = m;
            Integer integer = Integer.valueOf(m.getOperationID());
            Integer integer1 = Integer.valueOf(ContextFrame.operationModelRepository.getOperationID());
            if(integer.equals(integer1)){
                dif=false;
                break;
            }
        }
        if (operationAr.size() != 0  &&  dif ) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContextFrame.contextActivity);
            builder.setTitle("Ação Cancelada.");
            builder.setMessage("Operação ("+operationOpen.getOperationID()+" "+operationOpen.getCreatedAt()+") em Aberto. \npara Continuar em Outra Operação.");
            builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    ContextFrame.running=false;
                    ContextFrame.alerta.dismiss();
                    Intent intent = new Intent(ContextFrame.contextActivity, MyOperations.class);
                    intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, Enum.STATUS_IS_OPEN_OPERATION);
                    startActivity(intent);
                    finish();
                }

            });
            ContextFrame.alerta = builder.create();
            ContextFrame.alerta.show();
            return;
        }
    }

    private void setToolbar() {
        setSupportActionBar(ContextFrame.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }




    private void getDeviceApi() {
        ContextFrame.operationModelRepository.setDeviceID(ContextFrame.userDevice.getDataJson());
        runningRoute();

    }

    private void initCamera() {
        startActivityOnResult();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getFilesDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        ContextFrame.currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void startActivityOnResult() {
        ContextFrame.receiveAnswerFromChildren = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Constants.FINISH_OPERATION_OK_ACTIONS) {


                        }

                    }
                });

        ContextFrame.startCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            OnClick onClick = new OnClick() {
                                @Override
                                public void onClick(Object obj) {
                                    File photoFile = null;
                                    try {
                                        String realPathFromURI = getRealPathFromURI(ContextFrame.cam_uri);
                                        System.out.println("#4 " + ContextFrame.cam_uri.getPath());
                                        photoFile = createImageFile();
                                        String[] geo = (((String) obj).split("\\|"))[0].split("\\,");
                                        PhotoEntity photoEntity = new PhotoEntity();
                                        photoEntity.setCreated_at(PartenerHelpes.getDate());
                                        photoEntity.setUri(realPathFromURI);
                                        photoEntity.setSync("0");
                                        photoEntity.setDelivery_id(String.valueOf(ContextFrame.operationModelRepository.getOperationID()));
                                        photoEntity.setLatitude(geo[0]);
                                        photoEntity.setLongitude(geo[1]);
                                        photoEntity.setPic_uid("0");
                                        photoEntity.setType("1");
                                        photoEntity.setPartners_id(String.valueOf(ContextFrame.operationModelRepository.getParternID()));
                                        photoEntity.setDeviceId(ContextFrame.userDevice.getDataJson());
                                        if (ContextFrame.photosPartnersModel.save(photoEntity) > 0) {
                                            Toast.makeText(ContextFrame.contextActivity, "Foto Salva Com Sucesso..!", Toast.LENGTH_SHORT).show();
                                        }
                                        try {
                                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ContextFrame.cam_uri.toString()));
                                            System.out.println("s");

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (IOException ex) {
                                    }
                                }
                            };

                            InitPermisionGeral();
                            geoPosition(onClick);


                        }
                    }
                });


    }

    private void geoPosition(OnClick onClick) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

            }

            @Override
            public void onProviderEnabled(String arg0) {

            }

            @Override
            public void onProviderDisabled(String arg0) {

            }

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    onClick.onClick(location.getLatitude() + "," + location.getLongitude() + "|" + location.getAccuracy() + "|" + location.getProvider());

                }

            }
        }, null);


    }


    private void initContextFrame() {
        ContextFrame.handlerMessage = new Handler();
        ContextFrame.statusPorcentageRouter = findViewById(R.id.statusPorcentageRouter);
        ContextFrame.statuskmsRouter = findViewById(R.id.statuskmsRouter);
        ContextFrame.btnmini = findViewById(R.id.btnmini);
        ContextFrame.btn_start = findViewById(R.id.btn_start);
        ContextFrame.btn_stop = findViewById(R.id.btn_stop);
        ContextFrame.btnPhoto = findViewById(R.id.btnPhoto);

        ContextFrame.photosPartnersModel = new PhotosPartnersModel(ContextFrame.contextActivity);
        ContextFrame.btn_utimo_comando = findViewById(R.id.btn_utimo_comando);
        ContextFrame.playlerRouter = new Playler(ContextFrame.contextActivity);
        ContextFrame.appOpeationsRepository = new AppOpeationsRepository(ContextFrame.contextActivity);
        ContextFrame.conf = new ServerConfiguration(ContextFrame.contextActivity);
        ContextFrame.servicesHttp = new ServicesHttp(ContextFrame.contextActivity, ContextFrame.conf, CallBackOnResponseGlobalApi());
        ContextFrame.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(this));
        String userLogged = ContextFrame.conf.getUserLogged();
        ContextFrame.userLoginCast = new Gson().fromJson(userLogged, UserLoginCast.class);

    }

    private OnResponseHttp CallBackOnResponseGlobalApi() {
        OnResponseHttp onResponseHttp = new OnResponseHttp() {
            @Override
            public void onResponse(Object data) {
                System.out.println("d");
            }

            @Override
            public void onError(Object error) {

            }
        };
        return onResponseHttp;
    }

    public void pickCamera() {
        InitPermisionGeral();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        ContextFrame.cam_uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, ContextFrame.cam_uri);
        //startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE); // OLD WAY
        ContextFrame.startCamera.launch(cameraIntent);                // VERY NEW WAY
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    private void initModelDataBase() {
        ContextFrame.navegationsReaderDbHelper = new NavegationsReaderDbHelper(ContextFrame.contextActivity);

        ContextFrame.routerMovimentDbHelper = new RouterMovimentBusiness(new RouterMovimentHelper(ContextFrame.contextActivity));
        ContextFrame.lastOperationAndActiveRoute = new NavegationModel();
        ContextFrame.deliveryStateReaderDbHelper = new DeliveryStateReaderDbHelper(ContextFrame.contextActivity);
    }

    private void initModelDataBaseConfiruations() {
        ContextFrame.lastOperationAndActiveRoute.setProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));


    }

    private void setComandsPainel(int startRouter, int photo, int ir) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  ContextFrame.start.setVisibility(startRouter);
                // ContextFrame.photo.setVisibility(photo);
                //  ContextFrame.btnIr.setVisibility(ir);

            }
        });

    }

    private void initGps() {
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        boolean providerEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!providerEnabled){
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContextFrame.contextActivity);
            OnResponse click = new OnResponse() {
                @Override
                public void OnResponseType(String type, Object object) {
                    ContextFrame.dialog.dismiss();
                    showTheRouteStatusOnTheDisplay();
                    finish();
                }
            };
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    click.OnResponseType(null,null);
                }
            });
            builder.setTitle("Ação Cancelada..!");
            ContextFrame.dialog = builder.create();
            ContextFrame.dialog.show();
            return ;
    }
        ContextFrame.fusedLocationClient = LocationServices.getFusedLocationProviderClient(ContextFrame.contextActivity);
        ContextFrame.HandlerGps = new Handler();
        ContextFrame.HandlerGps.post(new Runnable() {
            @Override
            public void run() {
                InitPermisionGeral();

                if (ActivityCompat.checkSelfPermission(ContextFrame.contextActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ContextFrame.contextActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                ContextFrame.fusedLocationClient.getLastLocation().addOnSuccessListener(RouteGuidanceActivity.this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    ContextFrame.latiudeActive = location.getLatitude();
                                    ContextFrame.longitudeActive = location.getLongitude();
                                    ContextFrame.distance = 0;
                                    ContextFrame.distanceStartPoint = getDistanceInitMarket();
                                }
                            }
                        });
                ContextFrame.HandlerGps.postDelayed(this, 8*1000);
            }
        });
    }

    private int getDistanceInitMarket() {
        return ((int) distanceBetweenTwoPoints(ContextFrame.latiudeActive,
                ContextFrame.longitudeActive,
                ContextFrame.myPath.get(ContextFrame.previousSequence).getLat(),
                ContextFrame.myPath.get(ContextFrame.previousSequence).getLng()));


    }


    private double distanceBetweenTwoPoints(double latiudeActive, double longitudeActive, double latDestiny, double lngDestiny) {
        try {
            GeoCoordinate p1 = new GeoCoordinate(latiudeActive, longitudeActive);
            GeoCoordinate p2 = new GeoCoordinate(latDestiny, lngDestiny);
            return p1.distanceInKm(p2) * 1000;
        } catch (Exception e) {
            return 0;
        }
    }


    private void initEvents() {
        ContextFrame.statusPorcentageRouter.setText("0%");
        ContextFrame.statuskmsRouter.setText("0km");
        ContextFrame.btnmini.setVisibility(View.GONE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ContextFrame.btnmini.setVisibility(View.VISIBLE);
            ContextFrame.btnmini.setEnabled(true);
            ContextFrame.btnmini.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Display d = getWindowManager()
                            .getDefaultDisplay();
                    Point p = new Point();
                    d.getSize(p);
                    int width = p.x;
                    int height = p.y;

                    Rational ratio
                            = new Rational(width, height);
                    PictureInPictureParams.Builder
                            pip_Builder
                            = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        pip_Builder = new PictureInPictureParams
                                .Builder();
                        pip_Builder.setAspectRatio(ratio).build();
                        enterPictureInPictureMode(pip_Builder.build());
                    }


                }
            });
        }


        ContextFrame.btn_utimo_comando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAudioGuidance();
            }
        });

        ContextFrame.btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRouterGps();
            }
        });



        ContextFrame.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                if(PartenerHelpes.getLatencyGoogle() > 150){
                    startActivity(new Intent(ContextFrame.contextActivity,SlowInternet.class));

                    return;
                }
                */


                double distance = 0;
                if(!br.com.stilldistribuidora.Libs.Libs.isOnlineApplication(RouteGuidanceActivity.this)){
                    Intent intent = new Intent(RouteGuidanceActivity.this, NetworkActivity2.class);
                    startActivity(intent);

                    return;
                }

                distance = (int) distanceBetweenTwoPoints(ContextFrame.latiudeActive, ContextFrame.longitudeActive,
                                 ContextFrame.myPath.get(ContextFrame.previousSequence).getLat(),
                                 ContextFrame.myPath.get(ContextFrame.previousSequence).getLng());
                if(distance <= ContextFrame.raio){

                    startRouterGps();

                }else{


                    invalideDistanceCurrent();

                }

            }
        });


        ContextFrame.btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCamera();
            }
        });

        ContextFrame.unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextFrame.uncheckedlayot.setVisibility(View.GONE);
            }
        });




    }




    private void stopRouterGpsOperation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContextFrame.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        if (type.equals("STOP")) {
                            ContextFrame.running = false;
                            ContextFrame.ultimo_comando = "";
                            ContextFrame.initPoint = false;
                            ContextFrame.init_comando = false;
                            setButtonStateStart(false);
                            finish();
                        }
                        ContextFrame.dialog.dismiss();
                    }
                };


                builder.setPositiveButton("Parar   ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.OnResponseType("STOP", null);
                    }
                });
                builder.setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.OnResponseType("CONTINUE", null);
                    }
                });
                builder.setTitle("Continuar Operação..!");
                ContextFrame.dialog = builder.create();
                ContextFrame.dialog.show();
            }
        });


    }


    private void stopRouterGps() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContextFrame.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        if (type.equals("STOP")) {
                            ContextFrame.running = false;
                            ContextFrame.ultimo_comando = "";
                            ContextFrame.initPoint = false;
                            ContextFrame.init_comando = false;
                            setButtonStateStart(false);
                            finish();
                        }
                        ContextFrame.dialog.dismiss();
                    }
                };


                builder.setPositiveButton("Parar   ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.OnResponseType("STOP", null);
                    }
                });
                builder.setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.OnResponseType("CONTINUE", null);
                    }
                });
                builder.setTitle("Continuar Operação..!");
                ContextFrame.dialog = builder.create();
                ContextFrame.dialog.show();
            }
        });


    }


    private void publicReceivedInterfaceEvents() {
        ContextFrame.responseGenerics = new OnResponse() {
            @Override
            public void OnResponseType(String type, Object object) {

                if (type.equals(Resources.STARTING_POINT_NOT_FOUND)) {
                    showMensagemStartNotFould();
                } else if (type.equals(Resources.ERROR_POINT_NOT_FOUND)) {
                    showMensagemErrorNotFould();

                }

            }
        };

    }

    private void showMensagemErrorNotFould() {
        ContextFrame.playlerRouter.gpsNotFound();
        timeSom(ContextFrame.timeProcessSound);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContextFrame.contextActivity);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RouterMovimentModel moviments = new RouterMovimentModel();
                        moviments.setAction("C");
                        moviments.setIdKeys(String.valueOf(ContextFrame.currentMarket.getId()));
                        moviments.setStartPoints(String.valueOf(ContextFrame.previousSequence));
                        moviments.setDeviceID(ContextFrame.operationModelRepository.getDeviceID());
                        moviments.setDeliveryId(String.valueOf(ContextFrame.operationModelRepository.getOperationID()));
                        ContextFrame.routerMovimentDbHelper.insert(moviments);
                        ContextFrame.previousSequence++;
                        ContextFrame.positionError = 0;
                        ContextFrame.navegationsReaderDbHelper.update(getRouterCurrent(ContextFrame.previousSequence-1));
                        drawMapInMarkers();
                        showTheRouteStatusOnTheDisplay();
                        System.out.println("#12 cancelado ["+ContextFrame.previousSequence+"] ");

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                builder.setTitle("Igorar Ponto..!");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void    showMensagemStartNotFould() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContextFrame.contextActivity);
                builder.setPositiveButton(R.string.navegar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse(ContextFrame.actuvePoint_initPoint));
                            startActivity(intent);
                        } catch (Exception e) {
                        }

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                builder.setTitle("Inicio Obrigatório..!");
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }


    private void startRouterGps() {
        try {
            System.out.println("#23 " + ContextFrame.distanceStartPoint);
            if (ContextFrame.running == false) {

                ContextFrame.running = true;
                ContextFrame.ultimo_comando = "";
                ContextFrame.initPoint = true;
                ContextFrame.distance = 0;
                if (!ContextFrame.init_comando) {
                    List<OperationModelRepository> operationAr = ContextFrame.appOpeationsRepository.getAll(String.valueOf(Enum.STATUS_IS_OPEN_OPERATION));
                    List<NavegationModel> lastNavegations = ContextFrame.navegationsReaderDbHelper.getByProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
                    boolean dif = true;
                    for (OperationModelRepository m : operationAr) {
                        Integer integer = Integer.valueOf(m.getOperationID());
                        Integer integer1 = Integer.valueOf(ContextFrame.operationModelRepository.getOperationID());
                        if (integer.equals(integer1)) {
                            dif = false;
                            break;
                        }
                    }


                    if (operationAr.size() != 0 && dif) {
                        //Cria o gerador do AlertDialog
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContextFrame.contextActivity);
                        //define o titulo
                        builder.setTitle("Ação Cancelada.");
                        //define a mensagem
                        builder.setMessage("Operação em Aberto. para Continuar em Outra Operação.");
                        //define um botão como positivo
                        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                ContextFrame.running = false;
                                ContextFrame.alerta.dismiss();
                                Intent intent = new Intent(ContextFrame.contextActivity, MyOperations.class);
                                intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, Enum.STATUS_IS_OPEN_OPERATION);
                                startActivity(intent);
                                finish();
                            }

                        });
                        //cria o AlertDialog
                        ContextFrame.alerta = builder.create();
                        //Exibe
                        ContextFrame.alerta.show();
                        return;
                    }
                    double distance = 0;
                    distance = (int) distanceBetweenTwoPoints(ContextFrame.latiudeActive, ContextFrame.longitudeActive,
                                        ContextFrame.myPath.get(ContextFrame.previousSequence).getLat(),
                                        ContextFrame.myPath.get(ContextFrame.previousSequence).getLng());

                    String[] routerIdGeoSize = null;
                    if (lastNavegations.size() > 0) {
                        String content = lastNavegations.get(0).getContent();
                        routerIdGeoSize = Functions.split("\\|", content);
                    }
                    if (routerIdGeoSize != null && routerIdGeoSize.length == 4 &&
                            routerIdGeoSize[1].equals(ContextFrame.rotaActiva) &&
                            ContextFrame.operationModelRepository.getOperationID().equals(routerIdGeoSize[0])) {
                        String[] finalRouterIdGeoSize = routerIdGeoSize;
                        ContextFrame.previousSequence = Integer.parseInt(finalRouterIdGeoSize[3]);
                    }


                    if (ContextFrame.previousSequence > 0 && ContextFrame.operationModelRepository.
                            getOperationID().equals(routerIdGeoSize[0])) {

                        ContextFrame.initPoint = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(ContextFrame.contextActivity);
                        String[] finalRouterIdGeoSize = routerIdGeoSize;
                        double finalDistance = distance;
                        builder.setPositiveButton("Continuar >> ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ContextFrame.running = true;
                                ContextFrame.ultimo_comando = "I";
                                performAudioGuidance();
                                startRouterGo(finalDistance);
                                setButtonStateStart(ContextFrame.running);
                                initStartServices();

                            }
                        });
                        builder.setTitle("Continuar Operação..!");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        setButtonStateStart(false);
                        ContextFrame.running = false;
                        ContextFrame.playlerRouter.direcaoPontoAzul();
                        timeSom(ContextFrame.timeProcessSound);
                        startRouterGo(ContextFrame.distanceStartPoint);

                    }
                }
            }
        }catch (Exception e){
            System.out.println("#100 =>" +e.getMessage());
        }
    }


    private void startRouterGo(double distance) {
        try {
            if (distance > ContextFrame.raio && ContextFrame.previousSequence == 0 && ContextFrame.initPoint) {
                ContextFrame.init_comando = false;
                invalideDistanceCurrent();
                ContextFrame.running=false;
                setButtonStateStart(ContextFrame.running);
            } else {
                ContextFrame.running=true;
                ContextFrame.init_comando = true;
                ContextFrame.ultimo_comando = "I";
                performAudioGuidance();
                setButtonStateStart(ContextFrame.running);
                getDeviceApi();
            }
        }catch (Exception e){
            System.out.println("#100 =>" +e.getMessage());
        }
    }

    private void initStartServices() {
        startService(new Intent(ContextFrame.contextActivity, ServicesApiStill.class));
    }
    private void runningRoute() {

        //9

        if (ContextFrame.operationModelRepository.getDeviceID() == null) {
            //Cria o gerador do AlertDialog
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContextFrame.contextActivity);
            //define o titulo
            builder.setTitle("Ação Cancelada.");
            //define a mensagem
            builder.setMessage("Não há Dispositivo Livres no Momento. para Continuar em Outra Operação.");
            //define um botão como positivo
            builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    ContextFrame.alerta.dismiss();
                    Intent intent = new Intent(ContextFrame.contextActivity, MyOperations.class);
                    intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, "2");
                    startActivity(intent);
                    finish();
                }

            });
            //cria o AlertDialog
            ContextFrame.alerta = builder.create();
            //Exibe
            ContextFrame.alerta.show();
            return;
        }

        //2

        ContextFrame.navegationsReaderDbHelper.update(getRouterCurrent(ContextFrame.previousSequence));

        ContextFrame.running = true;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("#233 navegando.... " + ContextFrame.init_comando+" dasd  +> "+ContextFrame.latiudeActive+"***"+ContextFrame.longitudeActive);
                    knowWhereIAm(ContextFrame.latiudeActive, ContextFrame.longitudeActive);
                    if (ContextFrame.init_comando) {
                        actionPhoto(true);
                        handler.postDelayed(this, 10 * 1000);
                    }
                }catch (Exception e){
                    System.out.println("#100 =>" +e.getMessage());
                }

            }
        });



    }

    private void actionPhoto(boolean visible) {
        if (visible) {
            ContextFrame.btnPhoto.setVisibility(View.VISIBLE);
        } else {
            ContextFrame.btnPhoto.setVisibility(View.GONE);
        }
    }

    private void invalideDistanceCurrent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ContextFrame.actuvePoint_initPoint = String.format(Resources.NAVEGATIONS_MAPS, ContextFrame.latiudeActive, ContextFrame.longitudeActive,
                            ContextFrame.myPath.get(ContextFrame.previousSequence+1).getLat(), ContextFrame.myPath.get(ContextFrame.previousSequence+1).getLng());
                    ContextFrame.playlerRouter.farFromTheBeginning();
                    ContextFrame.btn_start.setText("Iniciar");
                    ContextFrame.ultimo_comando = "";
                    timeSom(ContextFrame.timeProcessSound);
                    ContextFrame.initPoint = false;
                    ContextFrame.handlerMessage.post(new Runnable() {
                        @Override
                        public void run() {
                            ContextFrame.responseGenerics.OnResponseType(Resources.STARTING_POINT_NOT_FOUND, null);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    ContextFrame.handlerMessage.post(new Runnable() {
                        @Override
                        public void run() {
                            ContextFrame.responseGenerics.OnResponseType(Resources.STARTING_POINT_NOT_FOUND, null);
                        }
                    });

                }
            }
        }).start();
    }


    public void performAudioGuidance() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ContextFrame.runningPlayer && ContextFrame.previousSequence < ContextFrame.markers.size()) {
                            ContextFrame.playlerRouter.direcaoPontoAzul();
                            timeSom(ContextFrame.timeProcessSound);

                        }
                        if (!ContextFrame.runningPlayer) {
                            switch (ContextFrame.ultimo_comando) {
                                case "I":
                                    ContextFrame.playlerRouter.direcaoPontoAzul();
                                    timeSom(ContextFrame.timeProcessSound);
                                    break;
                                case "F":
                                    ContextFrame.playlerRouter.goAhead();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;
                                case "D":
                                    ContextFrame.playlerRouter.turnRight();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;
                                case "E":
                                    ContextFrame.playlerRouter.turnLeft();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;

                                case "VELND":

                                    ContextFrame.playlerRouter.returnFromTheOtherSideRigth();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;


                                case "VELNE":

                                    ContextFrame.playlerRouter.returnFromTheOtherSideLeft();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;

                                case "SDPA":

                                    ContextFrame.playlerRouter.direcaoPontoAzul();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;

                                case "NEROLRNQSF":
                                    ContextFrame.playlerRouter.NEROLRNQSF();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;


                                case "VDSVE":

                                    ContextFrame.playlerRouter.VDSVE();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;


                                case "VESVD":

                                    ContextFrame.playlerRouter.VESVD();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;


                                case "NRPPS":

                                    ContextFrame.playlerRouter.NRPPS();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;


                                case "NRPSS":

                                    ContextFrame.playlerRouter.NRPSS();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;


                                case "NRPTS":

                                    ContextFrame.playlerRouter.NRPTS();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;

                                case "NRPQS":

                                    ContextFrame.playlerRouter.NRPQS();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;


                                case "FFF":

                                    ContextFrame.playlerRouter.FFF();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;

                                default:
                                    ContextFrame.playlerRouter.thereIsNoCommand();
                                    timeSom(ContextFrame.timeProcessSound);
                                    ContextFrame.runningPlayer = false;
                                    break;

                            }


                        }
                    }
                }).start();

            }
        });
    }

    private void timeSom(int i) {
        try {
            Thread.sleep(i);
        } catch (Exception e) {
        }
    }


    private void setConfigurationCronometro() {
        //002
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = ContextFrame.second / 3600;
                int minute = (ContextFrame.second % 3600) / 60;
                int seconds = ContextFrame.second % 60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minute, seconds);
                //ContextFrame.time.setText(time);
                if (ContextFrame.running) {
                    ContextFrame.second++;
                    String composta = String.format("%s%s%s%s", ContextFrame.operationModelRepository.getOperationID()
                            , ContextFrame.operationModelRepository.getRouterID(),
                            ContextFrame.operationModelRepository.getStoreID(),
                            ContextFrame.operationModelRepository.getParternID());
                    String latlng = String.format("%s,%s", ContextFrame.latiudeActive, ContextFrame.longitudeActive);
                    DeliveryStateModel deliveryStateModel = new DeliveryStateModel();
                    deliveryStateModel.setDeliveryid(String.valueOf(ContextFrame.operationModelRepository.getOperationID()));
                    deliveryStateModel.setDeviceid(ContextFrame.operationModelRepository.getDeviceID());
                    deliveryStateModel.setRouterid(ContextFrame.operationModelRepository.getRouterID());
                    deliveryStateModel.setStoreid(ContextFrame.operationModelRepository.getStoreID());
                    deliveryStateModel.setPartnerid(ContextFrame.operationModelRepository.getParternID());
                    deliveryStateModel.setUuidcomposta(composta);
                    deliveryStateModel.setLatlng(String.format(latlng));
                    deliveryStateModel.setNuvem(0);
                    deliveryStateModel.setBatery(ContextFrame.batteryTxt);
                    ContextFrame.deliveryStateReaderDbHelper.insert(deliveryStateModel);

                }
                handler.postDelayed(this, 1000);
            }
        });


    }

    private void initPlayer() {
        ContextFrame.Player = new Playler(ContextFrame.contextActivity);

    }

    private void initMapGoogle() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void configureCompatibiliteApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }


    }

    public void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }


    private void retrieveReceivedData() {
      try{
          Bundle extras = getIntent().getExtras();
          resetListsData();

          if (extras != null) {
              String json = extras.getString(Constants.ROUTER_OPERATION_SELECIONADA);
              ContextFrame.operationModelRepository = new Gson().fromJson(json, OperationModelRepository.class);
              ContextFrame.rotaActiva = String.valueOf(ContextFrame.operationModelRepository.getRouterID());
              ContextFrame.instructions = new RouterInMap().getInstructions(ContextFrame.operationModelRepository.getRouterMap());
              for (RouterMap r : ContextFrame.instructions) {
                  String[] latLng = r.pontos.split("\\,");
                  Locations locations = new Locations(r.id, r.command, Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                  ContextFrame.myPath.add(locations);
              }
              ProcessWaypoints processWaypoints = new ProcessWaypoints(getWaypoints(ContextFrame.instructions));
              ContextFrame.distancePerKm = processWaypoints.kmDividedByPoints();
              ContextFrame.pointInRouter = processWaypoints.numberOfPointsOnTheRoute();
              ContextFrame.zonas = new RouterInMap().getAreasStill(ContextFrame.operationModelRepository.getZonasJson());


          }
      }catch (Exception e){
          Toast.makeText(RouteGuidanceActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
      }
    }

    private void resetListsData() {
        if(ContextFrame.myPath!=null){
            ContextFrame.myPath.clear();
        }
        if(ContextFrame.instructions!=null){
            ContextFrame.instructions.clear();
        }
        if(ContextFrame.markers!=null){
            ContextFrame.markers.clear();
        }
        if(ContextFrame.listSequenciePoints!=null){
            ContextFrame.listSequenciePoints.clear();
        }

        if(ContextFrame.lidos!=null){
            ContextFrame.lidos.clear();
        }
        if(ContextFrame.zonas!=null){
            ContextFrame.zonas.clear();
        }
        if(ContextFrame.markersReads!=null){
            ContextFrame.markersReads.clear();
        }

        if(ContextFrame.ponts!=null){
            ContextFrame.ponts.clear();
        }

    }


    private void setButtonStateStart(boolean running) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    ContextFrame.btn_start.setText("Navegando...");
                    ContextFrame.btn_start.setEnabled(false);
                } else {
                    ContextFrame.btn_start.setText("Iniciar >>");
                    ContextFrame.btn_start.setEnabled(false);
                }
            }
        });
    }

    private void showTheRouteStatusOnTheDisplay() {
       try{
        double distanceMade = (ContextFrame.previousSequence != 0) ? ContextFrame.distancePerKm * (ContextFrame.pointInRouter) : 0;
        int lestPoint = ContextFrame.pointInRouter - 1;
        double distanceMadePorcents = 0;

        if(lestPoint!=0){
            distanceMadePorcents = ((ContextFrame.distancePerKm * ContextFrame.previousSequence) * 100) / (ContextFrame.distancePerKm * (lestPoint));
        }
        ContextFrame.statuskmsRouter.setText((distanceMade > 1) ? String.format("%.2f", distanceMade) + " Km" : String.format("%.2f", distanceMade) + " M");
        ContextFrame.statusPorcentageRouter.setText(String.format("%.2f", distanceMadePorcents) + " %");
        autoCloseFinishOperation(distanceMadePorcents);
       }catch (Exception e){
           Toast.makeText(RouteGuidanceActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
       }

    }

    private void autoCloseFinishOperation(double distanceMadePorcents) {
        try {
            Toast.makeText(ContextFrame.contextActivity, String.valueOf(distanceMadePorcents), Toast.LENGTH_LONG).show();
            if (distanceMadePorcents == 100) {
                this.finishOperaion(ContextFrame.operationModelRepository);
                ContextFrame.initPoint=false;
            }
        }catch (Exception e){
            Toast.makeText(RouteGuidanceActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void finishOperaion(Object objetct) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OperationModelRepository item = (OperationModelRepository) objetct;
                ContextFrame.appOpeationsRepository.updateStateOperations(
                        String.valueOf(item.getOperationID()),
                        String.valueOf(item.getParternID()), 4);

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContextFrame.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        try {
                            resetStateOperation();
                            finish();
                            Intent intent = new Intent(ContextFrame.contextActivity, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }catch (Exception e){

                        }
                    }
                };
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.OnResponseType("OK", null);
                    }
                });
                builder.setTitle("Operação Finalizada..!");
                ContextFrame.dialog = builder.create();
                ContextFrame.dialog.show();

            }
        });


    }

    private void resetStateOperation() {
        ContextFrame.lastOperationAndActiveRoute.setProps(String.valueOf(Resources.LAST_ID_AND_POSSITION_OPERATION_CURRENTR));
        ContextFrame.lastOperationAndActiveRoute.setContent("");
        ContextFrame.lastOperationAndActiveRoute.setProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
        ContextFrame.lastOperationAndActiveRoute.setContent("");
        ContextFrame.previousSequence = 0;
        ContextFrame.navegationsReaderDbHelper.update(ContextFrame.lastOperationAndActiveRoute);

    }


    private List<RoutePoints> getWaypoints(List<RouterMap> instructions) {
        List<RoutePoints> routerPoints = new ArrayList<>();
        for (RouterMap rMap : instructions) {
            String[] latLon = rMap.pontos.split("\\,");
            try {
                routerPoints.add(new RoutePoints(Float.valueOf(latLon[0]), Float.valueOf(latLon[0])));
            } catch (Exception e) {
            }
        }


        System.out.println();
        return routerPoints;
    }

    private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        ContextFrame.mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        recalcularTrajeto();
        ContextFrame.mMap.setMyLocationEnabled(true);
        drawAreaOfacting();
    }

    private void recalcularTrajeto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) !=
                        PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) !=
                        PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String[] routerIdGeoSize = null;
        ContextFrame.mMap.setMyLocationEnabled(true);
        for (int next = 0; next < ContextFrame.instructions.size(); next++) {
            String[] latLng = Functions.split("\\,",ContextFrame.instructions.get(next).pontos);// ContextFrame.instructions.get(next).pontos.split("\\,");
            ContextFrame.markers.add(new Markers(latLng[0], latLng[1], String.format("Ponto : %s", next + 1)));
        }
        drawMapInMarkers();
    }

    private void drawAreaOfacting() {
        List<LatLng> area = new ArrayList<>();
        try{
            for (Zonas zona : ContextFrame.zonas) {
                String[] split = zona.content.split("\\|");
                for (int nextMakter = 0; nextMakter < split.length; nextMakter++) {
                    String market = split[nextMakter];
                    String[] marketSeparetedVirgule = market.split("\\,");
                    if (marketSeparetedVirgule.length == 2) {
                        if (!marketSeparetedVirgule[0].isEmpty() && !marketSeparetedVirgule[1].isEmpty()) {
                            if(Double.valueOf(marketSeparetedVirgule[0])!=0 && Double.valueOf(marketSeparetedVirgule[1])!=0){
                                area.add(new LatLng(Double.valueOf(marketSeparetedVirgule[0]), Double.valueOf(marketSeparetedVirgule[1])));
                            }

                        }
                    }
                }
            }

            if(area.size() > 0){
                PolygonOptions polygonOptions = new PolygonOptions();
                polygonOptions.addAll(area);
                polygonOptions.fillColor(Color.argb(3, 3, 0, 255))
                        .strokeColor(Color.BLACK)
                        .clickable(true)
                        .strokeWidth(4);
                ContextFrame.mMap.addPolygon(polygonOptions);

            }
            Locations locations = ContextFrame.myPath.get(ContextFrame.previousSequence);
            LatLng marketInit = new LatLng(locations.getLat(), locations.getLng());
            if (!ContextFrame.initPoint) {
                ContextFrame.mMap.addMarker(new MarkerOptions()
                        .position(marketInit)
                        .title("Inicial Rota..."));
                ContextFrame.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marketInit, 11));
            }
        }catch (Exception e){

        }

    }


    private void knowWhereIAm(double latitude, double longitude) {
        //001
        int diff = 0;
        double distance = 0;
        int size = ContextFrame.myPath.size();
        String marker = initString();
        actionPhoto(false);



        try{
            if (ContextFrame.initPoint) {
                ContextFrame.PointsRead++;
                if (!ContextFrame.setOperationInit) {
                    ContextFrame.appOpeationsRepository.updateStateOperations(
                            String.valueOf(ContextFrame.operationModelRepository.getOperationID()),
                            String.valueOf(ContextFrame.operationModelRepository.getParternID()), Enum.STATUS_IS_OPEN_OPERATION);
                    actionPhoto(true);
                    ContextFrame.setOperationInit = true;
                }


                for (int next = (ContextFrame.previousSequence ); next < size; next++) {
                    marker = String.format("%s%s", ContextFrame.myPath.get(next).getLat(), ContextFrame.myPath.get(next).getLng());
                    ContextFrame.currentMarket = ContextFrame.myPath.get(next);

                    if (ContextFrame.lidos.indexOf(marker) == -1) {
                        diff = Math.abs(ContextFrame.previousSequence - next);
                        distance = (distanceBetweenTwoPoints(latitude, longitude,
                                ContextFrame.myPath.get(next).getLat(), ContextFrame.myPath.get(next).getLng()));


                        System.out.println("#8  Distanc[ " + distance + "] Direcao [" + ContextFrame.myPath.get(next).getDirecao() + "] Passo: " + next);
                        if (distance < ContextFrame.raio && (Math.abs(ContextFrame.previousSequence - next) == 1)) {
                            ContextFrame.ultimo_comando = ContextFrame.myPath.get(next).getDirecao();
                            ContextFrame.previousSequence = next;
                            drawMapInMarkers();
                            showTheRouteStatusOnTheDisplay();
                            performAudioGuidance();
                            ContextFrame.positionError = 0;
                            ContextFrame.lidos.add(marker);
                            RouterMovimentModel moviments = new RouterMovimentModel();
                            moviments.setAction("F");
                            moviments.setIdKeys(String.valueOf(ContextFrame.currentMarket.getId()));
                            moviments.setStartPoints(String.valueOf(ContextFrame.previousSequence));
                            moviments.setDeviceID(ContextFrame.operationModelRepository.getDeviceID());
                            moviments.setDeliveryId(String.valueOf(ContextFrame.operationModelRepository.getOperationID()));
                            //#2023
                            if(!ContextFrame.operationModelRepository.getDeviceID().isEmpty()){
                                ContextFrame.routerMovimentDbHelper.insert(moviments);
                                ContextFrame.PointsRead =0;
                                final long update = ContextFrame.navegationsReaderDbHelper.update(getRouterCurrent(next));

                                System.out.println("#233 "+update);
                                System.out.println("#233 "+getRouterCurrent(next));
                                System.out.println("#233 "+next);


                            }
                            System.out.println("#12 Feito ["+ContextFrame.previousSequence+"] ");
                            StringBuffer losg = new StringBuffer();
                            for (String v : ContextFrame.lidos) {
                                losg.append(v);
                            }
                        } else if (diff != 1) {
                            ContextFrame.positionError++;
                        }
                    }
                }
            }
            if (ContextFrame.positionError == ContextFrame.startBoxMensageError) {
                ContextFrame.responseGenerics.OnResponseType(Resources.ERROR_POINT_NOT_FOUND, null);
                return;
            }
        }catch (Exception e){
                e.printStackTrace();
        }


    }

    private NavegationModel getRouterCurrent(int next) {
        String latLon = String.format("%s,%s", ContextFrame.myPath.get(next).getLat(), ContextFrame.myPath.get(next).getLng());
        ContextFrame.lastOperationAndActiveRoute.setProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
        ContextFrame.lastOperationAndActiveRoute.setContent(String.format("%s|%s|%s|%s",ContextFrame.operationModelRepository.getOperationID(),ContextFrame.rotaActiva, latLon, next));
        System.out.println("#233 **"+ ContextFrame.lastOperationAndActiveRoute.getContent());

        return ContextFrame.lastOperationAndActiveRoute;
    }

    private String initString() {
        return "";
    }

    public void drawMapInMarkers() {
        try{
            System.out.println("#5 processando..(Pintando map");
            ContextFrame.mMap.clear();
            drawAreaOfacting();
            int size = ContextFrame.markers.size();
            LatLng p1 = null, p2 = null;
            List<LatLng> lts = new ArrayList<>();
            boolean pontoTravado = false, nextPonits = false;
            for (int next = 0; next < size; next++) {
                p1 = new LatLng(
                        Double.parseDouble(ContextFrame.markers.get(next).getLat()),
                        Double.parseDouble(ContextFrame.markers.get(next).getLng()));

                if (next < ContextFrame.previousSequence && ContextFrame.previousSequence != 0) {
             /*   MarkerOptions pt = new MarkerOptions().position(p1)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                pt.title("Ponto : " + (next));
                gui.mMap.addMarker(pt);*/
                } else {
                    if ((next == ContextFrame.previousSequence || ContextFrame.previousSequence == 0) && !pontoTravado) {
                        MarkerOptions pt = new MarkerOptions().position(p1).
                                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                        pt.title("Inicio....").snippet("Blah");
                        ContextFrame.mMap.addMarker(pt);
                        pontoTravado = true;
                    } else {
                        if (next > ContextFrame.previousSequence) {
                            if (!nextPonits) {
                                MarkerOptions pt = new MarkerOptions().position(p1).
                                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                pt.title("Ponto : " + (next + 1));
                                ContextFrame.mMap.addMarker(pt);
                                nextPonits = true;
                            }

                        }
                    }
                }
                lts.add(p1);

                ContextFrame.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lts.get(lts.size() - 1), 14.0f));

            }

        }catch (Exception e){}

    }


    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);
        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);
        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void drawMapInLines() {
        ContextFrame.mMap.clear();
        int size = ContextFrame.markers.size();
        LatLng p1 = null;
        List<LatLng> lts = new ArrayList<>();
        for (int next = 0; next < size; next++) {
            p1 = new LatLng(
                    Double.parseDouble(ContextFrame.markers.get(next).getLat()),
                    Double.parseDouble(ContextFrame.markers.get(next).getLng()));
            lts.add(p1);
        }
        PolylineOptions gg = new PolylineOptions();
        for (LatLng df : lts) {
            gg.add(df);
        }
        Polyline line = ContextFrame.mMap.addPolyline(gg);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_router_activy, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()) {
                   case R.id.menulockscreen:
                       ContextFrame.uncheckedlayot.setVisibility(View.VISIBLE);
                       return true;

                   case R.id.finishoperation:
                       stopRouterGps();
                       return true;

                   case R.id.menucloseoperation:
                    this.finishOperaion(ContextFrame.operationModelRepository);
                    return true;

                   case R.id.menusyncronized:

                       return true;




            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private static class ContextFrame {
        public final List<Markers> markers = new ArrayList<>();
        public final List<Locations> myPath = new ArrayList<>();
        private final List<String> lidos = new ArrayList<>();
        public ActivityResultLauncher<Intent> receiveAnswerFromChildren;
        public Context contextActivity;
        public Toolbar toolbar;
        public OperationModelRepository operationModelRepository;
        public List<RouterMap> instructions;
        public List<Zonas> zonas;
        public GoogleMap mMap;
        public Playler Player;
        public int second = 0;
        public boolean running = false;
        public Button iniciar;
        public TextView time;
        public FusedLocationProviderClient fusedLocationClient;
        public Handler HandlerGps;
        public double latiudeActive;
        public double longitudeActive;
        public Button btnIr, btnPhoto, btnFinalizar;
        public Button photo;
        public Button start;
        public double raio = 45;//metros
        public int previousSequence = 0;
        public List<GeoCoordinate> markersReads = new ArrayList<GeoCoordinate>();
        public int distance = 0;
        public int distanceStartPoint = 0;
        public String ultimo_comando = "";
        public boolean initPoint = false;
        public boolean init_comando = false;
        public NavegationsReaderDbHelper navegationsReaderDbHelper;
        public NavegationModel lastOperationAndActiveRoute;
        public RouterMovimentBusiness routerMovimentDbHelper;
        public int timeProcessSound = 200;
        public TextView statusPorcentageRouter;
        public TextView statuskmsRouter;
        public Button btnmini;
        public ActionBar actionBar;
        public Button btn_utimo_comando;
        public boolean runningPlayer = false;
        public Playler playlerRouter;
        public Button btn_start;
        public String rotaActiva;
        public int pointInRouter;
        public double distancePerKm;
        public List ponts = new ArrayList<>();
        public String actuvePoint_initPoint;
        public OnResponse responseGenerics;
        public Handler handlerMessage;
        public Button btn_stop;
        public AlertDialog dialog;
        public int positionError = 0;
        public int startBoxMensageError = 2000;

        public HashSet<String> listSequenciePoints;
        public DeliveryStateReaderDbHelper deliveryStateReaderDbHelper;
        public String batteryTxt;
        public AppOpeationsRepository appOpeationsRepository;
        public boolean setOperationInit = false;
        public Uri cam_uri;
        public PhotosPartnersModel photosPartnersModel;
        public ServicesHttp servicesHttp;
        public ServerConfiguration conf;
        public ConfigurationsUser configurationsAdapter;
        public UserLoginCast userLoginCast;
        public Dialog alerta;
        public Locations currentMarket;
        public String phoneInformationPhoneId;
        public Config userDevice;
        public ConstraintLayout uncheckedlayot;
        public LinearLayout unlock;
        public int PointsRead=0;
        public ProgressDialog pd;
        public LinearLayout loadding;
        public LinearLayout main;
        ActivityResultLauncher<Uri> takeAPhoto;
        ActivityResultLauncher<Intent> startCamera;
        String currentPhotoPath;
    }


}
