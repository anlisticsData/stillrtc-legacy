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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
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
import java.util.UUID;

import br.com.stilldistribuidora.Libs.FileManager;
import br.com.stilldistribuidora.Libs.Libs;
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
import br.com.stilldistribuidora.partners.Commom.MarketIgn;
import br.com.stilldistribuidora.partners.Commom.MarketRouter;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Contracts.UploadFile;
import br.com.stilldistribuidora.partners.Models.Audio;
import br.com.stilldistribuidora.partners.Models.CabinAudioUploadFile;
import br.com.stilldistribuidora.partners.Models.RouterMap;
import br.com.stilldistribuidora.partners.Models.Zonas;
import br.com.stilldistribuidora.partners.Repository.Audio.AudioRepository;
import br.com.stilldistribuidora.partners.Repository.Audio.CabinAudioOperationRepository;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CarRouteGuidanceActivity extends ActivityBaseApp implements OnMapReadyCallback {
    private static final Boot Boot = new Boot();
    private class NavegationState{
        private  int sizeInformation=4;
        private String delimiterSeparated="\\|";
        public  String operation="";
        public String routerId="";
        public int lastPoint=0;
        public  String latLng ="";

        public NavegationState(){
            List<NavegationModel> lastNavegations = Boot.navegationsReaderDbHelper.getByProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
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
            Boot.batteryTxt = String.valueOf(level);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //*2
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Boot.contextActivity = this;
        Boot.cabinRepository =  new CabinAudioOperationRepository(Boot.contextActivity);
        setContentView(R.layout.activity_operation_router_soundcar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ConfigDataAccess configModel = new ConfigDataAccess(Boot.contextActivity);
        Boot.userDevice = (Config) configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE));
        Boot.toolbar= (Toolbar) findViewById(R.id.toolbar);
        Boot.actionBar = getActionBar();
        Boot.uncheckedlayot = (ConstraintLayout)findViewById(R.id.uncheckedlayot);
        Boot.unlock =(LinearLayout)findViewById(R.id.unlock);
        Boot.main = (LinearLayout)findViewById(R.id.main);
        Boot.loadding = (LinearLayout)findViewById(R.id.loadding);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Boot.phoneInformationPhoneId = new PhoneInformations(Boot.contextActivity).getPhoneId();
        Boot.palyStop =  (ImageView)findViewById(R.id.play_stop);
        Boot.cronometer=  (TextView)findViewById(R.id.cronometer);
        
        

        InitPermisionGeral();
        keepScreenOn();
        retrieveReceivedData();
        initMapGoogle();
        initPlayer();
        configureCompatibiliteApp();
        setConfigurationCronometro();
        initBoot();
        initGps();
        initCamera();
        initEvents();
        setComandsPainel(View.GONE, View.GONE, View.GONE);
        initModelDataBase();
        initModelDataBaseConfiruations();
        publicReceivedInterfaceEvents();
        actionPhoto(false);
        Boot.navegationsReaderDbHelper.initInfo();
        setToolbar();
        //isOperationToOpen();
        isResetOperation();

        Boot.fileManager = new FileManager(this);
        try{
            Toast.makeText(Boot.contextActivity,"1",Toast.LENGTH_LONG).show();
            Boot.audioCabin = new File(cabinSonds());
            Boot.fileManager.createFileDirectory(Boot.audioCabin );

        }catch (Exception e){
            Toast.makeText(Boot.contextActivity,e.getMessage(),Toast.LENGTH_LONG).show();
        }


        try{
            Toast.makeText(Boot.contextActivity,"2",Toast.LENGTH_LONG).show();
            Boot.audio = new File(amostraSonds());
            Boot.fileManager.createFileDirectory(Boot.audio );

        }catch (Exception e){
            Toast.makeText(Boot.contextActivity,e.getMessage(),Toast.LENGTH_LONG).show();
        }




        Boot.cabinAudioUploadFile = Boot.cabinRepository.audioBy(Boot.operationModelRepository.getOperationID().toString(),
                Boot.operationModelRepository.getParternID().toString());

        Toast.makeText(Boot.contextActivity,"3",Toast.LENGTH_LONG).show();

        recorddingAudio();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                       // recorddingAudio();
                        Thread.sleep(15);
                    }catch (Exception E){}
                }
            }
        }).start();




/*

        MarketRouter marketRouter;
        Boot.marketRouters = new ArrayList<>();
        int next=0;
        for(Locations market : Boot.myPath) {
            if(market.getDirecao().equals("%")){
                Boot.marketRouters.get(next-1).
                        ignoredPoints.add(new MarketIgn(market.getLat(),market.getLng(),
                                market.getDirecao(),market.getId()));
            }else{
                marketRouter =  new MarketRouter();
                marketRouter.start =new MarketIgn(market.getLat(),market.getLng(),
                        market.getDirecao(),market.getId());
                if(next !=0){
                    Boot.marketRouters.get(next-1).fim =   marketRouter.start;
                }
                Boot.marketRouters.add(marketRouter);
                next++;
            }
        }

*/




        Boot.palyStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if( Boot.mediaPlayer ==null){
                        Boot.mediaPlayer = new MediaPlayer();
                        Boot.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        Boot.mediaPlayer.setDataSource(cabinSonds()+"/"+Boot.cabinAudioUploadFile.url);
                        Boot.mediaPlayer.prepare();
                        Boot.mediaPlayer.setLooping(true);
                    }
                    if(!Boot.isRunning){
                        Boot.isRunning=true;
                        Boot.runningDisplay=true;
                        Boot.palyStop.setImageResource(R.drawable.baseline_pause_circle_128);
                        Boot.mediaPlayer.start();
                        recorddingAudio();
                    }else{
                        Boot.mediaPlayer.stop();
                        Boot.palyStop.setImageResource(R.drawable.baseline_play_circle_filled_128_play);
                        Boot.mediaPlayer=null;
                        Boot.isRunning=false;
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });




        //levar para camada de services



    }

    private void recorddingAudio() {

        Toast.makeText(Boot.contextActivity,"4",Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Audio> audios;
                while (Boot.runningDisplay){


                    try {
                        audios = Boot.audioRepository.notUploadFile(String.valueOf(Boot.operationModelRepository.getOperationID()),String.valueOf( Boot.operationModelRepository.getParternID()));
                        for(Audio amostra : audios){
                            File AudioSaveInDevice = new File(amostra.audio);
                            if (AudioSaveInDevice.exists()) {
                                String latLng = String.format("%s,%s",Boot.latiudeActive,Boot.longitudeActive);
                                File audioFile =AudioSaveInDevice;
                                RequestBody reqFile = RequestBody.create(MediaType.parse("audio/*"), audioFile);
                                MultipartBody.Part audioPart = MultipartBody.Part.createFormData("audio", audioFile.getName(), reqFile);
                                UploadFile uploadFile = new UploadFile() {
                                    @Override
                                    public void response(boolean isUpload, int codeTableInDeviceId,String filePath) {
                                        if(isUpload){
                                            Boot.audioRepository.removeSample(codeTableInDeviceId,filePath);
                                        }
                                    }
                                };
                                Boot.servicesHttp.uploadReceiptMp3(uploadFile,amostra,Boot.userLoginCast,Boot.operationModelRepository,
                                        audioPart, Boot.conf.getToken() ,latLng);
                            }else{
                                Boot.audioRepository.removeSample(amostra.id,amostra.audio);
                            }
                        }
                        Thread.sleep(Libs.minuteToMills(3));
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }).start();

    }

    private void cronometer() {
        //*3

        if(!Boot.runningDisplay){
            Boot.initialTime = System.currentTimeMillis();
            Boot.runningDisplay = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (Boot.runningDisplay){
                       try{
                           long seconds = (System.currentTimeMillis() - Boot.initialTime) / Boot.MILLIS_IN_SEC;
                           String time = String.format("%02d:%02d:%02d",seconds / Boot.SECS_IN_HOURS, seconds / Boot.SECS_IN_MIN, seconds % Boot.SECS_IN_MIN);
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Boot.cronometer.setText(time);
                               }
                           });
                           Thread.sleep(Boot.MILLIS_IN_SEC);
                           getDeviceApi();
                       }catch (Exception e){e.printStackTrace();}

                    }
                }
            }).start();

        }else{
            Boot.runningDisplay = false;
        }
    }





    private void gravar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formataData =new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                Date data;
                while (Boot.isRunning){
                    try{
                        data = new Date();
                        String uuid_amostra =String.format("%s/amostra_%s.mp3",amostraSonds(),UUID.randomUUID().toString());
                        File audiof =  new File(uuid_amostra);

                        Boot.recorder = new MediaRecorder();
                        Boot.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        Boot.recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        Boot.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   Toast.makeText(Boot.contextActivity, "Gravando audio", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Boot.recorder.setOutputFile(audiof);
                            Boot.recorder.prepare();
                            Boot.recorder.start();   // Recording is now started
                            Thread.sleep(Libs.secondsToMills(10));
                            Boot.recorder.stop();
                            //Boot.recorder.reset();   // You can reuse the object by going back to setAudioSource() step
                            Boot.recorder.release(); // Now the object cannot be reused
                            Boot.recorder=null;
                            Audio amostra = new Audio();
                            amostra.audio = uuid_amostra;
                            amostra.lat_lng = String.format("%s,%s",Boot.latiudeActive,Boot.longitudeActive);
                            amostra.time_device =  formataData.format(data);
                            amostra.code_router = Boot.rotaActiva;
                            amostra.code=String.valueOf(Boot.operationModelRepository.getOperationID());
                            amostra.code_partner = String.valueOf(Boot.operationModelRepository.getParternID());
                            Boot.audioRepository.createAudio(amostra);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Boot.contextActivity, "Gravação Finalizada.", Toast.LENGTH_SHORT).show();
                                }
                            });


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(Boot.contextActivity, "Salva", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Thread.sleep(Libs.minuteToMills(1));
                        }
                    }catch (Exception e ){

                        System.out.println("#56 => "+e.getMessage());
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Boot.contextActivity, "#Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }


                }
            }
        }).start();

    }

    private String amostraSonds() {
        return String.format("%s/amostras",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) );
    }

    private String cabinSonds() {
        return String.format("%s/cabin",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) );
    }


    public void loadding(boolean isLoadding){
        if(isLoadding){
            Boot.loadding.setVisibility(View.VISIBLE);
        }else{
            Boot.loadding.setVisibility(View.GONE);
        }
    }




    private  void isResetOperation(){
        List<NavegationModel> lastNavegations = Boot.navegationsReaderDbHelper.getByProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
        String[] routerIdGeoSize = null;
        if (lastNavegations.size() > 0) {
            String content = lastNavegations.get(0).getContent();
            routerIdGeoSize = Functions.split("\\|",content);
        }
        String deliveryCode=  String.valueOf(Boot.operationModelRepository.getOperationID());


        System.out.println("#344 "+deliveryCode.trim()+" => "+routerIdGeoSize[0].trim());
        if(deliveryCode.trim()!=routerIdGeoSize[0].trim()){
            resetStateOperation();
            return;
        }
        if (routerIdGeoSize != null && routerIdGeoSize.length == 4  &&
                deliveryCode.trim().equals(routerIdGeoSize[0].trim()) ) {

            String[] finalRouterIdGeoSize = routerIdGeoSize;
            Boot.previousSequence = Integer.parseInt(finalRouterIdGeoSize[3]);
        }

        double distance = 0;
        distance = (int) distanceBetweenTwoPoints(Boot.latiudeActive, Boot.longitudeActive,
                         Boot.myPath.get(Boot.previousSequence).getLat(),Boot.myPath.get(Boot.previousSequence).getLng());


        Integer operationCurrent = Boot.operationModelRepository.getOperationID();
        Integer operationSelected =(routerIdGeoSize[0].trim().isEmpty()) ? 0 :  Integer.valueOf(routerIdGeoSize[0]);
        boolean isEquals = operationCurrent.equals(operationSelected);
        if (Boot.previousSequence > 0 && isEquals && distance < Boot.raio) {
            Boot.initPoint = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
            String[] finalRouterIdGeoSize = routerIdGeoSize;
            double finalDistance = distance;
            builder.setPositiveButton("Continuar >> ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Boot.running = true;
                    Boot.ultimo_comando = "I";
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

        List<OperationModelRepository> operationAr = Boot.appOpeationsRepository.getAll(String.valueOf(Enum.STATUS_IS_OPEN_OPERATION));
        boolean dif=true;
        OperationModelRepository  operationOpen =  new OperationModelRepository();

        for(OperationModelRepository m : operationAr){
            operationOpen = m;
            Integer integer = Integer.valueOf(m.getOperationID());
            Integer integer1 = Integer.valueOf(Boot.operationModelRepository.getOperationID());
            if(integer.equals(integer1)){
                //dif=false;
                //break;
            }
        }
        if (operationAr.size() != 0  &&  dif ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
            builder.setTitle("Ação Cancelada.");
            builder.setMessage("Operação ("+operationOpen.getOperationID()+" "+operationOpen.getCreatedAt()+") em Aberto. \npara Continuar em Outra Operação.");
            builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Boot.running=false;
                    Boot.alerta.dismiss();
                    Intent intent = new Intent(Boot.contextActivity, MyOperations.class);
                    intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, Enum.STATUS_IS_OPEN_OPERATION);
                    startActivity(intent);
                    finish();
                }

            });
            Boot.alerta = builder.create();
            Boot.alerta.show();
            return;
        }
    }

    private void setToolbar() {
        setSupportActionBar(Boot.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }




    private void getDeviceApi() {
        Boot.operationModelRepository.setDeviceID(Boot.userDevice.getDataJson());
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
        Boot.currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void startActivityOnResult() {
        Boot.receiveAnswerFromChildren = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Constants.FINISH_OPERATION_OK_ACTIONS) {


                        }

                    }
                });

        Boot.startCamera = registerForActivityResult(
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
                                        String realPathFromURI = getRealPathFromURI(Boot.cam_uri);
                                        System.out.println("#4 " + Boot.cam_uri.getPath());
                                        photoFile = createImageFile();
                                        String[] geo = (((String) obj).split("\\|"))[0].split("\\,");
                                        PhotoEntity photoEntity = new PhotoEntity();
                                        photoEntity.setCreated_at(PartenerHelpes.getDate());
                                        photoEntity.setUri(realPathFromURI);
                                        photoEntity.setSync("0");
                                        photoEntity.setDelivery_id(String.valueOf(Boot.operationModelRepository.getOperationID()));
                                        photoEntity.setLatitude(geo[0]);
                                        photoEntity.setLongitude(geo[1]);
                                        photoEntity.setPic_uid("0");
                                        photoEntity.setType("1");
                                        photoEntity.setPartners_id(String.valueOf(Boot.operationModelRepository.getParternID()));
                                        photoEntity.setDeviceId(Boot.userDevice.getDataJson());
                                        if (Boot.photosPartnersModel.save(photoEntity) > 0) {
                                            Toast.makeText(Boot.contextActivity, "Foto Salva Com Sucesso..!", Toast.LENGTH_SHORT).show();
                                        }
                                        try {
                                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(Boot.cam_uri.toString()));

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


    private void initBoot() {
        Boot.handlerMessage = new Handler();
        Boot.statusPorcentageRouter = findViewById(R.id.statusPorcentageRouter);
        Boot.statuskmsRouter = findViewById(R.id.statuskmsRouter);
        Boot.btnmini = findViewById(R.id.btnmini);
        Boot.btn_start = findViewById(R.id.btn_start);
        Boot.btn_stop = findViewById(R.id.btn_stop);
        Boot.btnPhoto = findViewById(R.id.btnPhoto);

        Boot.photosPartnersModel = new PhotosPartnersModel(Boot.contextActivity);
        Boot.btn_utimo_comando = findViewById(R.id.btn_utimo_comando);
        Boot.playlerRouter = new Playler(Boot.contextActivity);
        Boot.appOpeationsRepository = new AppOpeationsRepository(Boot.contextActivity);
        Boot.conf = new ServerConfiguration(Boot.contextActivity);
        Boot.servicesHttp = new ServicesHttp(Boot.contextActivity, Boot.conf, CallBackOnResponseGlobalApi());
        Boot.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(this));
        String userLogged = Boot.conf.getUserLogged();
        Boot.userLoginCast = new Gson().fromJson(userLogged, UserLoginCast.class);

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
        Boot.cam_uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Boot.cam_uri);
        //startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE); // OLD WAY
        Boot.startCamera.launch(cameraIntent);                // VERY NEW WAY
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
        Boot.navegationsReaderDbHelper = new NavegationsReaderDbHelper(Boot.contextActivity);
        Boot.routerMovimentDbHelper = new RouterMovimentBusiness(new RouterMovimentHelper(Boot.contextActivity));
        Boot.lastOperationAndActiveRoute = new NavegationModel();
        Boot.deliveryStateReaderDbHelper = new DeliveryStateReaderDbHelper(Boot.contextActivity);
        Boot.audioRepository  = new AudioRepository(Boot.contextActivity);
    }

    private void initModelDataBaseConfiruations() {
        Boot.lastOperationAndActiveRoute.setProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));


    }

    private void setComandsPainel(int startRouter, int photo, int ir) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  Boot.start.setVisibility(startRouter);
                // Boot.photo.setVisibility(photo);
                //  Boot.btnIr.setVisibility(ir);

            }
        });

    }

    private void initGps() {
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        boolean providerEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!providerEnabled){
            AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
            OnResponse click = new OnResponse() {
                @Override
                public void OnResponseType(String type, Object object) {
                    Boot.dialog.dismiss();
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
            Boot.dialog = builder.create();
            Boot.dialog.show();
            return ;
    }
        Boot.fusedLocationClient = LocationServices.getFusedLocationProviderClient(Boot.contextActivity);
        Boot.HandlerGps = new Handler();
        Boot.HandlerGps.post(new Runnable() {
            @Override
            public void run() {
                InitPermisionGeral();

                if (ActivityCompat.checkSelfPermission(Boot.contextActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Boot.contextActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Boot.fusedLocationClient.getLastLocation().addOnSuccessListener(CarRouteGuidanceActivity.this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    Boot.latiudeActive = location.getLatitude();
                                    Boot.longitudeActive = location.getLongitude();
                                    Boot.distance = 0;
                                    Boot.distanceStartPoint = getDistanceInitMarket();
                                }
                            }
                        });
                Boot.HandlerGps.postDelayed(this, 8*1000);
            }
        });
    }

    private int getDistanceInitMarket() {
        return ((int) distanceBetweenTwoPoints(Boot.latiudeActive,
                Boot.longitudeActive,
                Boot.myPath.get(Boot.previousSequence).getLat(),
                Boot.myPath.get(Boot.previousSequence).getLng()));


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

        Boot.btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boot.running =false;
                Boot.isRunning=false;
                cronometer();
                if(Boot.mediaPlayer!=null){
                    Boot.mediaPlayer.stop();
                    Boot.mediaPlayer=null;
                }
                finish();
                Intent intent  = new Intent(Boot.contextActivity, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



        Boot.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Boot.running){
                    Boot.running=true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Boot.btn_start.setText(R.string.executando);
                        }
                    });

                    cronometer();
                    if (Boot.audio.isDirectory()) {
                        gravar();
                    }
                }
                else{
                    Boot.running=false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Boot.btn_start.setText(R.string.iniciar);
                        }
                    });

                    cronometer();
                }



            }
        });


        Boot.btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCamera();
            }
        });

        Boot.unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boot.uncheckedlayot.setVisibility(View.GONE);
            }
        });




    }




    private void stopRouterGpsOperation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        if (type.equals("STOP")) {
                            Boot.running = false;
                            Boot.ultimo_comando = "";
                            Boot.initPoint = false;
                            Boot.init_comando = false;
                            setButtonStateStart(false);
                            Intent intent  = new Intent(Boot.contextActivity, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        Boot.dialog.dismiss();
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
                Boot.dialog = builder.create();
                Boot.dialog.show();
            }
        });


    }


    private void stopRouterGps() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        if (type.equals("STOP")) {
                            Boot.running = false;
                            Boot.ultimo_comando = "";
                            Boot.initPoint = false;
                            Boot.init_comando = false;
                            setButtonStateStart(false);
                            finish();
                        }
                        Boot.dialog.dismiss();
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
                Boot.dialog = builder.create();
                Boot.dialog.show();
            }
        });


    }


    private void publicReceivedInterfaceEvents() {
        Boot.responseGenerics = new OnResponse() {
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
        Boot.playlerRouter.gpsNotFound();
        timeSom(Boot.timeProcessSound);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RouterMovimentModel moviments = new RouterMovimentModel();
                        moviments.setAction("C");
                        moviments.setIdKeys(String.valueOf(Boot.currentMarket.getId()));
                        moviments.setStartPoints(String.valueOf(Boot.previousSequence));
                        moviments.setDeviceID(Boot.operationModelRepository.getDeviceID());
                        moviments.setDeliveryId(String.valueOf(Boot.operationModelRepository.getOperationID()));
                        Boot.routerMovimentDbHelper.insert(moviments);
                        Boot.previousSequence++;
                        Boot.positionError = 0;
                        Boot.navegationsReaderDbHelper.update(getRouterCurrent(Boot.previousSequence-1));
                     //   drawMapInMarkers();
                        showTheRouteStatusOnTheDisplay();
                        System.out.println("#12 cancelado ["+Boot.previousSequence+"] ");

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
                AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
                builder.setPositiveButton(R.string.navegar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(Boot.actuvePoint_initPoint));
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
            System.out.println("#23 " + Boot.distanceStartPoint);
            if (Boot.running == false) {

                Boot.running = true;
                Boot.ultimo_comando = "";
                Boot.initPoint = true;
                Boot.distance = 0;
                if (!Boot.init_comando) {
                    List<OperationModelRepository> operationAr = Boot.appOpeationsRepository.getAll(String.valueOf(Enum.STATUS_IS_OPEN_OPERATION));
                    List<NavegationModel> lastNavegations = Boot.navegationsReaderDbHelper.getByProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
                    boolean dif = true;
                    for (OperationModelRepository m : operationAr) {
                        Integer integer = Integer.valueOf(m.getOperationID());
                        Integer integer1 = Integer.valueOf(Boot.operationModelRepository.getOperationID());
                        if (integer.equals(integer1)) {
                            dif = false;
                            break;
                        }
                    }


                    if (operationAr.size() != 0 && dif) {
                        //Cria o gerador do AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
                        //define o titulo
                        builder.setTitle("Ação Cancelada.");
                        //define a mensagem
                        builder.setMessage("Operação em Aberto. para Continuar em Outra Operação.");
                        //define um botão como positivo
                        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Boot.running = false;
                                Boot.alerta.dismiss();
                                Intent intent = new Intent(Boot.contextActivity, MyOperations.class);
                                intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, Enum.STATUS_IS_OPEN_OPERATION);
                                startActivity(intent);
                                finish();
                            }

                        });
                        //cria o AlertDialog
                        Boot.alerta = builder.create();
                        //Exibe
                        Boot.alerta.show();
                        return;
                    }
                    double distance = 0;
                    distance = (int) distanceBetweenTwoPoints(Boot.latiudeActive, Boot.longitudeActive,
                                        Boot.myPath.get(Boot.previousSequence).getLat(),
                                        Boot.myPath.get(Boot.previousSequence).getLng());

                    String[] routerIdGeoSize = null;
                    if (lastNavegations.size() > 0) {
                        String content = lastNavegations.get(0).getContent();
                        routerIdGeoSize = Functions.split("\\|", content);
                    }
                    if (routerIdGeoSize != null && routerIdGeoSize.length == 4 &&
                            routerIdGeoSize[1].equals(Boot.rotaActiva) &&
                            Boot.operationModelRepository.getOperationID().equals(routerIdGeoSize[0])) {
                        String[] finalRouterIdGeoSize = routerIdGeoSize;
                        Boot.previousSequence = Integer.parseInt(finalRouterIdGeoSize[3]);
                    }


                    if (Boot.previousSequence > 0 && Boot.operationModelRepository.
                            getOperationID().equals(routerIdGeoSize[0])) {

                        Boot.initPoint = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
                        String[] finalRouterIdGeoSize = routerIdGeoSize;
                        double finalDistance = distance;
                        builder.setPositiveButton("Continuar >> ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Boot.running = true;
                                Boot.ultimo_comando = "I";
                                performAudioGuidance();
                                startRouterGo(finalDistance);
                                setButtonStateStart(Boot.running);
                                initStartServices();

                            }
                        });
                        builder.setTitle("Continuar Operação..!");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        setButtonStateStart(false);
                        Boot.running = false;
                        Boot.playlerRouter.direcaoPontoAzul();
                        timeSom(Boot.timeProcessSound);
                        startRouterGo(Boot.distanceStartPoint);

                    }
                }
            }
        }catch (Exception e){
            System.out.println("#100 =>" +e.getMessage());
        }
    }


    private void startRouterGo(double distance) {
        try {
            if (distance > Boot.raio && Boot.previousSequence == 0 && Boot.initPoint) {
                Boot.init_comando = false;
                invalideDistanceCurrent();
                Boot.running=false;
                setButtonStateStart(Boot.running);
            } else {
                Boot.running=true;
                Boot.init_comando = true;
                Boot.ultimo_comando = "I";
                performAudioGuidance();
                setButtonStateStart(Boot.running);
                getDeviceApi();
            }
        }catch (Exception e){
            System.out.println("#100 =>" +e.getMessage());
        }
    }

    private void initStartServices() {
        startService(new Intent(Boot.contextActivity, ServicesApiStill.class));
    }
    private void runningRoute() {

        //9

        if (Boot.operationModelRepository.getDeviceID() == null) {
            //Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
            //define o titulo
            builder.setTitle(R.string.a_o_cancelada);
            //define a mensagem
            builder.setMessage(R.string.n_o_h_dispositivo_livres_no_momento_para_continuar_em_outra_opera_o);
            //define um botão como positivo
            builder.setPositiveButton(R.string.fechar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Boot.alerta.dismiss();
                    Intent intent = new Intent(Boot.contextActivity, MyOperations.class);
                    intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, "2");
                    startActivity(intent);
                    finish();
                }

            });
            //cria o AlertDialog
            Boot.alerta = builder.create();
            //Exibe
            Boot.alerta.show();
            return;
        }
        Boot.navegationsReaderDbHelper.update(getRouterCurrent(Boot.previousSequence));
        Boot.running = true;

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Boot.init_comando) {
                        actionPhoto(true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Boot.contextActivity,"5",Toast.LENGTH_LONG).show();
                            }
                        });
                        handler.postDelayed(this, 10 * 1000);
                    }
                }catch (Exception e){
                  e.printStackTrace();
                }

            }
        });



    }

    private void actionPhoto(boolean visible) {
        if (visible) {
            Boot.btnPhoto.setVisibility(View.VISIBLE);
        } else {
            Boot.btnPhoto.setVisibility(View.GONE);
        }
    }

    private void invalideDistanceCurrent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Boot.actuvePoint_initPoint = String.format(Resources.NAVEGATIONS_MAPS, Boot.latiudeActive, Boot.longitudeActive,
                            Boot.myPath.get(Boot.previousSequence+1).getLat(), Boot.myPath.get(Boot.previousSequence+1).getLng());
                    Boot.playlerRouter.farFromTheBeginning();
                    Boot.btn_start.setText("Iniciar");
                    Boot.ultimo_comando = "";
                    timeSom(Boot.timeProcessSound);
                    Boot.initPoint = false;
                    Boot.handlerMessage.post(new Runnable() {
                        @Override
                        public void run() {
                            Boot.responseGenerics.OnResponseType(Resources.STARTING_POINT_NOT_FOUND, null);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Boot.handlerMessage.post(new Runnable() {
                        @Override
                        public void run() {
                            Boot.responseGenerics.OnResponseType(Resources.STARTING_POINT_NOT_FOUND, null);
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
                        if (!Boot.runningPlayer && Boot.previousSequence < Boot.markers.size()) {
                            Boot.playlerRouter.direcaoPontoAzul();
                            timeSom(Boot.timeProcessSound);

                        }
                        if (!Boot.runningPlayer) {
                            switch (Boot.ultimo_comando) {
                                case "I":
                                    Boot.playlerRouter.direcaoPontoAzul();
                                    timeSom(Boot.timeProcessSound);
                                    break;
                                case "F":
                                    Boot.playlerRouter.goAhead();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;
                                case "D":
                                    Boot.playlerRouter.turnRight();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;
                                case "E":
                                    Boot.playlerRouter.turnLeft();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;

                                case "VELND":

                                    Boot.playlerRouter.returnFromTheOtherSideRigth();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;


                                case "VELNE":

                                    Boot.playlerRouter.returnFromTheOtherSideLeft();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;

                                case "SDPA":

                                    Boot.playlerRouter.direcaoPontoAzul();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;

                                case "NEROLRNQSF":
                                    Boot.playlerRouter.NEROLRNQSF();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;


                                case "VDSVE":

                                    Boot.playlerRouter.VDSVE();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;


                                case "VESVD":

                                    Boot.playlerRouter.VESVD();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;


                                case "NRPPS":

                                    Boot.playlerRouter.NRPPS();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;


                                case "NRPSS":

                                    Boot.playlerRouter.NRPSS();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;


                                case "NRPTS":

                                    Boot.playlerRouter.NRPTS();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;

                                case "NRPQS":

                                    Boot.playlerRouter.NRPQS();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;


                                case "FFF":

                                    Boot.playlerRouter.FFF();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
                                    break;

                                default:
                                    Boot.playlerRouter.thereIsNoCommand();
                                    timeSom(Boot.timeProcessSound);
                                    Boot.runningPlayer = false;
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
                int hours = Boot.second / 3600;
                int minute = (Boot.second % 3600) / 60;
                int seconds = Boot.second % 60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minute, seconds);
                //Boot.time.setText(time);
                if (Boot.running) {
                    Boot.second++;
                    String composta = String.format("%s%s%s%s", Boot.operationModelRepository.getOperationID()
                            , Boot.operationModelRepository.getRouterID(),
                            Boot.operationModelRepository.getStoreID(),
                            Boot.operationModelRepository.getParternID());
                    String latlng = String.format("%s,%s", Boot.latiudeActive, Boot.longitudeActive);
                    DeliveryStateModel deliveryStateModel = new DeliveryStateModel();
                    deliveryStateModel.setDeliveryid(String.valueOf(Boot.operationModelRepository.getOperationID()));
                    deliveryStateModel.setDeviceid(Boot.operationModelRepository.getDeviceID());
                    deliveryStateModel.setRouterid(Boot.operationModelRepository.getRouterID());
                    deliveryStateModel.setStoreid(Boot.operationModelRepository.getStoreID());
                    deliveryStateModel.setPartnerid(Boot.operationModelRepository.getParternID());
                    deliveryStateModel.setUuidcomposta(composta);
                    deliveryStateModel.setLatlng(String.format(latlng));
                    deliveryStateModel.setNuvem(0);
                    deliveryStateModel.setBatery(Boot.batteryTxt);
                    Boot.deliveryStateReaderDbHelper.insert(deliveryStateModel);

                }
                handler.postDelayed(this, 1000);
            }
        });


    }

    private void initPlayer() {
        Boot.Player = new Playler(Boot.contextActivity);

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
              Boot.operationModelRepository = new Gson().fromJson(json, OperationModelRepository.class);
              Boot.rotaActiva = String.valueOf(Boot.operationModelRepository.getRouterID());
              Boot.instructions = new RouterInMap().getInstructions(Boot.operationModelRepository.getRouterMap());
              for (RouterMap r : Boot.instructions) {
                  String[] latLng = r.pontos.split("\\,");
                  Locations locations = new Locations(r.id, r.command, Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                  Boot.myPath.add(locations);
              }
              ProcessWaypoints processWaypoints = new ProcessWaypoints(getWaypoints(Boot.instructions));
              Boot.distancePerKm = processWaypoints.kmDividedByPoints();
              Boot.pointInRouter = processWaypoints.numberOfPointsOnTheRoute();
              Boot.zonas = new RouterInMap().getAreasStill(Boot.operationModelRepository.getZonasJson());
              System.out.println("");


          }
      }catch (Exception e){
          Toast.makeText(CarRouteGuidanceActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
      }
    }

    private void resetListsData() {
        if(Boot.myPath!=null){
            Boot.myPath.clear();
        }
        if(Boot.instructions!=null){
            Boot.instructions.clear();
        }
        if(Boot.markers!=null){
            Boot.markers.clear();
        }
        if(Boot.listSequenciePoints!=null){
            Boot.listSequenciePoints.clear();
        }

        if(Boot.lidos!=null){
            Boot.lidos.clear();
        }
        if(Boot.zonas!=null){
            Boot.zonas.clear();
        }
        if(Boot.markersReads!=null){
            Boot.markersReads.clear();
        }

        if(Boot.ponts!=null){
            Boot.ponts.clear();
        }

    }


    private void setButtonStateStart(boolean running) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    Boot.btn_start.setText("Navegando...");
                    Boot.btn_start.setEnabled(false);
                } else {
                    Boot.btn_start.setText("Iniciar >>");
                    Boot.btn_start.setEnabled(false);
                }
            }
        });
    }

    private void showTheRouteStatusOnTheDisplay() {
       try{
        double distanceMade = (Boot.previousSequence != 0) ? Boot.distancePerKm * (Boot.pointInRouter) : 0;
        int lestPoint = Boot.pointInRouter - 1;
        double distanceMadePorcents = 0;

        if(lestPoint!=0){
            distanceMadePorcents = ((Boot.distancePerKm * Boot.previousSequence) * 100) / (Boot.distancePerKm * (lestPoint));
        }
        Boot.statuskmsRouter.setText((distanceMade > 1) ? String.format("%.2f", distanceMade) + " Km" : String.format("%.2f", distanceMade) + " M");
        Boot.statusPorcentageRouter.setText(String.format("%.2f", distanceMadePorcents) + " %");
        //autoCloseFinishOperation(distanceMadePorcents);
       }catch (Exception e){
           Toast.makeText(CarRouteGuidanceActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
       }

    }

    private void autoCloseFinishOperation(double distanceMadePorcents) {
        try {
            Toast.makeText(Boot.contextActivity, String.valueOf(distanceMadePorcents), Toast.LENGTH_LONG).show();
            if (distanceMadePorcents == 100) {
                //this.finishOperaion(Boot.operationModelRepository);
                Boot.initPoint=false;
            }
        }catch (Exception e){
            Toast.makeText(CarRouteGuidanceActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void finishOperaion(Object objetct) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OperationModelRepository item = (OperationModelRepository) objetct;
                Boot.appOpeationsRepository.updateStateOperations(
                        String.valueOf(item.getOperationID()),
                        String.valueOf(item.getParternID()), 4);

                AlertDialog.Builder builder = new AlertDialog.Builder(Boot.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        try {
                            resetStateOperation();
                            finish();
                            Intent intent = new Intent(Boot.contextActivity, HomeActivity.class);
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
                Boot.dialog = builder.create();
                Boot.dialog.show();

            }
        });


    }

    private void resetStateOperation() {
        Boot.lastOperationAndActiveRoute.setProps(String.valueOf(Resources.LAST_ID_AND_POSSITION_OPERATION_CURRENTR));
        Boot.lastOperationAndActiveRoute.setContent("");
        Boot.lastOperationAndActiveRoute.setProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
        Boot.lastOperationAndActiveRoute.setContent("");
        Boot.previousSequence = 0;
        Boot.navegationsReaderDbHelper.update(Boot.lastOperationAndActiveRoute);

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
        Boot.mMap = googleMap;
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
        Boot.mMap.setMyLocationEnabled(true);
        Boot.mMap.getUiSettings().setCompassEnabled(true);

        Boot.mMap.getUiSettings().setRotateGesturesEnabled(true);
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
        Boot.mMap.setMyLocationEnabled(true);
        for (int next = 0; next < Boot.instructions.size(); next++) {
            String[] latLng = Functions.split("\\,",Boot.instructions.get(next).pontos);// Boot.instructions.get(next).pontos.split("\\,");
            Boot.markers.add(new Markers(latLng[0], latLng[1], String.format("Ponto : %s", next + 1)));
        }
        drawMapInMarkers();
    }

    private void drawAreaOfacting() {
        List<LatLng> area = new ArrayList<>();
        try{
            for (Zonas zona : Boot.zonas) {
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
                Boot.mMap.addPolygon(polygonOptions);

            }
            Locations locations = Boot.myPath.get(Boot.previousSequence);
            LatLng marketInit = new LatLng(locations.getLat(), locations.getLng());
            if (!Boot.initPoint) {
                Boot.mMap.addMarker(new MarkerOptions()
                        .position(marketInit)
                        .title("Inicial Rota..."));
                Boot.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marketInit, 11));
            }
        }catch (Exception e){

        }

    }


    private void knowWhereIAm(double latitude, double longitude) {
        //001
        int diff = 0;
        double distance = 0;
        int size = Boot.myPath.size();
        String marker = initString();
        actionPhoto(false);
        MarketRouter Mrouter;
        try{
            if (Boot.initPoint) {
                Boot.PointsRead++;
                if (!Boot.setOperationInit) {
                    Boot.appOpeationsRepository.updateStateOperations(
                            String.valueOf(Boot.operationModelRepository.getOperationID()),
                            String.valueOf(Boot.operationModelRepository.getParternID()), Enum.STATUS_IS_OPEN_OPERATION);
                    actionPhoto(true);
                    Boot.setOperationInit = true;
                }


                for (int next = (Boot.previousSequence ); next < size; next++) {
                    marker = String.format("%s%s", Boot.myPath.get(next).getLat(), Boot.myPath.get(next).getLng());
                    Boot.currentMarket = Boot.myPath.get(next);

                    if (Boot.lidos.indexOf(marker) == -1) {
                        diff = Math.abs(Boot.previousSequence - next);
                        distance = (distanceBetweenTwoPoints(latitude, longitude,
                                Boot.myPath.get(next).getLat(), Boot.myPath.get(next).getLng()));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Boot.contextActivity,"Processando : "+ br.com.stilldistribuidora.partners.views.CarRouteGuidanceActivity.Boot.previousSequence,Toast.LENGTH_SHORT).show();
                            }
                        });

                        if (distance < Boot.raio ) {
                            /*Desenha o mapa*/
                            Boot.previousSequence_ = Boot.previousSequence;
                            List<LatLng> routers = new ArrayList<>();
                            if(Boot.marketRouters.size() > Boot.previousSequence_+1){
                                    Boot.mMap.clear();
                            }


                            if(Boot.previousSequence==0){
                                Mrouter = Boot.marketRouters.get(Boot.previousSequence_);
                            }else{
                                Mrouter = Boot.marketRouters.get(Boot.previousSequence_-1);
                            }



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
                            if((Boot.previousSequence_ - (Boot.marketRouters.size()-3)) < Boot.marketRouters.size()-3) {

                                if(Boot.marketRouters.size() > (Boot.previousSequence_) ) {
                                    Mrouter = Boot.marketRouters.get(Boot.previousSequence_ + 1);
                                    routers.add(new LatLng(Mrouter.start.lan, Mrouter.start.lng));
                                    for (MarketIgn p : Mrouter.ignoredPoints) {
                                        routers.add(new LatLng(p.lan, p.lng));
                                    }
                                    if (Mrouter.fim != null) {
                                        routers.add(new LatLng(Mrouter.fim.lan, Mrouter.fim.lng));
                                    } else {
                                        System.out.println("");
                                    }
                                    mapPolyline = new PolylineOptions();
                                    mapPolyline.width(25)
                                            .color(Color.BLUE)
                                            .geodesic(true);
                                    for (LatLng mk : routers) {
                                        mapPolyline.add(mk);
                                    }
                                }

                            }else  if(Boot.previousSequence_ < Boot.marketRouters.size()-2) {
                                Mrouter = Boot.marketRouters.get(Boot.previousSequence_ + 1);
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
                            } else  if(Boot.previousSequence_ < Boot.marketRouters.size()-1) {
                                Mrouter = Boot.marketRouters.get(Boot.previousSequence_ + 1);
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
                            Boot.mMap.addPolyline(finalMapPolyline);

                            Boot.ultimo_comando = Boot.myPath.get(next).getDirecao();
                            Boot.previousSequence = next;
                            //drawMapInMarkers();
                            showTheRouteStatusOnTheDisplay();
                            performAudioGuidance();
                            Boot.positionError = 0;
                            Boot.lidos.add(marker);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Boot.contextActivity,Boot.previousSequence+" |X "+
                                            Boot.previousSequence_,Toast.LENGTH_SHORT).show();
                                }
                            });
                            RouterMovimentModel moviments = new RouterMovimentModel();
                            moviments.setAction("F");
                            moviments.setIdKeys(String.valueOf(Boot.currentMarket.getId()));
                            moviments.setStartPoints(String.valueOf(Boot.previousSequence));
                            moviments.setDeviceID(Boot.operationModelRepository.getDeviceID());
                            moviments.setDeliveryId(String.valueOf(Boot.operationModelRepository.getOperationID()));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Boot.contextActivity,Boot.previousSequence,Toast.LENGTH_SHORT).show();
                                }
                            });
                            //#2023
                            if(!Boot.operationModelRepository.getDeviceID().isEmpty()){
                                Boot.routerMovimentDbHelper.insert(moviments);
                                Boot.PointsRead =0;
                                final long update = Boot.navegationsReaderDbHelper.update(getRouterCurrent(next));

                                System.out.println("#233 "+update);
                                System.out.println("#233 "+getRouterCurrent(next));
                                System.out.println("#233 "+next);


                            }
                            System.out.println("#12 Feito ["+Boot.previousSequence+"] ");
                            StringBuffer losg = new StringBuffer();
                            for (String v : Boot.lidos) {
                                losg.append(v);
                            }
                        } else if (diff != 1) {
                            Boot.positionError++;
                        }
                    }
                }
            }
            if (Boot.positionError == Boot.startBoxMensageError) {
                Boot.responseGenerics.OnResponseType(Resources.ERROR_POINT_NOT_FOUND, null);
                return;
            }
        }catch (Exception e){
                e.printStackTrace();
        }


    }

    private NavegationModel getRouterCurrent(int next) {
        String latLon = String.format("%s,%s", Boot.myPath.get(next).getLat(), Boot.myPath.get(next).getLng());
        Boot.lastOperationAndActiveRoute.setProps(String.valueOf(Resources._SYSTEM_LAST_POINT_));
        Boot.lastOperationAndActiveRoute.setContent(String.format("%s|%s|%s|%s",Boot.operationModelRepository.getOperationID(),Boot.rotaActiva, latLon, next));
        System.out.println("#233 **"+ Boot.lastOperationAndActiveRoute.getContent());

        return Boot.lastOperationAndActiveRoute;
    }

    private String initString() {
        return "";
    }

    public void drawMapInMarkers() {
        try{
            System.out.println("#5 processando..(Pintando map");
            Boot.mMap.clear();
            drawAreaOfacting();
            int size = Boot.markers.size();
            LatLng p1 = null, p2 = null;
            List<LatLng> lts = new ArrayList<>();
            boolean pontoTravado = false, nextPonits = false;
            for (int next = 0; next < size; next++) {
                p1 = new LatLng(
                        Double.parseDouble(Boot.markers.get(next).getLat()),
                        Double.parseDouble(Boot.markers.get(next).getLng()));

                if (next < Boot.previousSequence && Boot.previousSequence != 0) {
             /*   MarkerOptions pt = new MarkerOptions().position(p1)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                pt.title("Ponto : " + (next));
                gui.mMap.addMarker(pt);*/
                } else {
                    if ((next == Boot.previousSequence || Boot.previousSequence == 0) && !pontoTravado) {
                        MarkerOptions pt = new MarkerOptions().position(p1).
                                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                        pt.title("Inicio....").snippet("Blah");
                        Boot.mMap.addMarker(pt);
                        pontoTravado = true;
                    } else {
                        if (next > Boot.previousSequence) {
                            if (!nextPonits) {
                                MarkerOptions pt = new MarkerOptions().position(p1).
                                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                pt.title("Ponto : " + (next + 1));
                                Boot.mMap.addMarker(pt);
                                nextPonits = true;
                            }

                        }
                    }
                }
                lts.add(p1);
                    /*
                new CameraPosition.Builder().target(lts.get(lts.size() - 1))
                        .zoom(20f)
                        .bearing(10)
                        .tilt(50)
                        .build();
                        */

/*
              CameraPosition BONDI =
                        new CameraPosition.Builder()
                                .target(lts.get(lts.size() - 1))
                                .zoom(20f)
                                .bearing(200)
                                .tilt(90)
                                .build();


                Boot.mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(BONDI)  );

 */


            }

   /*
              Boot.mMap.animateCamera(
                      CameraUpdateFactory.newLatLngZoom(
                                lts.get(lts.size() - 1),
                              30f)   );


            }
            */

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
        Boot.mMap.clear();
        int size = Boot.markers.size();
        LatLng p1 = null;
        List<LatLng> lts = new ArrayList<>();
        for (int next = 0; next < size; next++) {
            p1 = new LatLng(
                    Double.parseDouble(Boot.markers.get(next).getLat()),
                    Double.parseDouble(Boot.markers.get(next).getLng()));
            lts.add(p1);
        }
        PolylineOptions gg = new PolylineOptions();
        for (LatLng df : lts) {
            gg.add(df);
        }
        Polyline line = Boot.mMap.addPolyline(gg);
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
                       Boot.uncheckedlayot.setVisibility(View.VISIBLE);
                       return true;

                   case R.id.finishoperation:
                       stopRouterGps();
                       return true;

                   case R.id.menucloseoperation:
                    this.finishOperaion(Boot.operationModelRepository);
                    return true;

                   case R.id.menusyncronized:

                       return true;




            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private static class Boot {
        public List<MarketRouter> marketRouters = new ArrayList<>();

        public final List<Markers> markers = new ArrayList<>();
        public final List<Locations> myPath = new ArrayList<>();
        private final List<String> lidos = new ArrayList<>();
        public ActivityResultLauncher<Intent> receiveAnswerFromChildren;
        public Context contextActivity;
        public Toolbar toolbar;
        public OperationModelRepository operationModelRepository;

        public AudioRepository audioRepository;
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
        public double raio = 20;//metros
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
        public File audio;
        public int previousSequence_=0;
        public File audioCabin;
        public FileManager fileManager;
        public CabinAudioOperationRepository cabinRepository;
        public ImageView palyStop;
        public TextView cronometer;
        public int interval;

        public  Handler handlerCronometer;

        public Runnable runnable;
        public CabinAudioUploadFile cabinAudioUploadFile;
        public boolean runningDisplay;
        public MediaRecorder recorder;
        ActivityResultLauncher<Uri> takeAPhoto;
        ActivityResultLauncher<Intent> startCamera;
        String currentPhotoPath;

        public Handler handler;
        public boolean isRunning;
        public final long MILLIS_IN_SEC = 1000L;
        public final int SECS_IN_MIN = 60;


        public final int SECS_IN_HOURS= 3600;

        public  long initialTime;
        public MediaPlayer mediaPlayer;


    }


}
