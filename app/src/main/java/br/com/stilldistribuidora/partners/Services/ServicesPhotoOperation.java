package br.com.stilldistribuidora.partners.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import br.com.stilldistribuidora.partners.Casts.ResponseApi;
import br.com.stilldistribuidora.partners.Casts.ResponseApiGenerics;
import br.com.stilldistribuidora.partners.Casts.ResponsePhoto;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Models.Pictures;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ServicesPhotoOperation {
    private final Context contextServices;
    private final PhotosPartnersModel repository;
    private final ServicesHttp servicesHttp;
    private final UserLoginCast userLoginCast;
    private final ConfigDataAccess configData;
    private  Config photoData;
    private boolean is_upload_ativo_photo=false;
    private boolean is_upload_ativo=false;
    private  Config userDevice;



    public ServicesPhotoOperation(Context contextServices, PhotosPartnersModel repository,
                                   UserLoginCast userLoginCast, ServicesHttp servicesHttp) {

        this.contextServices = contextServices;
        this.repository = repository;
        this.servicesHttp = servicesHttp;
        this.userLoginCast = userLoginCast;
        configData = new ConfigDataAccess(contextServices);
        this.userDevice = (Config) this.configData.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE));
        this.photoData = (Config) this.configData.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE_SYNC_PHOTO));



        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    ProcessRun();


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void ProcessRun() {
        this.run();
    }


    List<PhotoEntity> getIsNotSyncPhoto(int Limit){
        List<PhotoEntity> photos = null;
        photos=repository.photoOfTheOperationsThatDidNotGoUpLimit(String.valueOf(Limit));
        return photos;
    }



    boolean performingProcess(){
        photoData = (Config) this.configData.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE_SYNC_PHOTO));
        return (this.photoData.getDataJson().trim().isEmpty() || this.photoData.getDataJson().trim().equals("0")) ? false : true;
    }

    long processOnExecution(){
        this.photoData.setDataJson("1");
        return configData.update(photoData,String.valueOf(photoData.getId()));
    }
    long processFinished(){
        this.photoData.setDataJson("0");
        return configData.update(photoData,String.valueOf(photoData.getId()));
    }


    private void run() {
        final List<PhotoEntity> isNotSyncPhotos = this.getIsNotSyncPhoto(1);
        boolean isSync= this.performingProcess();
        if(isNotSyncPhotos.size()==0 && isSync){
            processFinished();
        }


        for(PhotoEntity photo : isNotSyncPhotos){
            try{
                if (Utils.isNetworkAvailable(contextServices) &&  !isSync) {
                    OnResponseHttp responseHttp = new OnResponseHttp() {
                        @Override
                        public void onResponse(Object data) {
                            try {
                                ResponseApiGenerics responseApiGenerics =(ResponseApiGenerics)data;
                                if(responseApiGenerics.status.equals("200")){
                                    List<Pictures> photos = (List<Pictures>) responseApiGenerics.data;
                                    for (Pictures photoReceive : photos){
                                        PhotoEntity p = new PhotoEntity();
                                        p.setId(Integer.valueOf(photoReceive.id));
                                        p.setSync("1");
                                        p.setPath_serve_url(photoReceive.uri);
                                        repository.photoSyncedOnServer(p);
                                   }
                                    processFinished();

                                }
                            } catch (Exception e) {
                                processFinished();
                            }
                        }
                        @Override
                        public void onError(Object error) {
                            processFinished();
                        }
                    };

                    if(photo.getDeviceId()==null){
                        photo.setDeviceId(this.userDevice.getDataJson());
                    }
                    processOnExecution();
                    sendPhotoToServer(photo,responseHttp);
                    }

            }catch (Exception e){
                processFinished();
            }
        }
    }

    private void sendPhotoToServer(PhotoEntity photo,OnResponseHttp onResponseHttp) {

        UploadFile uploadFile = new UploadFile(onResponseHttp);
        uploadFile.execute(photo);
    }


    public class UploadFile extends AsyncTask<Object, String, String> {
        private  OnResponseHttp onResponseHttp;

        public UploadFile(OnResponseHttp onResponseHttp) {
            this.onResponseHttp=onResponseHttp;
        }

        private Bitmap resizeImage(Context context, Bitmap bmpOriginal,
                                   float newWidth, float newWeight) {
            Bitmap novoBmp = null;
            int w = bmpOriginal.getWidth();
            int h = bmpOriginal.getHeight();
            float densityFactor = context.getResources().getDisplayMetrics().density;
            float novoW = newWidth * densityFactor;
            float novoH = newWeight * densityFactor;
            //Calcula escala em percentagem do tamanho original para o novo tamanho
            float scalaW = novoW / w;
            float scalaH = novoH / h;
            // Criando uma matrix para manipulação da imagem BitMap
            Matrix matrix = new Matrix();
            // Definindo a proporção da escala para o matrix
            matrix.postScale(scalaW, scalaH);
            //criando o novo BitMap com o novo tamanho
            novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h, matrix, true);
            return novoBmp;
        }
        @Override
        protected String doInBackground(Object... objects) {
            try{

                if (Utils.isNetworkAvailable(contextServices) && Utils.isOnline()) {

                    PhotoEntity photo = (PhotoEntity) objects[0];
                    // photo.setDeviceId("00000");
                    PhotosPartnersModel partnersModel = new PhotosPartnersModel(contextServices);
                    File file = new File(Uri.parse(photo.getUri()).getEncodedPath());
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(photo.getUri());
                        //create a file to write bitmap data
                        File f = new File(contextServices.getCacheDir(), file.getName());
                        f.createNewFile();
                        //Convert bitmap to byte array
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 25 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        //write the bytes in file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(f);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();

                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
                            MultipartBody.Part body =
                                    MultipartBody.Part.createFormData("file", f.getName(), reqFile);
                            byte[] data = new byte[0];
                            data = photo.getCreated_at().getBytes("UTF-8");
                            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                            servicesHttp.registerPicturePartners(userLoginCast, photo, data, body, base64, onResponseHttp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }



























}
