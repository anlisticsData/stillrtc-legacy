package br.com.stilldistribuidora.partners.views;


import android.Manifest;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import br.com.stilldistribuidora.partners.Adapter.ConfigurationsUser;
import br.com.stilldistribuidora.partners.Base.ActivityBaseApp;
import br.com.stilldistribuidora.partners.Base.Notifications;
import br.com.stilldistribuidora.partners.Casts.AvatarCastReceive;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Commom.Functions;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Contracts.OnSetValue;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Services.ServicesApiStill;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.CommonFunctions;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;
import br.com.stilldistribuidora.stillrtc.services.ServiceApp;
import br.com.stilldistribuidora.stillrtc.services.ServiceDeviceStatus;

public class
HomeActivity extends ActivityBaseApp {
    private static final Components components = new Components();
    public ActivityResultLauncher<Intent> startCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Gson gson = new Gson();
        components.contextActivity = this;
        components.MyNotifications = new Notifications(components.contextActivity );
        components.avatar = findViewById(R.id.avatar);
        components.openStore = findViewById(R.id.openStore);
        components.openCameraAvatar = findViewById(R.id.openCameraAvatar);
        components.openMyDate = findViewById(R.id.openMyDate);
        components.openMyOperations = findViewById(R.id.openMyOperations);
        components.openOperationsNext = findViewById(R.id.openOperationsNext);
        components.openOtherOperations = findViewById(R.id.openOtherOperations);
        components.openCloseApp = findViewById(R.id.openCloseApp);
        components.txt_name = findViewById(R.id.txt_name);
        components.stillCoins = findViewById(R.id.stillCoins);
        components.txtNivel = findViewById(R.id.txtNivel);
        components.level = findViewById(R.id.level);
        components.conf = new ServerConfiguration(components.contextActivity);
        components.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(this));
        components.servicesHttp = new ServicesHttp(this, components.conf, CallBackOnResponseGlobalApi());
        components.appOpeationsRepository = new AppOpeationsRepository(components.contextActivity);
        components.openalert = findViewById(R.id.openalert);
        components.progressLevel = findViewById(R.id.progressLevel);
        InitPermisionGeral();
        String userLogged = components.conf.getUserLogged();
        components.userLoginCast = gson.fromJson(userLogged, UserLoginCast.class);
        components.txt_name.setText(components.userLoginCast.getName().trim());
        getUpdateAvatar();
        initStartServices();
        startOnEventsResultsActivity();
        alerts();
        setNivelUserExprerience( components.userLoginCast.getExperiences().ate,components.userLoginCast.getExperiences().description);
        components.onSetValue =  new OnSetValue() {
            @Override
            public void value(Object value) {
                components.hasOperationAvailableSize=(int) value;
            }
        };

    }

    private void setNivelUserExprerience(String maxExp,String descriptionExperiences) {
        try {
            Double exp1 = Math.floor(Double.parseDouble(maxExp));
            components.progressLevel.setMax(exp1.intValue());
            String kmsExperience = components.userLoginCast.getNivel().kmsExperience;
            Double kms1 = Functions.floor(Functions.workExperience(kmsExperience));
            components.progressLevel.setProgress(kms1.intValue());
            components.level.setText(descriptionExperiences);
        } catch (Exception ex) {
            components.progressLevel.setMax(10);
            components.progressLevel.setProgress(1);
            components.level.setText(descriptionExperiences);

        }

    }

    private void alerts() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    components.servicesHttp.issueOperationAlertAvailable(
                        Integer.parseInt(components.userLoginCast.getUuid()),
                        1,
                        new OnResponseHttp() {
                        @Override
                        public void onResponse(Object data) {
                            try {
                                ArrayList<String> strList = (ArrayList<String>) (ArrayList<?>) (data);

                                if (strList.size() > 0) {
                                    components.onSetValue.value(1);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            components.openalert.setBackground(getDrawable(R.drawable.ic_alert_item));
                                            components.openalert.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCc61729")));

                                            try{
                                                if (!components.MyNotifications.isHasNotificationActive()) {
                                                    components
                                                        .MyNotifications
                                                        .Notificar("Você tem ("+strList.size()+") operações disponíveis.");
                                                }
                                            }catch (Exception e){
                                                components.onSetValue.value(0);
                                            }
                                        }
                                    });
                                } else {
                                    components.onSetValue.value(0);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            components.openalert.setBackground(getDrawable(R.drawable.ic_no_alert));
                                            components.openalert.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CC1680C5")));
                                        }
                                    });
                                }
                            }catch (Exception e){
                                components.onSetValue.value(0);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        components.openalert.setBackground(getDrawable(R.drawable.ic_no_alert));
                                        components.openalert.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CC1680C5")));
                                    }
                                });
                            }

                        }

                        @Override
                        public void onError(Object error) {
                            components.onSetValue.value(0);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    components.openalert.setBackground(getDrawable(R.drawable.ic_no_alert));
                                    components.openalert.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CC1680C5")));
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!components.stop) {
                    handler.postDelayed(this, 6000);
                }
            }
        });
    }

    public void onHasOperations (View view) {
        components.hasOperationAvailable=false;
        Intent intent = new Intent(components.contextActivity, OpcomingOperations.class);
        intent.putExtra("onHasOperations", (components.hasOperationAvailableSize > 0) ? "true":"false");
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
    }

    private void issueOperationAlertAvailable() {
        components.hasOperationAvailable=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (components.hasOperationAvailable){
                    System.out.println("#4 Alerts");
                    try {
                        components.servicesHttp.
                                issueOperationAlertAvailable(Integer.parseInt(components.userLoginCast.getUuid()),
                                        1, new OnResponseHttp() {
                                    @Override
                                    public void onResponse(Object data) {
                                        try{
                                            ArrayList<String> strList = (ArrayList<String>)(ArrayList<?>)(data);
                                            System.out.println("#4 Alerts ["+String.valueOf(strList.size()) +"]" );
                                            if(strList.size()>0){
                                                components.onSetValue.value(1);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        components.openalert.setBackground(getDrawable(R.drawable.ic_alert_item));
                                                        components.openalert.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCc61729")));
                                                        try{
                                                            if(!components.MyNotifications.isHasNotificationActive()){
                                                                components.MyNotifications.
                                                                        Notificar("Voçê Tem ("+strList.size()+") Operação Disponível Still Distribuidora.");
                                                            }
                                                        }catch (Exception e){
                                                            components.onSetValue.value(0);
                                                        }

                                                    }
                                                });
                                            }else{
                                                components.onSetValue.value(0);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        components.openalert.setBackground(getDrawable(R.drawable.ic_no_alert));
                                                        components.openalert.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CC1680C5")));
                                                    }
                                                });
                                            }
                                        }catch (Exception e){
                                            components.onSetValue.value(0);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    components.openalert.setBackground(getDrawable(R.drawable.ic_no_alert));
                                                    components.openalert.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CC1680C5")));
                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void onError(Object error) {
                                        components.onSetValue.value(0);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                components.openalert.setBackground(getDrawable(R.drawable.ic_no_alert));
                                                components.openalert.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CC1680C5")));
                                            }
                                        });
                                    }
                                });


                        Thread.sleep(40*1000);

                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        components.onSetValue.value(0);
                    }
                }
            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        components.hasOperationAvailable=false;
    }

    @Override
    protected void onDestroy() {
        Intent serviceSynchronizedOperations = new Intent(components.contextActivity, ServicesApiStill.class);
        stopService(serviceSynchronizedOperations);
        super.onDestroy();
    }

    private void initStartServices() {
        startService(new Intent(components.contextActivity, ServicesApiStill.class));
    }

    private void updateInfoUser() {
        Locale localeBR = new Locale("pt","BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeBR);
        String show = numberFormat.format(components.configurationsAdapter.getStillCoins());
        components.stillCoins.setText(show.replace("R$", " SK$ "));
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateInfoUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initStartServices();
    }

    private void getUpdateAvatar() {
        if (components.userLoginCast.getPhoto().isEmpty()) {
            return;
        }

        components.txtNivel.setText(components.userLoginCast.getNivel().nivel);
        updateInfoUser();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = null;

                    try {
                        url = new URL(components.userLoginCast.getPhoto());
                        components.image = BitmapFactory.decodeStream(url.openStream());
                        InputStream in = url.openStream();
                        File imageFile = CommonFunctions.createImageFile(components.contextActivity);
                        CommonFunctions.writeStreamToFile(in, imageFile);
                        components.userLoginCast.setPhoto(imageFile.getAbsolutePath());

                        runOnUiThread(() -> components.avatar.setImageBitmap(components.image));
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        })
        .start();
    }

    public void onMyOperations (View view) {
        startActivity(new Intent(components.contextActivity, MyOperations.class));
    }

    public void onOpenOperations (View view) {
        startActivity(new Intent(components.contextActivity, OpcomingOperations.class));
    }

    public void onMyData (View view) {
        startActivity(
            new Intent(components.contextActivity, MyDataActivity.class),
            ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle()
        );
    }

    public void onNextOperations (View view) {
        startActivity(new Intent(components.contextActivity, NextOperations.class));
    }

    public void onExit (View view) {
        stopService(new Intent(components.contextActivity, ServiceDeviceStatus.class));
        stopService(new Intent(components.contextActivity, ServiceApp.class));
        Intent intent = new Intent(components.contextActivity, LoginAppActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }

    public void onPicture (View view) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        components.cam_uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, components.cam_uri);
        components.startCamera.launch(cameraIntent);
    }

    public void onStore (View view) {
        startActivity(new Intent(components.contextActivity, VirtualStoreActivity.class),
                ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
    }

    private void startOnEventsResultsActivity() {
        components.startCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != RESULT_OK) {
                    return;
                }
                OnClick onClick = obj -> {
                    try {
                        String realPathFromURI = getRealPathFromURI(components.cam_uri);
                        System.out.println("#4 " + components.cam_uri.getPath());
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(components.cam_uri.toString()));
                            components.avatar.setImageBitmap(bitmap);
                            OnResponseHttp onResponseHttp = new OnResponseHttp() {
                                @Override
                                public void onResponse(Object data) {
                                    runOnUiThread(() -> {
                                        AvatarCastReceive avatarCastReceive = new Gson().fromJson((String) data, AvatarCastReceive.class);
                                        components.userLoginCast.setPhoto(avatarCastReceive.photo);
                                        getUpdateAvatar();
                                        Toast.makeText(components.contextActivity, avatarCastReceive.msn, Toast.LENGTH_LONG).show();
                                    });
                                }
                                @Override
                                public void onError(Object error) {
                                    runOnUiThread(() -> Toast.makeText(components.contextActivity, (String) error, Toast.LENGTH_LONG).show());
                                }
                            };
                            components.servicesHttp.updateAvatar(realPathFromURI, components.userLoginCast.getUuid(), onResponseHttp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {}
                };
                geoPosition(onClick);
            }
        );
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

    private void geoPosition(OnClick onClick) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Boolean isFineLocation = ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        Boolean isCoarseLocation = ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (isFineLocation && isCoarseLocation) { return; }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, location -> {
            if (location != null) {
                onClick.onClick(
                location.getLatitude() + ","
                    + location.getLongitude() + "|"
                    + location.getAccuracy() + "|"
                    + location.getProvider()
                );
            }
        }, null);
    }

    private OnResponseHttp CallBackOnResponseGlobalApi() {
        OnResponseHttp onResponseHttp = new OnResponseHttp() {
            @Override
            public void onResponse(Object data) {

            }

            @Override
            public void onError(Object error) {

            }
        };

        return onResponseHttp;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static class Components {
        public ConfigurationsUser configurationsAdapter;
        public ServicesHttp servicesHttp;
        public UserLoginCast userLoginCast;
        public ServerConfiguration conf = null;
        public Context contextActivity;
        public AppOpeationsRepository appOpeationsRepository;
        public Bitmap image;
        public ActivityResultLauncher<Intent> startCamera;
        public Uri cam_uri;
        public Button openStore;

        public Button openCameraAvatar;
        public Button openMyDate;
        public Button openMyOperations;
        public Button openOperationsNext;
        public Button openOtherOperations;
        public Button openCloseApp;

        public ImageView avatar;
        public TextView txt_name;
        public TextView stillCoins;
        public TextView txtNivel;
        public Handler handle;

        public TextView level;

        public boolean hasOperationAvailable=true;
        public int hasOperationAvailableSize=0;
        public Notifications MyNotifications;

        public Button openalert;
        public OnSetValue onSetValue;
        public boolean stop=false;
        public ProgressBar progressLevel;
    }
}
