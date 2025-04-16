package br.com.stilldistribuidora.partners.views;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.stilldistribuidora.Libs.FileManager;
import br.com.stilldistribuidora.Libs.Libs;
import br.com.stilldistribuidora.httpService.partners.PartnersServices;
import br.com.stilldistribuidora.partners.Base.ActivityBaseApp;
import br.com.stilldistribuidora.partners.Casts.CabinAudioUpload;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Models.CabinAudioUploadFile;
import br.com.stilldistribuidora.partners.Repository.Audio.CabinAudioOperationRepository;

import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;

public class SelectcaraudioActivity  extends ActivityBaseApp {
    private static Init Boot =  new Init();


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sound_audio);
        Boot.context =  this;

        initComponents();
        loaddingParameters();


        if(Boot.cabinAudioOperationRepository.
                hasCheckedAudio(Boot.OperationRepository.getOperationID().toString(),
                        Boot.OperationRepository.getParternID().toString())){
            Boot.btn_advence.callOnClick();
            return;
        }

        Boot.fileManager = new FileManager(this);
        try {
            Boot.fileManager.createFileDirectory(new File(cabinSonds()));
        }catch (Exception e){}
        hasAudioCabin();
        uploadAudio(Boot.OperationRepository.getOperationID().toString());


    }

    private void hasAudioCabin() {

        try{
            JSONObject jsonObject = new JSONObject(Boot.user.getDataJson().toString());
            String uuid = jsonObject.get("uuid").toString();
          //  Boot.CabinRepository.hasAudio(uuid);

            actionsButtons(0);


        }catch (Exception e){}

    }




    private void uploadAudio(String deliveryCode){

        try{

            Boot.ServicesHttp.searchforcabinaudios(deliveryCode, new OnResponseHttp() {
                @Override
                public void onResponse(Object data) {

                    CabinAudioUpload cabinAudioUpload= (CabinAudioUpload) data;
                    if(!Boot.cabinAudioOperationRepository.
                            hasAudio(deliveryCode,Boot.OperationRepository.getParternID().toString())){
                        if(cabinAudioUpload!=null){
                            saveInDevice(cabinAudioUpload);
                        }else{
                            Toast.makeText(Boot.context, R.string.n_o_h_audio_nesta_opera_o,Toast.LENGTH_LONG).show();
                            actionsButtons(0);
                        }

                    }else{
                        actionsButtons(1);
                    }
                 }

                @Override
                public void onError(Object error) {
                    System.out.println("");
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String cabinSonds() {
        return String.format("%s/cabin",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) );
    }



    private void saveInDevice(CabinAudioUpload cabinAudioUploads) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {

            long save = -1;
            @Override
            public void run() {

                //Background work here
                try{
                    URL url = new URL(cabinAudioUploads.data.url);
                    URLConnection conexion = url.openConnection();

                    conexion.connect();
                    // this will be useful so that you can show a tipical 0-100% progress bar
                    int lenghtOfFile = conexion.getContentLength();
                    String m_name= UUID.randomUUID().toString();;
                    // downlod the file
                    InputStream input = new BufferedInputStream(url.openStream());

                    String storageState = Environment.getExternalStorageState();
                    if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                        String newName =  m_name+".mp3";
                        File file = new File(cabinSonds(),newName);

                        OutputStream output = new FileOutputStream(file);
                        byte data[] = new byte[1024];
                        long total = 0;
                        int count;
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            output.write(data, 0, count);
                        }
                        output.flush();
                        output.close();
                        input.close();

                        cabinAudioUploads.data.url = newName;
                    }
                   save= Boot.cabinAudioOperationRepository.createAudio(cabinAudioUploads.data,Boot.OperationRepository.getParternID());




                }catch (Exception e){
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                      if(save > 0){
                          actionsButtons(1);
                      }else{
                          actionsButtons(0);
                      }
                    }
                });
            }
        });



    }


    private void loaddingParameters() {
        Boot.extras = getIntent().getExtras();
        try{
            if (Boot.extras != null) {
                String json = Boot.extras.getString(Constants.ROUTER_OPERATION_SELECIONADA);
                Boot.OperationRepository = new Gson().fromJson(json, OperationModelRepository.class);
            }
        }catch (Exception e){
            Boot.OperationRepository=null;
        }


    }


    private void initComponents(){

        Boot.cabinAudioOperationRepository =  new CabinAudioOperationRepository(Boot.context);
        Boot.SettingRepository = new ConfigDataAccess(Boot.context);
        Boot.ServiceConfig =     new ServerConfiguration(Boot.context);
        Boot.ServicesHttp =  new ServicesHttp(Boot.context, new ServerConfiguration(Boot.context),ResponseGeneral());
        Boot.btn_upload_audio = (Button)findViewById(R.id.btn_upload_audio);
        Boot.btn_realese_audio = (Button)findViewById(R.id.btn_realese_audio);
        Boot.btn_play_audio = (Button)findViewById(R.id.btn_play_audio);
        Boot.btn_advence = (Button)findViewById(R.id.btn_advence);
        Boot.btn_upload_audio = (Button)findViewById(R.id.btn_upload_audio);
        Boot.audio_cheched = (CheckBox)findViewById(R.id.audio_cheched);

        /*loads*/
        Boot.user=(Config) Boot.SettingRepository.
                getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));


        /*Events*/
        Boot.btn_upload_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAudio(Boot.OperationRepository.getOperationID().toString());
            }
        });




        Boot.audio_cheched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if(checked){
                    if(!Boot.mediaPlayer_checked){
                        Boot.audio_cheched.setChecked(false);
                        Toast.makeText(Boot.context, R.string.necess_rio_conferir_o_udio,Toast.LENGTH_LONG).show();
                    }else{
                        if(Boot.mediaPlayer!=null){
                            Boot.mediaPlayer.stop();
                        }
                        actionsButtons(2);
                    }
                }else{
                    if(!Boot.cabinAudioOperationRepository.
                            hasAudio(Boot.OperationRepository.getOperationID().toString(),Boot.OperationRepository.getParternID().toString())){
                        actionsButtons(0);
                    }else{

                        actionsButtons(1);
                    }
                }
            }
        });



        Boot.btn_advence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boot.cabinAudioOperationRepository.
                        updateCheckAudio(Boot.OperationRepository.getOperationID().toString(),
                                Boot.OperationRepository.getParternID().toString());

                Intent intent;
                intent = new Intent(Boot.context, CarRouteGuidanceActivity.class);
                intent.putExtras(Boot.extras);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });




        Boot.btn_play_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CabinAudioUploadFile cabinAudioUploadFile = Boot.cabinAudioOperationRepository.
                        audioBy(Boot.OperationRepository.getOperationID().toString(),
                                Boot.OperationRepository.getParternID().toString());


                try {
                    if(Boot.mediaPlayer==null){
                        Boot.mediaPlayer = new MediaPlayer();
                        Boot.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        Boot.mediaPlayer.setDataSource(cabinSonds()+"/"+cabinAudioUploadFile.url);
                        Boot.mediaPlayer.prepare();
                    }
                    if( !Boot.mediaPlayer_start){
                        Boot.btn_play_audio.setText("Tocando..");
                        Boot.mediaPlayer_checked=true;
                        Boot.mediaPlayer_start=true;
                        Boot.mediaPlayer.start();
                    }else{
                        Boot.btn_play_audio.setText("Conferir Audio.");
                        Boot.mediaPlayer.stop();
                        Boot.mediaPlayer_start=false;
                        Boot.mediaPlayer=null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("");


            }
        });





    }


    @Override
    protected void onResume() {
        super.onResume();

        if(Boot.cabinAudioOperationRepository.
                hasCheckedAudio(Boot.OperationRepository.getOperationID().toString(),
                        Boot.OperationRepository.getParternID().toString())){
            Boot.btn_advence.callOnClick();
            return;
        }


    }

    private OnResponseHttp ResponseGeneral() {
        return new OnResponseHttp() {
            @Override
            public void onResponse(Object data) {

            }

            @Override
            public void onError(Object error) {

            }
        };
    }


    private void actionsButtons(int actionType){

        if(actionType==0){
            Boot.btn_advence.setVisibility(View.GONE);
            Boot.btn_play_audio.setVisibility(View.GONE);
            Boot.btn_realese_audio.setVisibility(View.GONE);
            Boot.btn_upload_audio.setVisibility(View.VISIBLE);
            Boot.audio_cheched.setVisibility(View.GONE);
        }
        if(actionType==1){
            Boot.btn_advence.setVisibility(View.GONE);
            Boot.btn_play_audio.setVisibility(View.VISIBLE);
            Boot.btn_realese_audio.setVisibility(View.VISIBLE);
            Boot.btn_upload_audio.setVisibility(View.GONE);
            Boot.audio_cheched.setVisibility(View.VISIBLE);
        }

        if(actionType==2){
            Boot.btn_advence.setVisibility(View.VISIBLE);
            Boot.btn_play_audio.setVisibility(View.GONE);
            Boot.btn_realese_audio.setVisibility(View.GONE);
            Boot.btn_upload_audio.setVisibility(View.GONE);
            Boot.audio_cheched.setVisibility(View.VISIBLE);
        }





    }

















    private  static class Init{
        public SelectcaraudioActivity context;


        public Button btn_upload_audio;
        public Button btn_realese_audio;
        public Button btn_play_audio;
        public Button btn_advence;
        public CheckBox audio_cheched;

        public ConfigDataAccess SettingRepository;
        public Config user;
        public OperationModelRepository OperationRepository;
        public br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp ServicesHttp;
        public PartnersServices PartenersServices;
        public ServerConfiguration ServiceConfig;

        public   CabinAudioOperationRepository cabinAudioOperationRepository;
        public MediaPlayer mediaPlayer;
        public boolean mediaPlayer_start=false;
        public boolean mediaPlayer_checked;
        public Bundle extras;
        public FileManager fileManager;

    }
}
