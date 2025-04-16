package br.com.stilldistribuidora.partners.Services;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateModel;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentBusiness;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentHelper;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentModel;
import br.com.stilldistribuidora.partners.Casts.Market;
import br.com.stilldistribuidora.partners.Casts.ResponseBaseCastData;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;

public class ServicesPathTraveled {
    private static ContextVarGlobal varsGlobal = new ContextVarGlobal();
    private final Context contextServices;
    private final RouterMovimentBusiness repository;
    private final ServicesHttp servicesHttp;
    private final UserLoginCast userLoginCast;


    public static class ContextVarGlobal{
        public Boolean runThread = false;
        public int  nextOperation=0;
        public int sizeOperations=0;
    }


    public ServicesPathTraveled(Context contextServices, RouterMovimentBusiness routerMovimentBusiness,
                                UserLoginCast userLoginCast, ServicesHttp servicesHttp) {

        this.contextServices = contextServices;
        this.repository  = routerMovimentBusiness;
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
        if(!varsGlobal.runThread) {
            varsGlobal.runThread=true;
            List<RouterMovimentModel> routerExecuteds = repository.getAllLimit("0",String.valueOf(10));
            try {

                OnResponseHttp onResponseHttp = new OnResponseHttp() {
                    @Override
                    public void onResponse(Object data) {
                        if (data != null) {
                            try {
                                Market marketSaved = new Gson().fromJson(String.valueOf(data), Market.class);
                                if (marketSaved != null && marketSaved.markets != null) {
                                    for (Market.MarketsPoints m : marketSaved.markets) {
                                        RouterMovimentModel moviment = new RouterMovimentModel();
                                        moviment.setId(String.valueOf(m.id));
                                        repository.updateStateSyncOk(moviment);
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        varsGlobal.runThread = false;

                    }

                    @Override
                    public void onError(Object error) {
                        varsGlobal.runThread = false;
                    }
                };
                servicesHttp.updateOperationRouterPointsPartners(userLoginCast, routerExecuteds, onResponseHttp);
            } catch (Exception e) {
                varsGlobal.runThread = false;
            }
        }
    }
}
