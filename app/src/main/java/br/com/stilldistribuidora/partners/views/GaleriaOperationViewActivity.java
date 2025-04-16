package br.com.stilldistribuidora.partners.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.interfac.OnActionParentActivity;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;
import br.com.stilldistribuidora.stillrtc.services.DownloadImageTask;

public class GaleriaOperationViewActivity extends AppCompatActivity {
    private Components componentsStatics = new Components();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_operation_view);
        InitPermisionGeral();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Intent i = getIntent();
        List<ViewImagens> imagens=new ArrayList<>();
        OperationModelRepository operationData=(OperationModelRepository) i.getSerializableExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_SELECT_IMAGE);
        componentsStatics.self = this;
        componentsStatics.recycler_view_layour_recycler=(RecyclerView)findViewById(R.id.recycler_view_layour_recycler);
        componentsStatics.layoutManager = new LinearLayoutManager(componentsStatics.self);
        componentsStatics.recycler_view_layour_recycler.setLayoutManager(componentsStatics.layoutManager);
        componentsStatics.photoModel =new PhotosPartnersModel(componentsStatics.self);
        componentsStatics.message=(LinearLayout)findViewById(R.id.layout_mensage);
        InitEvents();
        List<PhotoEntity> photoEntities = (List<PhotoEntity>) componentsStatics.photoModel.
                photosAllByUser(new String[]{String.valueOf(operationData.getParternID()), String.valueOf(operationData.getOperationID())});

        for(PhotoEntity  photo:photoEntities){
            boolean ativo = (photo.getPath_serve_url()==null || photo.getPath_serve_url().isEmpty() ) ? false : true;
            imagens.add(
                    new ViewImagens(photo.getUri(),photo.getPath_serve_url(),ativo ,photo.getCreated_at()));
        }
        if(imagens.size()>0){
            componentsStatics.recycler_view_layour_recycler.setVisibility(View.VISIBLE);
            componentsStatics.message.setVisibility(View.GONE);
            componentsStatics.recycler_view_layour_recycler.setVisibility(View.VISIBLE);
            componentsStatics.
                    recycler_view_layour_recycler
                    .setAdapter(new LineToOperatpionAcepted(imagens,
                            componentsStatics.onClickGetAllDb,componentsStatics.onActionParentActivity));
        }else {
            componentsStatics.recycler_view_layour_recycler.setVisibility(View.GONE);
            componentsStatics.message.setVisibility(View.VISIBLE);
        }







    }

    private void InitEvents() {

        componentsStatics.onClickGetAllDb=new OnClick() {
            @Override
            public void onClick(Object obj) {

            }
        };
        componentsStatics.onActionParentActivity=new OnActionParentActivity() {
            @Override
            public void onStartActivity(String type, Object objetct) {

            }
        };




        //Buttons





    }


    public  void  close(){
        Intent intent =new Intent();
        setResult(Constants.FINISH_OPERATION_VIEW_PHOTO_CLOSE,intent);
        finish();
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
                close();
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


    public static class OperationsRowsAcepted extends RecyclerView.ViewHolder {




        public OperationsRowsAcepted(View itemView) {
            super(itemView);

        }
    }



    public static class LineToOperatpionAcepted extends RecyclerView.Adapter<OperationsRowsAcepted> {

        private final OnActionParentActivity onClickEventsParents;
        private List<ViewImagens> typesRoutes = new ArrayList<>();
        private final OnClick onClick;
        private ImageView image_file;
        private TextView create_file;
        private CheckBox send_file;

        public LineToOperatpionAcepted(List<ViewImagens> typesRoutes, OnClick onClick, OnActionParentActivity onClickEventsParents) {
            this.typesRoutes = typesRoutes;
            this.onClick = onClick;
            this.onClickEventsParents=onClickEventsParents;
        }
        @Override
        public OperationsRowsAcepted onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OperationsRowsAcepted(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rows_operation_photo, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull OperationsRowsAcepted holder, int position) {
            ViewImagens viewImagens = typesRoutes.get(position);
            image_file=(ImageView)holder.itemView.findViewById(R.id.image_file);
            create_file=(TextView) holder.itemView.findViewById(R.id.create_file);
            create_file.setText(viewImagens.createAt);
            send_file=(CheckBox) holder.itemView.findViewById(R.id.send_file);
            if(viewImagens.loadsServerFile){
                send_file.setChecked(true);
            }

            send_file.setClickable(false);
            try{
                File file = new File(viewImagens.uri);
                if(file.exists()){
                    image_file.setImageURI(Uri.parse(file.toString()));
                }else{
                    if(!viewImagens.url.trim().isEmpty()){
                        new DownloadImageTask(image_file).execute(viewImagens.url);
                    }
                }

            }catch (Exception e){
                System.out.println("#4 "+e.getMessage());
            }

        }



        @Override
        public int getItemCount() {
            return typesRoutes != null ? typesRoutes.size() : 0;
        }

    }






    public static class ViewImagens{
        public String uri;
        public String url;
        public boolean loadsServerFile=false;
        String createAt;

        public ViewImagens(String uri, String url, boolean loadServer,String createAt) {
            this.uri=uri;
            this.url=url;
            this.loadsServerFile=loadServer;
            this.createAt=createAt;

        }


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


    public static class ApiConfig {
        private AppCompatActivityBase Api;

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
        public Button openViewPicturesOperations;
        public RecyclerView recycler_view_layour_recycler;
        public LinearLayoutManager layoutManager;
        public OnClick onClickGetAllDb;
        public OnActionParentActivity onActionParentActivity;
        public PhotosPartnersModel photoModel;
        public LinearLayout message;
        private String uuid;
        private OnClick onClick;

    }

}