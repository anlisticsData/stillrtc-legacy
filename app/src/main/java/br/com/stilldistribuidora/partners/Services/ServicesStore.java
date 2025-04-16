package br.com.stilldistribuidora.partners.Services;

import android.content.Context;

import java.util.List;

import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;

public class ServicesStore {
    private final Context context;
    public AppOpeationsRepository appOpeationsRepository;
    public ServicesStore(Context context){
        this.context=context;
    }
    public void operationsNearAndFarBy(UserLoginCast userLoginCast, String dataCurrent,String start, OnResponseHttp onResponseHttp) {
        this.appOpeationsRepository = new AppOpeationsRepository(this.context);
        List<OperationModelRepository> operations = this.appOpeationsRepository.getAllPartnerId(userLoginCast.getUuid(), dataCurrent, start);
        onResponseHttp.onResponse(operations);
    }
}
