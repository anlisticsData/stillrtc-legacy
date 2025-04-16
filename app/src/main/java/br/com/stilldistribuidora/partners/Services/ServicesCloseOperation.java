package br.com.stilldistribuidora.partners.Services;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

import br.com.stilldistribuidora.partners.Base.OnResponse;
import br.com.stilldistribuidora.partners.Casts.ResponseApi;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Commom.Enum;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

public class ServicesCloseOperation {
    private final Context contextServices;
    private final AppOpeationsRepository repository;
    private final ServicesHttp servicesHttp;
    private final UserLoginCast userLoginCast;
    private final OnResponseHttp onresponseHttp;
    private boolean closeRun =false;


    public ServicesCloseOperation(Context contextServices, AppOpeationsRepository repository,
                                  UserLoginCast userLoginCast, ServicesHttp servicesHttp, OnResponseHttp onResponseHttp) {

        this.contextServices = contextServices;
        this.repository = repository;
        this.servicesHttp = servicesHttp;
        this.userLoginCast = userLoginCast;
        this.onresponseHttp = onResponseHttp;
        System.out.println("#55 Close Services");
        this.run();
    }


    private void run() {
        final List<OperationModelRepository> finishedOperations = this.repository.getAll(String.valueOf(Enum.STATUS_CLOSE_SAVE_DEVICE));
        if (finishedOperations.size() > 0) {
            if (Utils.isNetworkAvailable(contextServices.getApplicationContext()) && Utils.isOnline()) {

                StringBuilder operationBuffer = new StringBuilder();
                for (OperationModelRepository operation : finishedOperations) {
                    operationBuffer.append(String.format("%S:%S",operation.getOperationID(),operation.getParternID()));
                    operationBuffer.append("|");
                }
                this.servicesHttp.closeOperationPartners(userLoginCast,operationBuffer.toString(), new OnResponseHttp() {
                    @Override
                    public void onResponse(Object data) {
                        System.out.println("#55 Close  ok");
                        onresponseHttp.onResponse(true);


                    }

                    @Override
                    public void onError(Object error) {
                        System.out.println("#55 Close  error");
                        onresponseHttp.onResponse(false);

                    }
                });

            }
        }




    }


}

