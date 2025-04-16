package br.com.stilldistribuidora.partners.views;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
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
import android.content.res.Configuration;
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
import android.os.PersistableBundle;
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
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
import br.com.stilldistribuidora.partners.Casts.CoordinateDto;
import br.com.stilldistribuidora.partners.Casts.Market;
import br.com.stilldistribuidora.partners.Casts.RouterInMap;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Commom.Enum;
import br.com.stilldistribuidora.partners.Commom.Functions;

import br.com.stilldistribuidora.partners.Commom.MarketIgn;
import br.com.stilldistribuidora.partners.Commom.MarketRouter;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Models.RouterMap;
import br.com.stilldistribuidora.partners.Models.Zonas;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.Services.ServicesApiStill;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.Resources;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.lib.PartenerHelpes;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;

public class SoundsCarActivity   extends ActivityBaseApp implements OnMapReadyCallback {
    private static final ActivityContext ActivityContext = new ActivityContext();


    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            ActivityContext.batteryTxt = String.valueOf(level);
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_operation_router_2);
        ActivityContext.contextActivity = this;
        ConfigDataAccess configModel = new ConfigDataAccess(ActivityContext.contextActivity);
        ActivityContext.userDevice = (Config) configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE));
        ActivityContext.toolbar= (Toolbar) findViewById(R.id.toolbar);
        ActivityContext.actionBar = getActionBar();
        ActivityContext.uncheckedlayot = (ConstraintLayout)findViewById(R.id.uncheckedlayot);
        ActivityContext.unlock =(LinearLayout)findViewById(R.id.unlock);
        ActivityContext.main = (LinearLayout)findViewById(R.id.main);
        ActivityContext.loadding = (LinearLayout)findViewById(R.id.loadding);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        ActivityContext.phoneInformationPhoneId = new PhoneInformations(ActivityContext.contextActivity).getPhoneId();
        InitPermisionGeral();
        keepScreenOn();
        retrieveReceivedData();
        initMapGoogle();
        initPlayer();
        configureCompatibiliteApp();
        setConfigurationCronometro();
        initActivityContext();
        initGps();
        initCamera();
        initEvents();
        setComandsPainel(View.GONE, View.GONE, View.GONE);
        initModelDataBase();
        initModelDataBaseConfiruations();
        publicReceivedInterfaceEvents();
        actionPhoto(false);
        ActivityContext.navegationsReaderDbHelper.initInfo();
        setToolbar();
        isOperationToOpen();
        isResetOperation();
        ActivityContext.mapsNavegation = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            Intent data = result.getData();
                            //SomeClass obj = (obj) data.getSerializableExtra("Object");
                            finishActivity(ActivityContext.GOOGLE_NAV);

                        }
                    }
                }
        );

        MarketRouter marketRouter;
        ActivityContext.marketRouters = new ArrayList<>();
        int next=0;
        for(Locations market : ActivityContext.myPath) {
            if(market.getDirecao().equals("%")){
                ActivityContext.marketRouters.get(next-1).
                        ignoredPoints.add(new MarketIgn(market.getLat(),market.getLng(),
                                market.getDirecao(),market.getId()));
            }else{
                marketRouter =  new MarketRouter();
                marketRouter.start =new MarketIgn(market.getLat(),market.getLng(),
                        market.getDirecao(),market.getId());
                if(next !=0){
                    ActivityContext.marketRouters.get(next-1).fim =   marketRouter.start;
                }
                ActivityContext.marketRouters.add(marketRouter);
                next++;
            }
        }







/*


   MarketRouter marketRouter.ignoredPoints.add(
                        new MarketIgn(market.getLat(),market.getLng(),
                                market.getDirecao(),market.getId())
                );




*/
/*

        Uri mapIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia&waypoints=Google+Sydney%7CSydney+Opera+House");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
*/


        
    }




    public void  openGoogleNavegador(CoordinateDto coordinateDto){

        try {
            System.out.println("#888 * "+coordinateDto.larLngToString6());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + coordinateDto.larLngToString6()+ "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(mapIntent);
                //ActivityContext.mapsNavegation.launch(mapIntent);
                Display d = getWindowManager()
                        .getDefaultDisplay();
                Point p = new Point();
                d.getSize(p);
                int width = p.x;
                int height = p.y;
                Rational ratio= new Rational(width, height);
                PictureInPictureParams.Builder  pip_Builder= null;
                pip_Builder = new PictureInPictureParams.Builder();
                pip_Builder.setAspectRatio(ratio).build();
                enterPictureInPictureMode(pip_Builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    private  void isResetOperation(){
        List<NavegationModel> lastNavegations = ActivityContext.navegationsReaderDbHelper.getByProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
        String[] routerIdGeoSize = null;
        if (lastNavegations.size() > 0) {
            String content = lastNavegations.get(0).getContent();
            routerIdGeoSize = Functions.split("\\|",content);
        }
        String deliveryCode=  String.valueOf(ActivityContext.operationModelRepository.getOperationID());

        String codeCurrency = deliveryCode.trim();
        String codeCurrencyStore = routerIdGeoSize[0].trim();


        if(!codeCurrency.equals(codeCurrencyStore)){
            resetStateOperation();
            return;
        }
        if (routerIdGeoSize != null && routerIdGeoSize.length == 5  &&
                deliveryCode.trim().equals(routerIdGeoSize[0].trim()) ) {

            String[] finalRouterIdGeoSize = routerIdGeoSize;
            ActivityContext.previousSequence = Integer.parseInt(finalRouterIdGeoSize[3]);
            ActivityContext.laps = Integer.parseInt(routerIdGeoSize[4]);
        }



       /* if (routerIdGeoSize != null && routerIdGeoSize.length == 4  &&
                deliveryCode.trim().equals(routerIdGeoSize[0].trim()) ) {

            String[] finalRouterIdGeoSize = routerIdGeoSize;
            ActivityContext.previousSequence = Integer.parseInt(finalRouterIdGeoSize[3]);
        }*/



        double distance = 0;
        distance = (int) distanceBetweenTwoPoints(ActivityContext.latiudeActive, ActivityContext.longitudeActive,
                ActivityContext.myPath.get(ActivityContext.previousSequence).getLat(),ActivityContext.myPath.get(ActivityContext.previousSequence).getLng());


        Integer operationCurrent = ActivityContext.operationModelRepository.getOperationID();
        Integer operationSelected =(routerIdGeoSize[0].trim().isEmpty()) ? 0 :  Integer.valueOf(routerIdGeoSize[0]);
        boolean isEquals = operationCurrent.equals(operationSelected);
        if (ActivityContext.previousSequence > 0 && isEquals && distance < ActivityContext.raio) {
            ActivityContext.initPoint = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContext.contextActivity);
            String[] finalRouterIdGeoSize = routerIdGeoSize;
            double finalDistance = distance;
            builder.setPositiveButton("Continuar >> ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityContext.running = true;
                    ActivityContext.ultimo_comando = "I";
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
        List<OperationModelRepository> operationAr = ActivityContext.appOpeationsRepository.getAll(String.valueOf(Enum.STATUS_IS_OPEN_OPERATION));
        boolean dif=true;
        OperationModelRepository  operationOpen =  new OperationModelRepository();

        for(OperationModelRepository m : operationAr){
            operationOpen = m;
            Integer integer = Integer.valueOf(m.getOperationID());
            Integer integer1 = Integer.valueOf(ActivityContext.operationModelRepository.getOperationID());
            if(integer.equals(integer1)){
                dif=false;
                break;
            }
        }
        if (operationAr.size() != 0  &&  dif ) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityContext.contextActivity);
            builder.setTitle("Ação Cancelada.");
            builder.setMessage("Operação ("+operationOpen.getOperationID()+" "+operationOpen.getCreatedAt()+") em Aberto. \npara Continuar em Outra Operação.");
            builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    ActivityContext.running=false;
                    ActivityContext.alerta.dismiss();
                    Intent intent = new Intent(ActivityContext.contextActivity, MyOperations.class);
                    intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, Enum.STATUS_IS_OPEN_OPERATION);
                    startActivity(intent);
                    finish();
                }

            });
            ActivityContext.alerta = builder.create();
            ActivityContext.alerta.show();
            return;
        }
    }

    private void setToolbar() {
        setSupportActionBar(ActivityContext.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }




    private void getDeviceApi() {
        ActivityContext.operationModelRepository.setDeviceID(ActivityContext.userDevice.getDataJson());
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
        ActivityContext.currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void startActivityOnResult() {
        ActivityContext.receiveAnswerFromChildren = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Constants.FINISH_OPERATION_OK_ACTIONS) {


                        }

                    }
                });

        ActivityContext.startCamera = registerForActivityResult(
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
                                        String realPathFromURI = getRealPathFromURI(ActivityContext.cam_uri);
                                        System.out.println("#4 " + ActivityContext.cam_uri.getPath());
                                        photoFile = createImageFile();
                                        String[] geo = (((String) obj).split("\\|"))[0].split("\\,");
                                        PhotoEntity photoEntity = new PhotoEntity();
                                        photoEntity.setCreated_at(PartenerHelpes.getDate());
                                        photoEntity.setUri(realPathFromURI);
                                        photoEntity.setSync("0");
                                        photoEntity.setDelivery_id(String.valueOf(ActivityContext.operationModelRepository.getOperationID()));
                                        photoEntity.setLatitude(geo[0]);
                                        photoEntity.setLongitude(geo[1]);
                                        photoEntity.setPic_uid("0");
                                        photoEntity.setType("1");
                                        photoEntity.setPartners_id(String.valueOf(ActivityContext.operationModelRepository.getParternID()));
                                        photoEntity.setDeviceId(ActivityContext.userDevice.getDataJson());
                                        if (ActivityContext.photosPartnersModel.save(photoEntity) > 0) {
                                            Toast.makeText(ActivityContext.contextActivity, "Foto Salva Com Sucesso..!", Toast.LENGTH_SHORT).show();
                                        }
                                        try {
                                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(ActivityContext.cam_uri.toString()));
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


    private void initActivityContext() {
        ActivityContext.handlerMessage = new Handler();
        ActivityContext.statusPorcentageRouter = findViewById(R.id.statusPorcentageRouter);
        ActivityContext.statuskmsRouter = findViewById(R.id.statuskmsRouter);
        ActivityContext.btnmini = findViewById(R.id.btnmini);
        ActivityContext.btn_start = findViewById(R.id.btn_start);
        ActivityContext.btn_stop = findViewById(R.id.btn_stop);
        ActivityContext.btnPhoto = findViewById(R.id.btnPhoto);

        ActivityContext.photosPartnersModel = new PhotosPartnersModel(ActivityContext.contextActivity);
        ActivityContext.btn_utimo_comando = findViewById(R.id.btn_utimo_comando);
        ActivityContext.playlerRouter = new Playler(ActivityContext.contextActivity);
        ActivityContext.appOpeationsRepository = new AppOpeationsRepository(ActivityContext.contextActivity);
        ActivityContext.conf = new ServerConfiguration(ActivityContext.contextActivity);
        ActivityContext.servicesHttp = new ServicesHttp(ActivityContext.contextActivity, ActivityContext.conf, CallBackOnResponseGlobalApi());
        ActivityContext.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(this));
        String userLogged = ActivityContext.conf.getUserLogged();
        ActivityContext.userLoginCast = new Gson().fromJson(userLogged, UserLoginCast.class);
        ActivityContext.laps =0;

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
        ActivityContext.cam_uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, ActivityContext.cam_uri);
        //startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE); // OLD WAY
        ActivityContext.startCamera.launch(cameraIntent);                // VERY NEW WAY
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
        ActivityContext.navegationsReaderDbHelper = new NavegationsReaderDbHelper(ActivityContext.contextActivity);

        ActivityContext.routerMovimentDbHelper = new RouterMovimentBusiness(new RouterMovimentHelper(ActivityContext.contextActivity));
        ActivityContext.lastOperationAndActiveRoute = new NavegationModel();
        ActivityContext.deliveryStateReaderDbHelper = new DeliveryStateReaderDbHelper(ActivityContext.contextActivity);
    }

    private void initModelDataBaseConfiruations() {
        ActivityContext.lastOperationAndActiveRoute.setProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));


    }

    private void setComandsPainel(int startRouter, int photo, int ir) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  ActivityContext.start.setVisibility(startRouter);
                // ActivityContext.photo.setVisibility(photo);
                //  ActivityContext.btnIr.setVisibility(ir);

            }
        });

    }

    private void initGps() {
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        boolean providerEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!providerEnabled){
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityContext.contextActivity);
            OnResponse click = new OnResponse() {
                @Override
                public void OnResponseType(String type, Object object) {
                    ActivityContext.dialog.dismiss();
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
            ActivityContext.dialog = builder.create();
            ActivityContext.dialog.show();
            return ;
        }
        ActivityContext.fusedLocationClient = LocationServices.getFusedLocationProviderClient(ActivityContext.contextActivity);
        ActivityContext.HandlerGps = new Handler();
        ActivityContext.HandlerGps.post(new Runnable() {
            @Override
            public void run() {
                InitPermisionGeral();

                if (ActivityCompat.checkSelfPermission(ActivityContext.contextActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityContext.contextActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                ActivityContext.fusedLocationClient.getLastLocation().addOnSuccessListener(SoundsCarActivity.this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    ActivityContext.latiudeActive = location.getLatitude();
                                    ActivityContext.longitudeActive = location.getLongitude();
                                    ActivityContext.distance = 0;
                                    ActivityContext.distanceStartPoint = getDistanceInitMarket();
                                    System.out.println("#888888 "+ getDistanceInitMarket());
                                    if( getDistanceInitMarket() < ActivityContext.raio ){
                                        ActivityContext.previousSequence++;
                                        if(ActivityContext.previousSequence == ActivityContext.myPath.size()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContext.contextActivity);
                                            builder.setMessage(R.string.continue_router)
                                                    .setPositiveButton(R.string.continue_router, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            ActivityContext.previousSequence = 0;
                                                            ActivityContext.laps++;
                                                            ActivityContext.btn_start.callOnClick();
                                                        }
                                                    })
                                                    .setNegativeButton(R.string.cancel_finalizar, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            finishOperaion(ActivityContext.operationModelRepository);
                                                            ActivityContext.initPoint = false;
                                                        }
                                                    });
                                            builder.create();
                                            builder.show();
                                            return;
                                        }
                                        ActivityContext.positionError = 0;
                                        RouterMovimentModel moviments = new RouterMovimentModel();
                                        ActivityContext.currentMarket = ActivityContext.myPath.get(ActivityContext.previousSequence);
                                        moviments.setAction("F");
                                        moviments.setIdKeys(String.valueOf(ActivityContext.currentMarket.getId()));
                                        moviments.setStartPoints(String.valueOf(ActivityContext.previousSequence));
                                        moviments.setDeviceID(ActivityContext.userDevice.getDataJson() );
                                        moviments.setDeliveryId(String.valueOf(ActivityContext.operationModelRepository.getOperationID()));
                                        moviments.setLap(ActivityContext.laps);
                                        ActivityContext.routerMovimentDbHelper.insert(moviments);
                                        ActivityContext.PointsRead =0;
                                        ActivityContext.navegationsReaderDbHelper.update(getRouterCurrent(ActivityContext.previousSequence));
                                        if(ActivityContext.previousSequence < ActivityContext.myPath.size()) {
                                            openGoogleNavegador(
                                                    new CoordinateDto(
                                                            ActivityContext.myPath.get(ActivityContext.previousSequence).getLat(),
                                                            ActivityContext.myPath.get(ActivityContext.previousSequence).getLng()));
                                        }
                                        System.out.println("#333 "+ActivityContext.previousSequence+"   **  "+ActivityContext.myPath.size());

                                    }
                                }
                            }
                        });
                ActivityContext.HandlerGps.postDelayed(this, 3*1000);
            }
        });
    }



    private int getDistanceInitMarket() {

        if(ActivityContext.previousSequence == ActivityContext.myPath.size()) return 1000;
        return ((int) distanceBetweenTwoPoints(ActivityContext.latiudeActive,
                ActivityContext.longitudeActive,
                ActivityContext.myPath.get(ActivityContext.previousSequence).getLat(),
                ActivityContext.myPath.get(ActivityContext.previousSequence).getLng()));


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
        ActivityContext.statusPorcentageRouter.setText("0%");
        ActivityContext.statuskmsRouter.setText("0km");
        ActivityContext.btnmini.setVisibility(View.GONE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ActivityContext.btnmini.setVisibility(View.VISIBLE);
            ActivityContext.btnmini.setEnabled(true);
            ActivityContext.btnmini.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Display d = getWindowManager()
                            .getDefaultDisplay();
                    Point p = new Point();
                    d.getSize(p);
                    int width = p.x;
                    int height = p.y;

                    Rational ratio= new Rational(width, height);

                         ActivityContext.pip_Builder
                            = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        ActivityContext.pip_Builder = new PictureInPictureParams
                                .Builder();
                        ActivityContext.pip_Builder.setAspectRatio(ratio).build();
                        enterPictureInPictureMode(ActivityContext.pip_Builder.build());
                    }


                }
            });
        }


        ActivityContext.btn_utimo_comando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAudioGuidance();
            }
        });

        ActivityContext.btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRouterGps();
            }
        });



        ActivityContext.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityContext.previousSequence < ActivityContext.myPath.size()) {
                    ActivityContext.running = true;
                    if (ActivityContext.laps != 0) {
                        ActivityContext.laps = ActivityContext.laps;
                    } else {
                        ActivityContext.laps = 1;
                    }
                    ActivityContext.navegationsReaderDbHelper.update(getRouterCurrent(ActivityContext.previousSequence));
                    List<LatLng> routers = new ArrayList<>();
                    ActivityContext.mMap.clear();
                    MarketRouter Mrouter = ActivityContext.marketRouters.get(ActivityContext.previousSequence);
                    routers.add(new LatLng(Mrouter.start.lan,Mrouter.start.lng));
                    for(MarketIgn p : Mrouter.ignoredPoints){
                        routers.add(new LatLng(p.lan,p.lng));
                    }
                    if(Mrouter.fim!=null){
                        routers.add(new LatLng(Mrouter.fim.lan,Mrouter.fim.lng));
                    }
                    PolylineOptions mapPolyline = new PolylineOptions();
                    mapPolyline.width(25)
                            .color(Color.BLUE)
                            .geodesic(true);
                    for (LatLng mk : routers) {
                        mapPolyline.add(mk);
                    }

                    if(ActivityContext.previousSequence < ActivityContext.marketRouters.size()-2) {
                        Mrouter = ActivityContext.marketRouters.get(ActivityContext.previousSequence + 1);
                        routers.add(new LatLng(Mrouter.start.lan, Mrouter.start.lng));
                        for (MarketIgn p : Mrouter.ignoredPoints) {
                            routers.add(new LatLng(p.lan, p.lng));
                        }
                        routers.add(new LatLng(Mrouter.fim.lan, Mrouter.fim.lng));
                        mapPolyline = new PolylineOptions();
                        mapPolyline.width(25)
                                .color(Color.BLUE)
                                .geodesic(true);
                        for (LatLng mk : routers) {
                            mapPolyline.add(mk);
                        }
                    }
                    PolylineOptions finalMapPolyline = mapPolyline;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityContext.mMap.addPolyline(finalMapPolyline);
                        }
                    });

                    if(ActivityContext.previousSequence < ActivityContext.marketRouters.size()-1) {
                        ActivityContext.previousSequence++;
                    }
                    System.out.println("##2 "+ActivityContext.previousSequence);



                 /*   List<LatLng> routers = new ArrayList<>();
                   // routers.add(new LatLng(ActivityContext.latiudeActive,ActivityContext.longitudeActive));
                    for(Locations m : ActivityContext.myPath) {
                       /// routers.add(new LatLng(m.getLat(), m.getLng()));
                    }
                    routers.add(new LatLng(ActivityContext.myPath.get(0).getLat(),ActivityContext.myPath.get(0).getLng()));
                    routers.add(new LatLng(ActivityContext.myPath.get(1).getLat(),ActivityContext.myPath.get(1).getLng()));
                    routers.add(new LatLng(ActivityContext.myPath.get(2).getLat(),ActivityContext.myPath.get(2).getLng()));
                    PolylineOptions mapPolyline = new PolylineOptions();
                    mapPolyline.width(25)
                    .color(Color.BLUE)
                    .geodesic(true);
                    for (LatLng mk : routers) {
                        mapPolyline.add(mk);
                    }

                    ActivityContext.mMap.addPolyline(mapPolyline);

                  *//*openGoogleNavegador(
                            new CoordinateDto(
                                    ActivityContext.myPath.get(ActivityContext.previousSequence).getLat(),
                                    ActivityContext.myPath.get(ActivityContext.previousSequence).getLng()));*//*
*/

                }
            }
        });


        ActivityContext.btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCamera();
            }
        });

        ActivityContext.unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContext.uncheckedlayot.setVisibility(View.GONE);
            }
        });




    }


    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if(!isInPictureInPictureMode){
            getApplication().startActivity(new Intent(this, getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }

    }

    private void stopRouterGpsOperation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContext.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        if (type.equals("STOP")) {
                            ActivityContext.running = false;
                            ActivityContext.ultimo_comando = "";
                            ActivityContext.initPoint = false;
                            ActivityContext.init_comando = false;
                            setButtonStateStart(false);
                            finish();
                        }
                        ActivityContext.dialog.dismiss();
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
                ActivityContext.dialog = builder.create();
                ActivityContext.dialog.show();
            }
        });


    }


    private void stopRouterGps() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContext.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        if (type.equals("STOP")) {
                            ActivityContext.running = false;
                            ActivityContext.ultimo_comando = "";
                            ActivityContext.initPoint = false;
                            ActivityContext.init_comando = false;
                            setButtonStateStart(false);
                            finish();
                        }
                        ActivityContext.dialog.dismiss();
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
                ActivityContext.dialog = builder.create();
                ActivityContext.dialog.show();
            }
        });


    }


    private void publicReceivedInterfaceEvents() {
        ActivityContext.responseGenerics = new OnResponse() {
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
        ActivityContext.playlerRouter.gpsNotFound();
        timeSom(ActivityContext.timeProcessSound);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContext.contextActivity);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityContext.currentMarket = ActivityContext.myPath.get(ActivityContext.previousSequence);
                        RouterMovimentModel moviments = new RouterMovimentModel();
                        moviments.setAction("C");
                        moviments.setIdKeys(String.valueOf(ActivityContext.currentMarket.getId()));
                        moviments.setStartPoints(String.valueOf(ActivityContext.previousSequence));
                        moviments.setDeviceID(ActivityContext.userDevice.getDataJson());
                        moviments.setDeliveryId(String.valueOf(ActivityContext.operationModelRepository.getOperationID()));
                        moviments.setLap(ActivityContext.laps);
                        ActivityContext.routerMovimentDbHelper.insert(moviments);
                        ActivityContext.previousSequence++;
                        ActivityContext.positionError = 0;
                        ActivityContext.navegationsReaderDbHelper.update(getRouterCurrent(ActivityContext.previousSequence-1));
                      //  drawMapInMarkers();
                        showTheRouteStatusOnTheDisplay();
                        System.out.println("#12 cancelado ["+ActivityContext.previousSequence+"] ");

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
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContext.contextActivity);
                builder.setPositiveButton(R.string.navegar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse(ActivityContext.actuvePoint_initPoint));
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
            System.out.println("#23 " + ActivityContext.distanceStartPoint);
            if (ActivityContext.running == false) {

                ActivityContext.running = true;
                ActivityContext.ultimo_comando = "";
                ActivityContext.initPoint = true;
                ActivityContext.distance = 0;
                if (!ActivityContext.init_comando) {
                    List<OperationModelRepository> operationAr = ActivityContext.appOpeationsRepository.getAll(String.valueOf(Enum.STATUS_IS_OPEN_OPERATION));
                    List<NavegationModel> lastNavegations = ActivityContext.navegationsReaderDbHelper.getByProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
                    boolean dif = true;
                    for (OperationModelRepository m : operationAr) {
                        Integer integer = Integer.valueOf(m.getOperationID());
                        Integer integer1 = Integer.valueOf(ActivityContext.operationModelRepository.getOperationID());
                        if (integer.equals(integer1)) {
                            dif = false;
                            break;
                        }
                    }


                    if (operationAr.size() != 0 && dif) {
                        //Cria o gerador do AlertDialog
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityContext.contextActivity);
                        //define o titulo
                        builder.setTitle("Ação Cancelada.");
                        //define a mensagem
                        builder.setMessage("Operação em Aberto. para Continuar em Outra Operação.");
                        //define um botão como positivo
                        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                ActivityContext.running = false;
                                ActivityContext.alerta.dismiss();
                                Intent intent = new Intent(ActivityContext.contextActivity, MyOperations.class);
                                intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, Enum.STATUS_IS_OPEN_OPERATION);
                                startActivity(intent);
                                finish();
                            }

                        });
                        //cria o AlertDialog
                        ActivityContext.alerta = builder.create();
                        //Exibe
                        ActivityContext.alerta.show();
                        return;
                    }
                    double distance = 0;
                    distance = (int) distanceBetweenTwoPoints(ActivityContext.latiudeActive, ActivityContext.longitudeActive,
                            ActivityContext.myPath.get(ActivityContext.previousSequence).getLat(),
                            ActivityContext.myPath.get(ActivityContext.previousSequence).getLng());

                    String[] routerIdGeoSize = null;
                    if (lastNavegations.size() > 0) {
                        String content = lastNavegations.get(0).getContent();
                        routerIdGeoSize = Functions.split("\\|", content);
                    }
                    if (routerIdGeoSize != null && routerIdGeoSize.length == 4 &&
                            routerIdGeoSize[1].equals(ActivityContext.rotaActiva) &&
                            ActivityContext.operationModelRepository.getOperationID().equals(routerIdGeoSize[0])) {
                        String[] finalRouterIdGeoSize = routerIdGeoSize;
                        ActivityContext.previousSequence = Integer.parseInt(finalRouterIdGeoSize[3]);
                    }


                    if (ActivityContext.previousSequence > 0 && ActivityContext.operationModelRepository.
                            getOperationID().equals(routerIdGeoSize[0])) {

                        ActivityContext.initPoint = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContext.contextActivity);
                        String[] finalRouterIdGeoSize = routerIdGeoSize;
                        double finalDistance = distance;
                        builder.setPositiveButton("Continuar >> ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityContext.running = true;
                                ActivityContext.ultimo_comando = "I";
                                performAudioGuidance();
                                startRouterGo(finalDistance);
                                setButtonStateStart(ActivityContext.running);
                                initStartServices();

                            }
                        });
                        builder.setTitle("Continuar Operação..!");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        setButtonStateStart(false);
                        ActivityContext.running = false;
                        ActivityContext.playlerRouter.direcaoPontoAzul();
                        timeSom(ActivityContext.timeProcessSound);
                        startRouterGo(ActivityContext.distanceStartPoint);

                    }
                }
            }
        }catch (Exception e){
            System.out.println("#100 =>" +e.getMessage());
        }
    }


    private void startRouterGo(double distance) {
        try {
            if (distance > ActivityContext.raio && ActivityContext.previousSequence == 0 && ActivityContext.initPoint) {
                ActivityContext.init_comando = false;
                invalideDistanceCurrent();
                ActivityContext.running=false;
                setButtonStateStart(ActivityContext.running);
            } else {
                ActivityContext.running=true;
                ActivityContext.init_comando = true;
                ActivityContext.ultimo_comando = "I";
                performAudioGuidance();
                setButtonStateStart(ActivityContext.running);
                getDeviceApi();
            }
        }catch (Exception e){
            System.out.println("#100 =>" +e.getMessage());
        }
    }

    private void initStartServices() {
        startService(new Intent(ActivityContext.contextActivity, ServicesApiStill.class));
    }
    private void runningRoute() {

        //9

        if (ActivityContext.operationModelRepository.getDeviceID() == null) {
            //Cria o gerador do AlertDialog
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityContext.contextActivity);
            //define o titulo
            builder.setTitle("Ação Cancelada.");
            //define a mensagem
            builder.setMessage("Não há Dispositivo Livres no Momento. para Continuar em Outra Operação.");
            //define um botão como positivo
            builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    ActivityContext.alerta.dismiss();
                    Intent intent = new Intent(ActivityContext.contextActivity, MyOperations.class);
                    intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, "2");
                    startActivity(intent);
                    finish();
                }

            });
            //cria o AlertDialog
            ActivityContext.alerta = builder.create();
            //Exibe
            ActivityContext.alerta.show();
            return;
        }

        //2

        ActivityContext.navegationsReaderDbHelper.update(getRouterCurrent(ActivityContext.previousSequence));

        ActivityContext.running = true;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("#233 navegando.... " + ActivityContext.init_comando+" dasd  +> "+ActivityContext.latiudeActive+"***"+ActivityContext.longitudeActive);
                    knowWhereIAm(ActivityContext.latiudeActive, ActivityContext.longitudeActive);
                    if (ActivityContext.init_comando) {
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
            ActivityContext.btnPhoto.setVisibility(View.VISIBLE);
        } else {
            ActivityContext.btnPhoto.setVisibility(View.GONE);
        }
    }

    private void invalideDistanceCurrent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ActivityContext.actuvePoint_initPoint = String.format(Resources.NAVEGATIONS_MAPS, ActivityContext.latiudeActive, ActivityContext.longitudeActive,
                            ActivityContext.myPath.get(ActivityContext.previousSequence+1).getLat(), ActivityContext.myPath.get(ActivityContext.previousSequence+1).getLng());
                    ActivityContext.playlerRouter.farFromTheBeginning();
                    ActivityContext.btn_start.setText("Iniciar");
                    ActivityContext.ultimo_comando = "";
                    timeSom(ActivityContext.timeProcessSound);
                    ActivityContext.initPoint = false;
                    ActivityContext.handlerMessage.post(new Runnable() {
                        @Override
                        public void run() {
                            ActivityContext.responseGenerics.OnResponseType(Resources.STARTING_POINT_NOT_FOUND, null);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    ActivityContext.handlerMessage.post(new Runnable() {
                        @Override
                        public void run() {
                            ActivityContext.responseGenerics.OnResponseType(Resources.STARTING_POINT_NOT_FOUND, null);
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
                        if (!ActivityContext.runningPlayer && ActivityContext.previousSequence < ActivityContext.markers.size()) {
                            ActivityContext.playlerRouter.direcaoPontoAzul();
                            timeSom(ActivityContext.timeProcessSound);

                        }
                        if (!ActivityContext.runningPlayer) {
                            switch (ActivityContext.ultimo_comando) {
                                case "I":
                                    ActivityContext.playlerRouter.direcaoPontoAzul();
                                    timeSom(ActivityContext.timeProcessSound);
                                    break;
                                case "F":
                                    ActivityContext.playlerRouter.goAhead();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;
                                case "D":
                                    ActivityContext.playlerRouter.turnRight();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;
                                case "E":
                                    ActivityContext.playlerRouter.turnLeft();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;

                                case "VELND":

                                    ActivityContext.playlerRouter.returnFromTheOtherSideRigth();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;


                                case "VELNE":

                                    ActivityContext.playlerRouter.returnFromTheOtherSideLeft();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;

                                case "SDPA":

                                    ActivityContext.playlerRouter.direcaoPontoAzul();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;

                                case "NEROLRNQSF":
                                    ActivityContext.playlerRouter.NEROLRNQSF();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;


                                case "VDSVE":

                                    ActivityContext.playlerRouter.VDSVE();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;


                                case "VESVD":

                                    ActivityContext.playlerRouter.VESVD();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;


                                case "NRPPS":

                                    ActivityContext.playlerRouter.NRPPS();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;


                                case "NRPSS":

                                    ActivityContext.playlerRouter.NRPSS();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;


                                case "NRPTS":

                                    ActivityContext.playlerRouter.NRPTS();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;

                                case "NRPQS":

                                    ActivityContext.playlerRouter.NRPQS();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;


                                case "FFF":

                                    ActivityContext.playlerRouter.FFF();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
                                    break;

                                default:
                                    ActivityContext.playlerRouter.thereIsNoCommand();
                                    timeSom(ActivityContext.timeProcessSound);
                                    ActivityContext.runningPlayer = false;
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
                int hours = ActivityContext.second / 3600;
                int minute = (ActivityContext.second % 3600) / 60;
                int seconds = ActivityContext.second % 60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minute, seconds);
                //ActivityContext.time.setText(time);
                if (ActivityContext.running) {
                    ActivityContext.second++;
                    String composta = String.format("%s%s%s%s", ActivityContext.operationModelRepository.getOperationID()
                            , ActivityContext.operationModelRepository.getRouterID(),
                            ActivityContext.operationModelRepository.getStoreID(),
                            ActivityContext.operationModelRepository.getParternID());
                    String latlng = String.format("%s,%s", ActivityContext.latiudeActive, ActivityContext.longitudeActive);
                    DeliveryStateModel deliveryStateModel = new DeliveryStateModel();
                    deliveryStateModel.setDeliveryid(String.valueOf(ActivityContext.operationModelRepository.getOperationID()));
                    deliveryStateModel.setDeviceid(ActivityContext.operationModelRepository.getDeviceID());
                    deliveryStateModel.setRouterid(ActivityContext.operationModelRepository.getRouterID());
                    deliveryStateModel.setStoreid(ActivityContext.operationModelRepository.getStoreID());
                    deliveryStateModel.setPartnerid(ActivityContext.operationModelRepository.getParternID());
                    deliveryStateModel.setUuidcomposta(composta);
                    deliveryStateModel.setLatlng(String.format(latlng));
                    deliveryStateModel.setNuvem(0);
                    deliveryStateModel.setBatery(ActivityContext.batteryTxt);
                    deliveryStateModel.setLap(ActivityContext.laps);
                    ActivityContext.deliveryStateReaderDbHelper.insert(deliveryStateModel);

                }
                handler.postDelayed(this, 1000);
            }
        });


    }

    private void initPlayer() {
        ActivityContext.Player = new Playler(ActivityContext.contextActivity);

    }

    private void initMapGoogle() {

        GoogleMapOptions options =  new GoogleMapOptions();
        options.compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(false);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        SupportMapFragment.newInstance(options);


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
                ActivityContext.operationModelRepository = new Gson().fromJson(json, OperationModelRepository.class);
                ActivityContext.rotaActiva = String.valueOf(ActivityContext.operationModelRepository.getRouterID());
                ActivityContext.instructions = new RouterInMap().getInstructions(ActivityContext.operationModelRepository.getRouterMap());
                for (RouterMap r : ActivityContext.instructions) {
                    String[] latLng = r.pontos.split("\\,");
                    Locations locations = new Locations(r.id, r.command, Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                    ActivityContext.myPath.add(locations);
                }
                ProcessWaypoints processWaypoints = new ProcessWaypoints(getWaypoints(ActivityContext.instructions));
                ActivityContext.distancePerKm = processWaypoints.kmDividedByPoints();
                ActivityContext.pointInRouter = processWaypoints.numberOfPointsOnTheRoute();
                ActivityContext.zonas = new RouterInMap().getAreasStill(ActivityContext.operationModelRepository.getZonasJson());


            }
        }catch (Exception e){
            Toast.makeText(SoundsCarActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void resetListsData() {
        if(ActivityContext.myPath!=null){
            ActivityContext.myPath.clear();
        }
        if(ActivityContext.instructions!=null){
            ActivityContext.instructions.clear();
        }
        if(ActivityContext.markers!=null){
            ActivityContext.markers.clear();
        }
        if(ActivityContext.listSequenciePoints!=null){
            ActivityContext.listSequenciePoints.clear();
        }

        if(ActivityContext.lidos!=null){
            ActivityContext.lidos.clear();
        }
        if(ActivityContext.zonas!=null){
            ActivityContext.zonas.clear();
        }
        if(ActivityContext.markersReads!=null){
            ActivityContext.markersReads.clear();
        }

        if(ActivityContext.ponts!=null){
            ActivityContext.ponts.clear();
        }

    }


    private void setButtonStateStart(boolean running) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    ActivityContext.btn_start.setText("Navegando...");
                    ActivityContext.btn_start.setEnabled(false);
                } else {
                    ActivityContext.btn_start.setText("Iniciar >>");
                    ActivityContext.btn_start.setEnabled(false);
                }
            }
        });
    }

    private void showTheRouteStatusOnTheDisplay() {
        try{
            double distanceMade = (ActivityContext.previousSequence != 0) ? ActivityContext.distancePerKm * (ActivityContext.pointInRouter) : 0;
            int lestPoint = ActivityContext.pointInRouter - 1;
            double distanceMadePorcents = 0;

            if(lestPoint!=0){
                distanceMadePorcents = ((ActivityContext.distancePerKm * ActivityContext.previousSequence) * 100) / (ActivityContext.distancePerKm * (lestPoint));
            }
            ActivityContext.statuskmsRouter.setText((distanceMade > 1) ? String.format("%.2f", distanceMade) + " Km" : String.format("%.2f", distanceMade) + " M");
            ActivityContext.statusPorcentageRouter.setText(String.format("%.2f", distanceMadePorcents) + " %");
            autoCloseFinishOperation(distanceMadePorcents);
        }catch (Exception e){
            Toast.makeText(SoundsCarActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    private void autoCloseFinishOperation(double distanceMadePorcents) {
        try {
            Toast.makeText(ActivityContext.contextActivity, String.valueOf(distanceMadePorcents), Toast.LENGTH_LONG).show();
            if (distanceMadePorcents == 100) {
                this.finishOperaion(ActivityContext.operationModelRepository);
                ActivityContext.initPoint=false;
            }
        }catch (Exception e){
            Toast.makeText(SoundsCarActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void finishOperaion(Object objetct) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OperationModelRepository item = (OperationModelRepository) objetct;
                ActivityContext.appOpeationsRepository.updateStateOperations(
                        String.valueOf(item.getOperationID()),
                        String.valueOf(item.getParternID()), 4);

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityContext.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        try {
                            resetStateOperation();
                            finish();
                            Intent intent = new Intent(ActivityContext.contextActivity, HomeActivity.class);
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
                ActivityContext.dialog = builder.create();
                ActivityContext.dialog.show();

            }
        });


    }

    private void resetStateOperation() {
        ActivityContext.lastOperationAndActiveRoute.setProps(String.valueOf(Resources.LAST_ID_AND_POSSITION_OPERATION_CURRENTR));
        ActivityContext.lastOperationAndActiveRoute.setContent("");
        ActivityContext.lastOperationAndActiveRoute.setProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
        ActivityContext.lastOperationAndActiveRoute.setContent("");
        ActivityContext.previousSequence = 0;
        ActivityContext.navegationsReaderDbHelper.update(ActivityContext.lastOperationAndActiveRoute);

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
        ActivityContext.mMap = googleMap;
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







        ActivityContext.mMap.setMyLocationEnabled(true);
        ActivityContext.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        ActivityContext.mMap.setBuildingsEnabled(true);

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
        ActivityContext.mMap.setMyLocationEnabled(true);
        for (int next = 0; next < ActivityContext.instructions.size(); next++) {
            String[] latLng = Functions.split("\\,",ActivityContext.instructions.get(next).pontos);// ActivityContext.instructions.get(next).pontos.split("\\,");
            ActivityContext.markers.add(new Markers(latLng[0], latLng[1], String.format("Ponto : %s", next + 1)));
        }
        drawMapInMarkers();
    }

    private void drawAreaOfacting() {
        List<LatLng> area = new ArrayList<>();
        try{
            for (Zonas zona : ActivityContext.zonas) {
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
                ActivityContext.mMap.addPolygon(polygonOptions);

            }
            Locations locations = ActivityContext.myPath.get(ActivityContext.previousSequence);
            LatLng marketInit = new LatLng(locations.getLat(), locations.getLng());
            if (!ActivityContext.initPoint) {
                ActivityContext.mMap.addMarker(new MarkerOptions()
                        .position(marketInit)
                        .title("Inicial Rota..."));
                ActivityContext.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marketInit, 11));
            }
        }catch (Exception e){

        }

    }


    private void knowWhereIAm(double latitude, double longitude) {
        //001
        int diff = 0;
        double distance = 0;
        int size = ActivityContext.myPath.size();
        String marker = initString();
        actionPhoto(false);



        try{
            if (ActivityContext.initPoint) {
                ActivityContext.PointsRead++;
                if (!ActivityContext.setOperationInit) {
                    ActivityContext.appOpeationsRepository.updateStateOperations(
                            String.valueOf(ActivityContext.operationModelRepository.getOperationID()),
                            String.valueOf(ActivityContext.operationModelRepository.getParternID()), Enum.STATUS_IS_OPEN_OPERATION);
                    actionPhoto(true);
                    ActivityContext.setOperationInit = true;
                }


                for (int next = (ActivityContext.previousSequence ); next < size; next++) {
                    marker = String.format("%s%s", ActivityContext.myPath.get(next).getLat(), ActivityContext.myPath.get(next).getLng());
                    ActivityContext.currentMarket = ActivityContext.myPath.get(next);

                    if (ActivityContext.lidos.indexOf(marker) == -1) {
                        diff = Math.abs(ActivityContext.previousSequence - next);
                        distance = (distanceBetweenTwoPoints(latitude, longitude,
                                ActivityContext.myPath.get(next).getLat(), ActivityContext.myPath.get(next).getLng()));


                        System.out.println("#8  Distanc[ " + distance + "] Direcao [" + ActivityContext.myPath.get(next).getDirecao() + "] Passo: " + next);
                        if (distance < ActivityContext.raio && (Math.abs(ActivityContext.previousSequence - next) == 1)) {
                            ActivityContext.ultimo_comando = ActivityContext.myPath.get(next).getDirecao();
                            ActivityContext.previousSequence = next;
                            drawMapInMarkers();
                            showTheRouteStatusOnTheDisplay();
                            performAudioGuidance();
                            ActivityContext.positionError = 0;
                            ActivityContext.lidos.add(marker);
                            RouterMovimentModel moviments = new RouterMovimentModel();
                            moviments.setAction("F");
                            moviments.setIdKeys(String.valueOf(ActivityContext.currentMarket.getId()));
                            moviments.setStartPoints(String.valueOf(ActivityContext.previousSequence));
                            moviments.setDeviceID(ActivityContext.operationModelRepository.getDeviceID());
                            moviments.setDeliveryId(String.valueOf(ActivityContext.operationModelRepository.getOperationID()));
                            //#2023
                            if(!ActivityContext.operationModelRepository.getDeviceID().isEmpty()){
                                ActivityContext.routerMovimentDbHelper.insert(moviments);
                                ActivityContext.PointsRead =0;
                                final long update = ActivityContext.navegationsReaderDbHelper.update(getRouterCurrent(next));

                                System.out.println("#233 "+update);
                                System.out.println("#233 "+getRouterCurrent(next));
                                System.out.println("#233 "+next);


                            }
                            System.out.println("#12 Feito ["+ActivityContext.previousSequence+"] ");
                            StringBuffer losg = new StringBuffer();
                            for (String v : ActivityContext.lidos) {
                                losg.append(v);
                            }
                        } else if (diff != 1) {
                            ActivityContext.positionError++;
                        }
                    }
                }
            }
            if (ActivityContext.positionError == ActivityContext.startBoxMensageError) {
                ActivityContext.responseGenerics.OnResponseType(Resources.ERROR_POINT_NOT_FOUND, null);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private NavegationModel getRouterCurrent(int next) {

        if(next < ActivityContext.myPath.size()) {
            String latLon = String.format("%s,%s", ActivityContext.myPath.get(next).getLat(), ActivityContext.myPath.get(next).getLng());
            ActivityContext.lastOperationAndActiveRoute.setProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
            ActivityContext.lastOperationAndActiveRoute.setContent(
                    String.format("%s|%s|%s|%s|%s", ActivityContext.operationModelRepository.getOperationID(), ActivityContext.rotaActiva,
                            latLon, next, ActivityContext.laps)
            );
        }
        return ActivityContext.lastOperationAndActiveRoute;
    }

    private String initString() {
        return "";
    }

    public void drawMapInMarkers() {
        try{
            System.out.println("#5 processando..(Pintando map");
            ActivityContext.mMap.clear();
            drawAreaOfacting();
            int size = ActivityContext.markers.size();
            LatLng p1 = null, p2 = null;
            List<LatLng> lts = new ArrayList<>();
            boolean pontoTravado = false, nextPonits = false;
            for (int next = 0; next < size; next++) {
                p1 = new LatLng(
                        Double.parseDouble(ActivityContext.markers.get(next).getLat()),
                        Double.parseDouble(ActivityContext.markers.get(next).getLng()));

                if (next < ActivityContext.previousSequence && ActivityContext.previousSequence != 0) {
             /*   MarkerOptions pt = new MarkerOptions().position(p1)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                pt.title("Ponto : " + (next));
                gui.mMap.addMarker(pt);*/
                } else {
                    if ((next == ActivityContext.previousSequence || ActivityContext.previousSequence == 0) && !pontoTravado) {
                        MarkerOptions pt = new MarkerOptions().position(p1).
                                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                        pt.title("Inicio....").snippet("Blah");
                        ActivityContext.mMap.addMarker(pt);
                        pontoTravado = true;
                    } else {
                        if (next > ActivityContext.previousSequence) {
                            if (!nextPonits) {
                                MarkerOptions pt = new MarkerOptions().position(p1).
                                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                pt.title("Ponto : " + (next + 1));
                                ActivityContext.mMap.addMarker(pt);
                                nextPonits = true;
                            }

                        }
                    }
                }
                lts.add(p1);

                ActivityContext.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lts.get(lts.size() - 1), 14.0f));

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
        ActivityContext.mMap.clear();
        int size = ActivityContext.markers.size();
        LatLng p1 = null;
        List<LatLng> lts = new ArrayList<>();
        for (int next = 0; next < size; next++) {
            p1 = new LatLng(
                    Double.parseDouble(ActivityContext.markers.get(next).getLat()),
                    Double.parseDouble(ActivityContext.markers.get(next).getLng()));
            lts.add(p1);
        }
        PolylineOptions gg = new PolylineOptions();
        for (LatLng df : lts) {
            gg.add(df);
        }
        Polyline line = ActivityContext.mMap.addPolyline(gg);
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
                ActivityContext.uncheckedlayot.setVisibility(View.VISIBLE);
                return true;

            case R.id.finishoperation:
                stopRouterGps();
                return true;

            case R.id.menucloseoperation:
                this.finishOperaion(ActivityContext.operationModelRepository);
                return true;

            case R.id.menusyncronized:

                return true;




            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private static class ActivityContext {
        public ActivityResultLauncher<Intent> mapsNavegation;
        public   final int  GOOGLE_NAV = 5;

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
        public double raio = 35;//metros
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
        public int laps;
        public PictureInPictureParams.Builder pip_Builder;
        public ActivityResultLauncher<Object> mGetContent;
        public List<MarketRouter> marketRouters = new ArrayList<>();
        ActivityResultLauncher<Uri> takeAPhoto;
        ActivityResultLauncher<Intent> startCamera;
        String currentPhotoPath;
    }


}
