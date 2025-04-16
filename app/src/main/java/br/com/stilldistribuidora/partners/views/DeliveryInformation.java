package br.com.stilldistribuidora.partners.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateModel;
import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateReaderDbHelper;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentBusiness;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentHelper;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentModel;
import br.com.stilldistribuidora.partners.Services.ServicesApiStill;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.stillrtc.R;

public class DeliveryInformation  extends AppCompatActivity {


    private boolean synchronizing=false;

    interface ResponseList{
        void result(List<RouterMovimentModel> moviments,List<DeliveryStateModel>  deliverys,List<PhotoEntity> photoEntityList);
    }

    private RouterMovimentBusiness movimentoData;
    private PhotosPartnersModel photoData;
    private DeliveryStateReaderDbHelper operationData;
    private TextView movimentQt,movimentPointsQt,movimentPhotoQt;
    private Button synchronizeOperation;
    private String operationCode;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_information_view);
        synchronizeOperation = (Button)findViewById(R.id.synchronizeOperation);
        movimentQt= (TextView)findViewById(R.id.movimentQt);
        movimentPointsQt= (TextView)findViewById(R.id.movimentPointsQt);
        movimentPhotoQt= (TextView)findViewById(R.id.movimentPhotoQt);
        movimentoData = new RouterMovimentBusiness(new RouterMovimentHelper(this));
        photoData = new PhotosPartnersModel(this);
        operationData = new DeliveryStateReaderDbHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            operationCode = extras.getString("operationCode");
            setDisplayInformation(operationCode);
        }

        synchronizeOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!synchronizing){
                    synchronizing=true;
                    setButtonServer(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            server();
                        }
                    }).start();
                }
            }
        });

    }

    private void server() {
        getInformationOperation(operationCode, new ResponseList() {
            @Override
            public void result(List<RouterMovimentModel> moviments, List<DeliveryStateModel> deliverys, List<PhotoEntity> photoEntityList) {
                for(RouterMovimentModel moviment: moviments ){
                    movimentoData.updateStateSyncNot(moviment);
                }
                for(DeliveryStateModel  operation : deliverys){
                    operationData.updateStateSyncNot(operation);
                }
                for(PhotoEntity photo : photoEntityList){
                    photoData.updatePhotoSyncNot(photo);
                }
                synchronizing=false;
                setButtonServer(synchronizing);
                finish();
            }
        });


    }

    private void setButtonServer(boolean synchronizing) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               if(synchronizing){
                   synchronizeOperation.setEnabled(synchronizing);
                   synchronizeOperation.setText("Processando...");
               }else{
                   synchronizeOperation.setEnabled(synchronizing);
                   synchronizeOperation.setText("Sincronizar Operação");
               }
           }
       });
    }

    private void initStartServices() {
        startService(new Intent(DeliveryInformation.this, ServicesApiStill.class));
    }
    private void setDisplayInformation(String delivery) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getInformationOperation(delivery, new ResponseList() {
                    @Override
                    public void result(List<RouterMovimentModel> moviments, List<DeliveryStateModel> deliverys, List<PhotoEntity> photoEntityList) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                movimentQt.setText(String.valueOf(moviments.size()));
                                movimentPointsQt.setText(String.valueOf(deliverys.size()));
                                movimentPhotoQt.setText(String.valueOf(photoEntityList.size()));
                             }
                        });

                    }
                });
            }
        }).start();

    }

    private void getInformationOperation(String delivery, ResponseList responseList) {
        List<RouterMovimentModel> moviments = movimentoData.getAllSynchronizeByOperation(String.valueOf(delivery));
        List<DeliveryStateModel>  deliverys = operationData.getAllDeliveryBy(String.valueOf(delivery));
        List<PhotoEntity> photos = photoData.photosAllByOperation(String.valueOf(delivery));
        responseList.result(moviments,deliverys,photos);
    }


}
