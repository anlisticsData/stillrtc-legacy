package br.com.stilldistribuidora.partners.Services;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

import br.com.stilldistribuidora.partners.Casts.ResponseApi;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;

public class ServicesAcceptedOperation {
    private final Context contextServices;
    private final AppOpeationsRepository repository;
    private final ServicesHttp servicesHttp;
    private final UserLoginCast userLoginCast;

    public ServicesAcceptedOperation(Context contextServices, AppOpeationsRepository repository,
                                     UserLoginCast userLoginCast, ServicesHttp servicesHttp) {

        this.contextServices = contextServices;
        this.repository = repository;
        this.servicesHttp = servicesHttp;
        this.userLoginCast = userLoginCast;
        this.run();
        



    }

    private void run() {

        List<OperationModelRepository> allPartnerId = this.repository.getAllAcceptedNuvem();
        for (OperationModelRepository Op : allPartnerId) {
            OnResponseHttp responseHttp = new OnResponseHttp() {
                @Override
                public void onResponse(Object data) {
                    try {
                        ResponseApi responseApi = new Gson().fromJson((String) data, ResponseApi.class);
                        if(responseApi.status.equals("200")){
                            repository.uploadAcceptionoperationNuvem(
                                    String.valueOf(Op.getId()),
                                    String.valueOf(Op.getOperationID()),
                                    String.valueOf(Op.getParternID()),
                                    String.valueOf(Op.getRouterID())
                            );
                        }
                    } catch (Exception e) {
                    }
                }
                @Override
                public void onError(Object error) {

                    System.out.println("dsdas");
                }
            };
            this.servicesHttp.registerAccepted(this.userLoginCast, Op, responseHttp);
        }
    }
}
