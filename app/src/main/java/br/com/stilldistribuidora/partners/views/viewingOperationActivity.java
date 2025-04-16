package br.com.stilldistribuidora.partners.views;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.stilldistribuidora.common.LoadProcess;
import br.com.stilldistribuidora.httpService.partners.PartnersServices;
import br.com.stilldistribuidora.partners.views.core.entity.PartnersOperationControlStateEntity;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.lib.PartenerHelpes;
import br.com.stilldistribuidora.partners.views.core.models.OperationControlStateModel;
import br.com.stilldistribuidora.partners.views.core.models.OperationsPartnersModel;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.partners.views.core.services.GpsPartners;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.AvailableOperation;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.db.models.MyRouterSerialized;
import br.com.stilldistribuidora.stillrtc.db.models.RouterApiDirectionContainer;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;

public class viewingOperationActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static final long MILLIS_IN_SEC = 1000L;
    private static final int SECS_IN_MIN = 60;
    private static long initialTime;
    private static Handler handler;
    private static boolean isRunning;
    public ActivityResultLauncher<Intent> receiveAnswerFromChildren;
    public Uri cam_uri;
    ActivityResultLauncher<Uri> takeAPhoto;
    ActivityResultLauncher<Intent> startCamera;
    String currentPhotoPath;
    private final Components componentsStatics = new Components();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_operation);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        InitPermisionGeral();
        Intent i = getIntent();
        setSupportActionBar(myToolbar);
        if (i.hasExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE)) {
            componentsStatics.routerSerialized = (MyRouterSerialized) i.getSerializableExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE);
        }
        handler = new Handler();
        componentsStatics.self = this;

        componentsStatics.loadProcess = new LoadProcess(componentsStatics.self, "Carregando Informaçãos  ...", false);


        componentsStatics.btnInitCronometro = (Button) findViewById(R.id.btnInitCronometro);
        componentsStatics.btnGaleriaAtiva = (Button) findViewById(R.id.btnGaleriaAtiva);
        componentsStatics.Navegador = (Button) findViewById(R.id.Navegador);


        componentsStatics.fotoview = (ImageView) findViewById(R.id.fotoview);
        componentsStatics.btnStopitCronometro = (Button) findViewById(R.id.btnStopitCronometro);
        componentsStatics.photosPartnersModel = new PhotosPartnersModel(componentsStatics.self);
        componentsStatics.operationsPartnersModel = new OperationsPartnersModel(componentsStatics.self);
        componentsStatics.operationControlStateModel = new OperationControlStateModel(componentsStatics.self);
        componentsStatics.selectedRoute = (MyRouterSerialized) i.getSerializableExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE);
        componentsStatics.operationDeliveryId = (TextView) findViewById(R.id.operationDeliveryId);
        componentsStatics.operationRouterId = (TextView) findViewById(R.id.operationRouterId);
        componentsStatics.storeName = (TextView) findViewById(R.id.storeName);
        componentsStatics.btnPauzedCronometro = (Button) findViewById(R.id.operationPaused);
        componentsStatics.displayTimer = (TextView) findViewById(R.id.displayTime);
        componentsStatics.apiConfig = new ApiConfig(componentsStatics.self);
        componentsStatics.configModel = new ConfigDataAccess(componentsStatics.self);

        componentsStatics.operationDeliveryId.setText(componentsStatics.routerSerialized.getOperation());
        componentsStatics.operationRouterId.setText(componentsStatics.routerSerialized.getRouter_id());
        componentsStatics.storeName.setText(componentsStatics.routerSerialized.getStoreName());


        //inicializa o Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        componentsStatics.receiveAnswerFromChildren = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        OnActityGenericsResults(result);
                    }
                });


        Config user = (Config) componentsStatics.apiConfig.Api.getConfigDataAccess().getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));
        try {
            JSONObject userJson = new JSONObject(user.getDataJson());
            componentsStatics.uuid = userJson.getString("uuid");
        } catch (Exception e) {
        }


        controlesOperations(false);


        startGps();
        initEvents();

        componentsStatics.operationsState = (List<PartnersOperationControlStateEntity>) componentsStatics
                .operationControlStateModel.by(new String[]{componentsStatics.uuid, componentsStatics.routerSerialized.getOperation()});


        List<PartnersOperationControlStateEntity> openOperations = (List<PartnersOperationControlStateEntity>) componentsStatics
                .operationControlStateModel.
                operationState(new String[]{componentsStatics.uuid, componentsStatics.routerSerialized.getOperation(), "P"});

        if (openOperations.size() > 0) {

            //Cria o gerador do AlertDialog
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(componentsStatics.self);
            //define o titulo
            builder.setTitle("Informação");
            //define a mensagem
            builder.setMessage("Você Tem Operações Em Aberto. Não Pode Inicializar Outra no Momento.");
            //define um botão como positivo
            builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE, componentsStatics.selectedRoute);
                    setResult(Constants.FINISH_OPERATION_OK_ACTIONS, intent);
                    intent = new Intent(componentsStatics.self, HomeParceiro.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            //cria o AlertDialog
            componentsStatics.alerta = builder.create();
            //Exibe
            componentsStatics.alerta.show();


        }


        if (componentsStatics.operationsState.size() != 0) {
            componentsStatics.btnInitCronometro.setText("Continuar...");
            componentsStatics.displayTimer.setText(componentsStatics.operationsState.get(0).getTime_current());
        }
        loadStartGpsConfiguration();
        startActivityOnResult();
        startThreads();
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();

        if (!memoryInfo.lowMemory) {
            // Do memory intensive work ...
            Toast.makeText(this, "Processamento....", Toast.LENGTH_LONG).show();
        }

        componentsStatics.loadProcess.loadActivityClose();
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


    private void OnActityGenericsResults(ActivityResult result) {
        if (result.getResultCode() == Constants.FINISH_OPERATION_OK_ACTIONS) {
            Intent i = getIntent();
            MyRouterSerialized serializableExtra = (MyRouterSerialized) i.getSerializableExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE);
            PartnersOperationControlStateEntity partnersOperationControlStateEntity = componentsStatics.operationsState.get(0);
            partnersOperationControlStateEntity.setDelivery_state("F");
            partnersOperationControlStateEntity.setCreated_at(PartenerHelpes.getDate());
            componentsStatics.operationControlStateModel.update(partnersOperationControlStateEntity);
            Intent intent = new Intent(componentsStatics.self, GpsPartners.class);
            stopService(intent);
            componentsStatics.IsRunningOperation = false;
            controlesOperations(false);
            intent = new Intent(componentsStatics.self, HomeParceiro.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        } else if (result.getResultCode() == Constants.FINISH_OPERATION_VIEW_PHOTO_CLOSE) {

            Toast.makeText(componentsStatics.self, "Galeria Fechado", Toast.LENGTH_LONG).show();

        }

    }


    private void controlesOperations(boolean ativo) {
       /* if (ativo) {
            componentsStatics.btnInitCronometro.setVisibility(View.GONE);
            componentsStatics.btnStopitCronometro.setVisibility(View.VISIBLE);
            componentsStatics.btnPauzedCronometro.setVisibility(View.VISIBLE);
            componentsStatics.fotoview.setVisibility(View.VISIBLE);
            componentsStatics.Navegador.setVisibility(View.VISIBLE);
        } else {

            componentsStatics.btnInitCronometro.setVisibility(View.VISIBLE);
            componentsStatics.btnStopitCronometro.setVisibility(View.GONE);
            componentsStatics.btnPauzedCronometro.setVisibility(View.GONE);
            componentsStatics.fotoview.setVisibility(View.GONE);
            componentsStatics.Navegador.setVisibility(View.GONE);


        }*/
    }

    private void startThreads() {


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
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void pickCamera() {
        InitPermisionGeral();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        cam_uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);
        //startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE); // OLD WAY

        componentsStatics.loadProcess = new LoadProcess(componentsStatics.self, "Carregando Camera  ...", false);
        startCamera.launch(cameraIntent);                // VERY NEW WAY
        componentsStatics.loadProcess.loadActivityClose();


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


    private void drawMapInLines() {
        //21

        try {
            List<LatLng> lts = new ArrayList<>();
            JSONArray routerInstructions = new JSONArray(componentsStatics.routerSerialized.getInstruction_router());
            for (int next = 0; next < routerInstructions.length(); next++) {

                try {

                    if (!routerInstructions.getJSONObject(next).isNull("points")) {
                        String[] points = (routerInstructions.getJSONObject(next).getString("points")).split("\\,");
                        if (points[0] != null && points[1] != null)
                            lts.add(new LatLng(Double.valueOf(points[0]), Double.valueOf(points[1])));
                    }
                } catch (Exception e) {

                }
            }


            PolylineOptions drawLineInMap = new PolylineOptions();
            for (LatLng df : lts) {
                drawLineInMap.add(df);
            }
            Polyline line = componentsStatics.map.addPolyline(drawLineInMap);
            componentsStatics.map.addMarker(new MarkerOptions()
                    .position(lts.get(0)).title("Operação Inicia Aqui..")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            componentsStatics.map.addMarker(new MarkerOptions()
                    .position(lts.get(lts.size() - 1)).title("Operação Termina Aqui.."));
            componentsStatics.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            componentsStatics.map.moveCamera(CameraUpdateFactory.newLatLngZoom(lts.get(0), 10));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("#4 " + e.getMessage());
        }

    }


    private void startActivityOnResult() {
        receiveAnswerFromChildren = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Constants.FINISH_OPERATION_OK_ACTIONS) {


                        }

                    }
                });

        startCamera = registerForActivityResult(
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
                                        String realPathFromURI = getRealPathFromURI(cam_uri);
                                        System.out.println("#4 " + cam_uri.getPath());
                                        photoFile = createImageFile();
                                        String[] geo = (((String) obj).split("\\|"))[0].split("\\,");
                                        PhotoEntity photoEntity = new PhotoEntity();
                                        photoEntity.setCreated_at(PartenerHelpes.getDate());
                                        photoEntity.setUri(realPathFromURI);
                                        photoEntity.setSync("0");
                                        photoEntity.setDelivery_id(componentsStatics.operationDeliveryId.getText().toString());
                                        photoEntity.setLatitude(geo[0]);
                                        photoEntity.setLongitude(geo[1]);
                                        photoEntity.setPic_uid("0");
                                        photoEntity.setType("1");
                                        photoEntity.setPartners_id(componentsStatics.uuid);
                                        photoEntity.setDeviceId(componentsStatics.routerSerialized.getDeviceId());
                                        if (componentsStatics.photosPartnersModel.save(photoEntity) > 0) {
                                            Toast.makeText(componentsStatics.self, "Foto Salva Com Sucesso..!", Toast.LENGTH_SHORT).show();
                                        }
                                        try {
                                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(cam_uri.toString()));
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

    private void initEvents() {


        componentsStatics.openViewPicturesOperations = (Button) findViewById(R.id.openViewPicturesOperations);
        componentsStatics.Navegador.setVisibility(View.GONE);
        componentsStatics.Navegador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    try {
                        String operationKeys = String.format("%s|%s|%s", componentsStatics.routerSerialized.getOperation()
                                , componentsStatics.routerSerialized.getRouter_id(), componentsStatics.uuid);
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.OPEN_ROUTERIDADOR_PACKAGE);
                        launchIntent.putExtra(Constants.STR_OPERATION_ID, operationKeys);
                        launchIntent.putExtra(Constants.STR_OPERATION_COMANDS, componentsStatics.routerSerialized.getInstruction_router());
                        launchIntent.putExtra(Constants.STR_OPERATION_COMANDS_IS_ROTAS, componentsStatics.routerSerialized.getRouter_id());
                        launchIntent.putExtra(Constants.STR_OPERATION_COMANDS_CREATED, componentsStatics.routerSerialized.getCreated());
                        launchIntent.putExtra(Constants.STR_OPERATION_COMANDS_DEVICE, componentsStatics.routerSerialized.getDeviceId());


                        if (launchIntent != null) {
                            startActivity(launchIntent);
                        } else {
                            Toast.makeText(viewingOperationActivity.this, "Aplicativo De Rota  Não Encontrado..", Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {

                    }

                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        componentsStatics.btnStopitCronometro = (Button) findViewById(R.id.btnStopitCronometro);
        componentsStatics.btnStopitCronometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(componentsStatics.self);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                        // Add action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                                componentsStatics.loadProcess = new LoadProcess(componentsStatics.self, "Processando  ...", false);
                                Intent intent = new Intent(componentsStatics.self, PartnersFinishOperationActivity.class);
                                intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE, componentsStatics.routerSerialized);
                                componentsStatics.receiveAnswerFromChildren.launch(intent);
                                componentsStatics.loadProcess.loadActivityClose();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
        componentsStatics.btnInitCronometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (componentsStatics.operationsState.size() == 0) {
                    initializeOrContinueOperationActive();
                } else {
                    componentsStatics.displayTimer.setText(componentsStatics.operationsState.get(0).getTime_current());
                    initializeOrContinueOperationActive();
                }
            }
        });
        componentsStatics.fotoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickCamera();
            }
        });
        componentsStatics.btnPauzedCronometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //89


                try {
                    Toast.makeText(componentsStatics.self, "Operação Pausada Com Sucesso.!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(componentsStatics.self, GpsPartners.class);
                    stopService(intent);
                    componentsStatics.IsRunningOperation = false;
                    controlesOperations(false);

                    String operationKeys = String.format("%s|%s|%s", componentsStatics.routerSerialized.getOperation()
                            , componentsStatics.routerSerialized.getRouter_id(), componentsStatics.uuid);
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(Constants.OPEN_ROUTERIDADOR_PACKAGE);
                    launchIntent.putExtra(Constants.STR_OPERATION_ID, operationKeys);
                    launchIntent.putExtra(Constants.STR_OPERATION_COMANDS, componentsStatics.routerSerialized.getInstruction_router());
                    launchIntent.putExtra(Constants.STR_OPERATION_COMANDS_IS_ROTAS, componentsStatics.routerSerialized.getRouter_id());
                    launchIntent.putExtra(Constants.STR_OPERATION_COMANDS_CREATED, componentsStatics.routerSerialized.getCreated());
                    launchIntent.putExtra(Constants.STR_OPERATION_COMANDS_STOP, "1");

                    if (launchIntent != null) {
                        startActivity(launchIntent);
                    } else {
                        Toast.makeText(viewingOperationActivity.this, "Aplicativo De Rota  Não Encontrado..", Toast.LENGTH_LONG).show();
                    }
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });
        componentsStatics.btnGaleriaAtiva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                componentsStatics.loadProcess = new LoadProcess(componentsStatics.self, "Processando...", false);
                AvailableOperation operationSelectd = new AvailableOperation();
                operationSelectd.setOperation(componentsStatics.routerSerialized.getOperation());
                operationSelectd.setIrPartrner(componentsStatics.uuid);
                Intent intent = new Intent(componentsStatics.self, GaleriaOperationViewActivity.class);
                intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_SELECT_IMAGE, operationSelectd);
                startActivity(intent);
                componentsStatics.loadProcess.loadActivityClose();


            }
        });
    }


    private void initializeOrContinueOperationActive() {
        //22
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Intent intent = new Intent(componentsStatics.self, GpsPartners.class);
            intent.putExtra("deviceIdentifier", componentsStatics.uuid);
            intent.putExtra("deviceIdentifierAuto", componentsStatics.routerSerialized.getDeviceId());
            intent.putExtra("deliveryFragmentId", componentsStatics.routerSerialized.getOperation());
            intent.putExtra("routerId", componentsStatics.routerSerialized.getRouter_id());
            startForegroundService(intent);
            saveStateControlOperation();
        } else {
            Intent intent = new Intent(componentsStatics.self, GpsPartners.class);
            intent.putExtra("deviceIdentifier", componentsStatics.uuid);
            intent.putExtra("deliveryFragmentId", componentsStatics.routerSerialized.getOperation());
            intent.putExtra("deviceIdentifierAuto", componentsStatics.routerSerialized.getDeviceId());
            intent.putExtra("routerId", componentsStatics.routerSerialized.getRouter_id());
            startService(intent);
            saveStateControlOperation();

        }
        componentsStatics.IsRunningOperation = true;

        startCronometro();


        controlesOperations(true);

    }

    private void startCronometro() {

//22
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (componentsStatics.IsRunningOperation) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    componentsStatics.operationsState = (List<PartnersOperationControlStateEntity>) componentsStatics
                                            .operationControlStateModel.by(new String[]{componentsStatics.uuid, componentsStatics.routerSerialized.getOperation()});


                                    String timecurrent = "00:00:00";
                                    timecurrent = componentsStatics.operationsState.get(0).getTime_current();
                                    componentsStatics.displayTimer.setText(timecurrent);
                                    System.out.println("#4  cronometro 4444  Tela " + timecurrent);
                                } catch (Exception e) {

                                    System.out.println("#4  " + e.getMessage());
                                }
                            }
                        });


                        Thread.sleep(2 * 1000);
                    } catch (Exception e) {

                        System.out.println("#4  " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    private void saveStateControlOperation() {
        PartnersOperationControlStateEntity control = new PartnersOperationControlStateEntity();
        control.setDelivery_state("P");
        control.setRoute_id(componentsStatics.routerSerialized.getRouter_id());
        control.setId_parteners(componentsStatics.uuid);
        control.setTime_current("00:00:00");
        control.setCreated_at(PartenerHelpes.getDate());
        control.setDelivery_fragment_id(componentsStatics.routerSerialized.getOperation());

        if (componentsStatics.operationsState.size() == 0) {
            componentsStatics.operationControlStateModel.save(control);
            Toast.makeText(componentsStatics.self, "Operação Inicializada Com Sucesso..!", Toast.LENGTH_LONG).show();
            componentsStatics.IsRunningOperation = true;
        } else {
            if (!componentsStatics.operationsState.get(0).getDelivery_state().equals("P")) {
                Toast.makeText(componentsStatics.self, "Erro ao  Inicializada Operação.!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(componentsStatics.self, GpsPartners.class);
                stopService(intent);
                componentsStatics.IsRunningOperation = false;
                controlesOperations(false);
            }
        }
    }

    private void startGps() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Verifica se o GPS está ativo
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // Caso não esteja ativo abre um novo diálogo com as configurações para
        // realizar se ativamento
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private void loadStartGpsConfiguration() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
        wakeLock.acquire();
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

    private void initcializationGps() {
        componentsStatics.fusedLocationClient = LocationServices.getFusedLocationProviderClient(componentsStatics.self);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        componentsStatics.map.setMyLocationEnabled(true);
        componentsStatics.map.getUiSettings().setMyLocationButtonEnabled(false);

        componentsStatics.fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) componentsStatics.self, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (location != null) {

                                    System.out.println("#99999 Atualizando Gps " + location.getLatitude() + " _ " + location.getLongitude());

                                    //   gui.latiudeActive = location.getLatitude();
                                    // gui.longitudeActive = location.getLongitude();
                                    //  System.out.println("#99999 Atualizando Gps " + gui.longitudeActive + " _ " + gui.latiudeActive);
                                    //   String SS=gui.latiudeActive+","+gui.longitudeActive;


                                }

                            }
                        }).start();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_available, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.system_last:
                this.onBackPressed();
                return true;
            case R.id.system_close:
                Intent i = new Intent(getApplicationContext(), LoginAppActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        componentsStatics.map = googleMap;
        componentsStatics.map.getUiSettings().setMapToolbarEnabled(true);
        componentsStatics.map.getUiSettings().setMyLocationButtonEnabled(true);
        componentsStatics.map.getUiSettings().setCompassEnabled(true);
        componentsStatics.map.getUiSettings().setTiltGesturesEnabled(true);

        drawMapInLines();

        componentsStatics.mapNotLoad = false;


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*Permisoes Gerais*/
    public void InitPermisionGeral() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INSTALL_SHORTCUT) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.LOCATION_HARDWARE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
                                android.Manifest.permission.ACCESS_NETWORK_STATE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_PHONE_STATE,
                                android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.INSTALL_SHORTCUT,
                                android.Manifest.permission.LOCATION_HARDWARE,
                                android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                                android.Manifest.permission.WRITE_SETTINGS,
                                android.Manifest.permission.WAKE_LOCK,
                                android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                        }, 10000);
            }

        }


    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    public void onDestroy() {
        componentsStatics.photosPartnersModel.close();
        componentsStatics.operationsPartnersModel.close();
        componentsStatics.operationControlStateModel.close();
        super.onDestroy();

    }

    public static class ApiConfig {
        private final AppCompatActivityBase Api;

        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }

    public static class Components {
        public ActivityResultLauncher<Intent> receiveAnswerFromChildren;

        public ApiConfig apiConfig;
        public Context self;
        public Config userLogged;
        public Config token;
        public ConfigDataAccess configModel;
        public RouterApiDirectionContainer routerApiDirectionContainer;
        public LatLng startService;
        public LatLng startServiceOperation;
        public TextView routerInformation;
        public Dialog alerta;
        public boolean processando = false;
        public boolean mapNotLoad = true;
        public Button btnStopitCronometro, Navegador;
        public ImageView fotoview;
        public PhotosPartnersModel photosPartnersModel;
        public OperationsPartnersModel operationsPartnersModel;
        public OperationControlStateModel operationControlStateModel;
        public MyRouterSerialized routerSerialized;
        public TextView operationDeliveryId;
        public TextView operationRouterId;
        public TextView storeName;
        public Button btnPauzedCronometro;
        public Location mLastKnowLocation;
        public List<PartnersOperationControlStateEntity> operationsState;
        public TextView displayTimer;
        public Runnable temporizador;
        public boolean IsRunningOperation = false;
        public Button openViewPicturesOperations;
        public LoadProcess loadProcess;
        private String uuid;
        private OnClick onClick;
        private GoogleMap map;
        private FusedLocationProviderClient fusedLocationClient;
        private MyRouterSerialized selectedRoute;
        private PartnersServices partnersServices;
        private ProgressDialog pd;
        private Button btnVoltarPosition, btnIr, btnInitCronometro;
        private final Handler handler = new Handler();
        // Variable to store brightness value
        private int brightness;
        // Content resolver used as a handle to the system's settings
        private ContentResolver cResolver;
        // Window object, that will store a reference to the current window
        private Window window;

        private Button btnGaleriaAtiva;

    }


}