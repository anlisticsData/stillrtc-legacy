package br.com.stilldistribuidora.partners.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateModel;
import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateReaderDbHelper;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentModel;
import br.com.stilldistribuidora.partners.Casts.ResponseApiGenerics;
import br.com.stilldistribuidora.partners.Casts.ResponseBaseCastData;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Models.Pictures;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ServicesAutoCloseOperations {
    private static ServicesPointsOperation.ContextVarGlobal varsGlobal = new ServicesPointsOperation.ContextVarGlobal();
    private final Context contextServices;
    private final DeliveryStateReaderDbHelper repository;
    private final ServicesHttp servicesHttp;
    private final UserLoginCast userLoginCast;

    public ServicesAutoCloseOperations(Context contextServices, DeliveryStateReaderDbHelper deviceStateRepository,
                                       UserLoginCast userLoginCast, ServicesHttp servicesHttp) {

        this.contextServices = contextServices;
        this.repository  = deviceStateRepository;
        this.servicesHttp  = servicesHttp;
        this.userLoginCast  = userLoginCast;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ProcessRun();

                }catch (Exception e){e.printStackTrace();}
            }
        }).start();

    }

    private void ProcessRun() {
        this.run();
    }
    private void run() {
      //  List<RouterMovimentModel> routerExecuteds = repository.getAll("0");



    }}
