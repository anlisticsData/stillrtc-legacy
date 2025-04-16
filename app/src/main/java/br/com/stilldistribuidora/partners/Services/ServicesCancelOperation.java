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

public class ServicesCancelOperation {
    private final Context contextServices;
    private final AppOpeationsRepository repository;
    private final ServicesHttp servicesHttp;
    private final UserLoginCast userLoginCast;

    public ServicesCancelOperation(Context contextServices, AppOpeationsRepository repository,
                                   UserLoginCast userLoginCast, ServicesHttp servicesHttp) {

        this.contextServices = contextServices;
        this.repository = repository;
        this.servicesHttp = servicesHttp;
        this.userLoginCast = userLoginCast;
        this.run();

    }

    private void run() {
        System.out.println("#13 ");
        List<OperationModelRepository> allPartnerId = this.repository.getAllPartnerId(this.userLoginCast.getUuid(), "3");
        for (OperationModelRepository Op : allPartnerId) {
            OnResponseHttp responseHttp = new OnResponseHttp() {
                @Override
                public void onResponse(Object data) {
                    try {
                        ResponseApi responseApi = new Gson().fromJson((String) data, ResponseApi.class);
                        if(responseApi.status.equals("200")){
                            repository.updateStateOperationsNuvem(String.valueOf(Op.getOperationID()),
                                            String.valueOf(Op.getParternID()),3,1);

                        }
                    } catch (Exception e) {
                    }
                }
                @Override
                public void onError(Object error) {
                }
            };
            this.servicesHttp.cancelOperation(this.userLoginCast, Op, responseHttp);
        }


    }


}
