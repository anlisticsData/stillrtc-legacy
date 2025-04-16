package br.com.stilldistribuidora.partners.Services;

import android.content.Context;

import java.util.List;

import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateModel;
import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateReaderDbHelper;
import br.com.stilldistribuidora.partners.Casts.ResponseBaseCastData;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;

public class ServicesPointsOperation {
    private static  ContextVarGlobal varsGlobal = new ContextVarGlobal();
    private final Context contextServices;
    private final DeliveryStateReaderDbHelper repository;
    private final ServicesHttp servicesHttp;
    private final UserLoginCast userLoginCast;
    private final ConfigDataAccess configData;
    private final Config userDevice;

    public static class ContextVarGlobal{
        public Boolean runThread = false;
        public int  nextOperation=0;
        public int sizeOperations=0;
    }

    public ServicesPointsOperation(Context contextServices, DeliveryStateReaderDbHelper deviceStateRepository,
                                   UserLoginCast userLoginCast, ServicesHttp servicesHttp) {

        this.contextServices = contextServices;
        this.repository  = deviceStateRepository;
        this.servicesHttp  = servicesHttp;
        this.userLoginCast  = userLoginCast;
        this.configData = new ConfigDataAccess(contextServices);
        this.userDevice = (Config) this.configData.getById(String.format("%s='%s'", "descricao", Constants.API_USER_DEVICE));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                        ProcessRun();

                }catch (Exception e){
                    System.out.println("#12 "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void ProcessRun() {
        this.run();
    }
    private void run() {

        try {
            System.out.println("#12 -Envindo pontos Executando  ");

            // if(!varsGlobal.runThread){
            varsGlobal.runThread = true;
            List<DeliveryStateModel> deliverys = repository.getNuvemLimit(30);
            varsGlobal.sizeOperations = deliverys.size();

            System.out.println("#12 -Envindo pontos "+deliverys.size());
            for (DeliveryStateModel delivery : deliverys) {
                System.out.println("#12 -Envindo pontos  (" + delivery.getDeliveryid() + ") ");
                try {

                    OnResponseHttp onResponseHttp = new OnResponseHttp() {
                        @Override
                        public void onResponse(Object data) {
                            ResponseBaseCastData responseBaseCastData = (ResponseBaseCastData) data;
                            if (responseBaseCastData.status == 201 || responseBaseCastData.status == 203) {
                                // if (responseBaseCastData.status == 201 || responseBaseCastData.status == 203) {
                                repository.update((DeliveryStateModel) responseBaseCastData.data);
                                varsGlobal.nextOperation++;
                                if (varsGlobal.sizeOperations == varsGlobal.nextOperation) {
                                    varsGlobal.runThread = false;
                                }
                                System.out.println("#12 -Envindo pontos OK  (" + delivery.getDeliveryid() + ") ");
                            } else {
                                System.out.println("#12 -Nao Criado pontos   (" + delivery.getDeliveryid() + ") ");
                            }
                        }

                        @Override
                        public void onError(Object error) {
                            System.out.println("#12 -Nao Criado pontos");
                            System.out.println("#12 -Erros  (" + error + ") ");
                        }
                    };

                    if (delivery.getDeviceid() == null) {
                        delivery.setDeviceid(this.userDevice.getDataJson());
                    }

                    if (!delivery.getDeviceid().isEmpty()) {
                        System.out.println("#12 Http -Envindo pontos 1");
                        servicesHttp.updateOperationRouterPoints(userLoginCast, delivery, onResponseHttp);
                        System.out.println("#12 Http -Envindo pontos 2");
                    }

                } catch (Exception e) {
                    varsGlobal.runThread = false;
                    System.out.println("#12 -Erros  (" + e.getMessage() + ") ");
                }
            }
            // }
        }catch (Exception e){
            e.printStackTrace();
        }
    }}
