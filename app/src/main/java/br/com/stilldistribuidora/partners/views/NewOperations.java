package br.com.stilldistribuidora.partners.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.common.LoadProcess;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.business.OperationPartnerBusines;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.AvailableOperation;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;
import br.com.stilldistribuidora.stillrtc.ui.activities.InitAppActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOperations extends AppCompatActivity {
    
    
    private static Components ContextFrame=new Components();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent parameters = getIntent();
        ContextFrame.self=this;
        requestPermissions();
        ContextFrame.typeList=parameters.getStringExtra("type_list");
        ContextFrame.apiConfig = new ApiConfig(ContextFrame.self);
        ContextFrame.operationPartnerModel = new OperationPartnerBusines(ContextFrame.self);


        setContentView(R.layout.activity_available_operation);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (ContextFrame.apiConfig.Api.getEndPointAccess() == null ||
                ContextFrame.apiConfig.Api.getEndPointAccess().getDataJson() == null ||
                ContextFrame.apiConfig.Api.getEndPointAccess().getDataJson().isEmpty()) {
            Intent intent = new Intent(ContextFrame.self, InitAppActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        ContextFrame.configModel=new ConfigDataAccess(ContextFrame.self);
        ContextFrame.layout_mensage=(LinearLayout)findViewById(R.id.layout_mensage);
        ContextFrame.layout_mensage_info_text=(TextView) findViewById(R.id.layout_mensage_info_text);
       // ContextFrame.input_address_start=(TextView)findViewById(R.id.input_address_start);
        ContextFrame.recyclerView= (RecyclerView) findViewById(R.id.recycler_view_layour_recycler);
        ContextFrame.userLogged=(Config)ContextFrame.configModel.getById(String.format("%s='%s'","descricao", Constants.API_USER_LOGGED));

        initEvents();
        ContextFrame.loadProcess=new LoadProcess(ContextFrame.self,"Carregando Informaçãos 22 ...",false);
        availableOperations();
        ContextFrame.loadProcess.loadActivityClose();

    }

    private void initEvents() {
        ContextFrame.layout_mensage.setVisibility(View.GONE);
        LinearLayoutManager horizontalLayoutManagaer =
                new LinearLayoutManager(ContextFrame.self, LinearLayoutManager.VERTICAL, false);
                ContextFrame.recyclerView.setLayoutManager(horizontalLayoutManagaer);



        ContextFrame.onClick=new OnClick() {
            @Override
            public void onClick(Object obj) {

            }
        };


    }



    public static class ApiConfig {
        private final AppCompatActivityBase Api;
        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_available, menu);
        return true;
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.system_last:
                 this.onBackPressed();
                return true;
            case R.id.system_close:
                Intent i = new Intent(getApplicationContext(), LoginAppActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /*Permisoes Gerais*/
    public void requestPermissions() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INSTALL_SHORTCUT) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.LOCATION_HARDWARE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
                                android.Manifest.permission.ACCESS_NETWORK_STATE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_PHONE_STATE,
                                android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.INSTALL_SHORTCUT,
                                android.Manifest.permission.LOCATION_HARDWARE,
                                android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                                android.Manifest.permission.WRITE_SETTINGS,
                                android.Manifest.permission.WAKE_LOCK,
                                android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS




                        }, 10000);
            }

        }


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*Permisoes Gerais*/


    public void availableOperations(){
        try{
            //2022
            String partnerId = (new JSONObject(ContextFrame.userLogged.getDataJson())).getString("uuid");
            JSONObject geo = (new JSONObject(ContextFrame.userLogged.getDataJson()))
                    .getJSONObject("address").getJSONObject("geo");
            ApiEndpointInterface apiService =ApiClient.getClient(ContextFrame.apiConfig.Api.getEndPointAccess().
                    getDataJson().trim()).create(ApiEndpointInterface.class);

            Call<ResponseBody> availiableOperations = apiService.
                     operationsNearAndFar(ContextFrame.apiConfig.Api.getEndPointAccessToken()
                    .getDataJson(), geo.getString("lat")+","+geo.getString("lng"),"1",partnerId);
                    outResponseOperation(availiableOperations);


        }catch (Exception e){
            System.out.println(e.getMessage());

        }



    }

    private void outResponseOperation(Call<ResponseBody> availiableOperations) {
        availiableOperations.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                List<AvailableOperation> availableOperations=new ArrayList<>();
                try{
                    if(response.isSuccessful()){
                        if(ContextFrame.typeList.equals("next")){
                             availableOperations = loadMyOperations(response.body().string(),true);
                        }else{
                             availableOperations = loadMyOperations(response.body().string(),false);
                        }

                        if(availableOperations.size() !=0) {


                            OnClick onClickReceive=new OnClick() {
                                @Override
                                public void onClick(Object obj) {

                                    AvailableOperation availableOperation =(AvailableOperation) obj;
                                    try{
                                        String routerId=String.valueOf(availableOperation.getRouter_id());
                                        String operationId=String.valueOf(availableOperation.getOperation());
                                        String api=ContextFrame.apiConfig.Api.getEndPointAccess().getDataJson().trim();
                                        String jwt=ContextFrame.apiConfig.Api.getEndPointAccessToken().getDataJson().toString();
                                        String partnerId = (new JSONObject(ContextFrame.userLogged.getDataJson())).getString("uuid");
                                        ApiEndpointInterface apiService =ApiClient.getClient(ContextFrame.apiConfig.Api.getEndPointAccess().
                                                getDataJson().trim()).create(ApiEndpointInterface.class);
                                        try{
                                            Call<ResponseBody> acceptService = apiService.
                                                    usersSetOperationStatus(jwt,partnerId,routerId,operationId,"1","");
                                            acceptService.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    try{
                                                        if(response.isSuccessful()){
                                                            JSONObject responseData = new JSONObject(response.body().string());
                                                            if(responseData.getString("status").trim().equals("401")){
                                                               //2022
                                                                long insert = ContextFrame.operationPartnerModel.insert(availableOperation);
                                                                Intent intent = new Intent();
                                                                setResult(Constants.UPDATE_OK_ACTIONS,intent);
                                                                finish();

                                                            }else{
                                                                messageBoxDialog(responseData.getString("erros"));
                                                            }
                                                        }
                                                    }catch (Exception e){
                                                        messageBoxDialog(e.getMessage());
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    messageBoxDialog("Erro ao Acessar o Servidor.");
                                                }
                                            });
                                        }catch (Exception e){

                                            messageBoxDialog(e.getMessage());
                                        }
                                    }catch (Exception e){

                                        messageBoxDialog(e.getMessage());
                                    }
                                }
                            };
                            ContextFrame.layout_mensage.setVisibility(View.GONE);
                            ContextFrame.recyclerView.setVisibility(View.VISIBLE);
                            ContextFrame.recyclerView.setAdapter(new LineAvailableOperationsHolderOperationAdapter(availableOperations, onClickReceive));
                        }else {
                            ContextFrame.layout_mensage.setVisibility(View.VISIBLE);
                            ContextFrame.recyclerView.setVisibility(View.GONE);
                            ContextFrame.layout_mensage_info_text.setText(R.string.not_operations_disponibilized);
                        }
                    }else{

                    }
                }catch (Exception e){
                    messageBoxDialog(e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(call);
                messageBoxDialog("Erro ao Acessar o Servidor.");

            }
        });

    }

    private void messageBoxDialog(String mensage) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ContextFrame.self);
        //define o titulo
        builder.setTitle("Informação");
        //define a mensagem
        builder.setMessage(mensage);
        //define um botão como positivo
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if(ContextFrame.alerta!=null ){
                    ContextFrame.alerta.dismiss();
                }


            }
        });

        //cria o AlertDialog
        ContextFrame.alerta = builder.create();
        //Exibe
        ContextFrame.alerta.show();

    }


    private List<AvailableOperation> loadMyOperations(String responseJson,boolean allOperations) throws JSONException {
        List<AvailableOperation> availableOperations=new ArrayList<>();
//2022
        Config userLogged = ContextFrame.userLogged;
        String idPartner = (new JSONObject(userLogged.getDataJson())).getString("uuid");

        try{
            if(allOperations){
                JSONArray listOfOperations =( (new JSONObject(responseJson))).getJSONArray("next");
                for(int next=0;next <  listOfOperations.length();next++){


                    int hasOperation =(int)ContextFrame.operationPartnerModel.
                            hasOperationPartnersInDabe(listOfOperations.getJSONObject(next).get("operation").toString(), idPartner);

                    if(hasOperation ==0) {

                        AvailableOperation operation = new AvailableOperation();
                        operation.setIrPartrner(idPartner);
                        operation.setCreated(listOfOperations.getJSONObject(next).get("created").toString());
                        operation.setRouter_id((int) listOfOperations.getJSONObject(next).get("router_id"));
                        operation.setRouter_name(listOfOperations.getJSONObject(next).get("router_name").toString());
                        operation.setRouter_id_instructions(listOfOperations.getJSONObject(next).get("router_instruction").toString());
                        operation.setStore(listOfOperations.getJSONObject(next).get("store").toString());
                        operation.setStoreName(listOfOperations.getJSONObject(next).get("storeName").toString());
                        operation.setPointStart(listOfOperations.getJSONObject(next).get("pointStart").toString());
                        operation.setDistancekm(listOfOperations.getJSONObject(next).get("distancekm").toString());
                        operation.setDistanceM(listOfOperations.getJSONObject(next).get("distanceM").toString());
                        operation.setOperation(listOfOperations.getJSONObject(next).get("operation").toString());
                        operation.setStartAddressInit(listOfOperations.getJSONObject(next).
                                getJSONArray("pointStartAddress").getJSONObject(0).get("endereco").toString());
                        availableOperations.add(operation);
                    }


                }
            }else{
                JSONArray listOfOperations =( (new JSONObject(responseJson))).getJSONArray("outs");
                for(int next=0;next <  listOfOperations.length();next++){
                    AvailableOperation operation =  new AvailableOperation();

                    int hasOperation =(int)ContextFrame.operationPartnerModel.
                            hasOperationPartnersInDabe(listOfOperations.getJSONObject(next).get("operation").toString(), idPartner);


                    if(hasOperation==0) {
                        operation.setIrPartrner(idPartner);
                        operation.setCreated(listOfOperations.getJSONObject(next).get("created").toString());
                        operation.setRouter_id((int) listOfOperations.getJSONObject(next).get("router_id"));
                        operation.setRouter_name(listOfOperations.getJSONObject(next).get("router_name").toString());
                        operation.setRouter_id_instructions(listOfOperations.getJSONObject(next).get("router_instruction").toString());

                        operation.setStore(listOfOperations.getJSONObject(next).get("store").toString());
                        operation.setStoreName(listOfOperations.getJSONObject(next).get("storeName").toString());
                        operation.setPointStart(listOfOperations.getJSONObject(next).get("pointStart").toString());
                        operation.setDistancekm(listOfOperations.getJSONObject(next).get("distancekm").toString());
                        operation.setDistanceM(listOfOperations.getJSONObject(next).get("distanceM").toString());
                        operation.setOperation(listOfOperations.getJSONObject(next).get("operation").toString());
                        operation.setStartAddressInit(listOfOperations.getJSONObject(next).
                                getJSONArray("pointStartAddress").getJSONObject(0).get("endereco").toString());

                        availableOperations.add(operation);
                    }
                }
            }
        }catch (Exception e){

            System.out.println(e);
            messageBoxDialog(e.getMessage());

        }

        return availableOperations;

    }






    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("result","");
        setResult(Constants.UPDATE_OK_ACTIONS,intent);
        finish();


    }




    /*Lista de Operações disponiveis*/


    public static class LineAvailableOperationsHolder extends RecyclerView.ViewHolder {
        public TextView item_store_name,item_router_id,item_router_name,grf_txt_operacao,
                item_start_point,item_dist_metros,item_dist_kilometros,item_created_at,
                item_input_address_start;
        public Button item_btn_accepted;

        public LineAvailableOperationsHolder(View itemView) {
            super(itemView);
            this.grf_txt_operacao=(TextView)itemView.findViewById(R.id.grf_txt_operacao);
            this.item_store_name=(TextView) itemView.findViewById(R.id.item_store_name);
            this.item_router_id=(TextView) itemView.findViewById(R.id.item_router_id);
            this.item_router_name=(TextView) itemView.findViewById(R.id.item_router_name);
            this.item_start_point=(TextView) itemView.findViewById(R.id.item_start_point);
            this.item_dist_metros=(TextView) itemView.findViewById(R.id.item_dist_metros);
            this.item_dist_kilometros=(TextView) itemView.findViewById(R.id.item_dist_kilometros);
            this.item_created_at=(TextView) itemView.findViewById(R.id.item_created_at);
            this.item_btn_accepted=(Button) itemView.findViewById(R.id.item_btn_accepted);
            this.item_input_address_start=(TextView) itemView.findViewById(R.id.input_address_start);
        }
    }






    public static class LineAvailableOperationsHolderOperationAdapter extends RecyclerView.Adapter<LineAvailableOperationsHolder> {

        private List<AvailableOperation> LineavailableOperationsHolder = new ArrayList<>();
        private OnClick onClick;

        public LineAvailableOperationsHolderOperationAdapter(List<AvailableOperation> LineavailableOperationsHolder,OnClick onClick) {
            this.LineavailableOperationsHolder = LineavailableOperationsHolder;
            this.onClick=onClick;
        }

        @Override
        public LineAvailableOperationsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LineAvailableOperationsHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_list_available_operations_view, parent, false));
        }

        @Override
        public void onBindViewHolder(LineAvailableOperationsHolder holder, int position) {
            final  int positionSelected=position;
            AvailableOperation route = LineavailableOperationsHolder.get(position);

            holder.grf_txt_operacao.setText(this.LineavailableOperationsHolder.get(position).getOperation());
            holder.item_router_id.setText(String.valueOf(this.LineavailableOperationsHolder.get(position).getRouter_id()));
            holder.item_store_name.setText(this.LineavailableOperationsHolder.get(position).getStoreName());
            holder.item_router_name.setText(this.LineavailableOperationsHolder.get(position).getRouter_name());
            holder.item_start_point.setText(this.LineavailableOperationsHolder.get(position).getPointStart());
            holder.item_created_at.setText(this.LineavailableOperationsHolder.get(position).getCreated());
            holder.item_dist_kilometros.setText(this.LineavailableOperationsHolder.get(position).getDistancekm());
            holder.item_dist_metros.setText(this.LineavailableOperationsHolder.get(position).getDistanceM());
            holder.item_input_address_start.setText(this.LineavailableOperationsHolder.get(position).getStartAddressInit());
            holder.item_btn_accepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  try{

                      ContextFrame.loadProcess=new LoadProcess(view.getContext(), "Realizando Processamento...",false);
                      onClick.onClick(LineavailableOperationsHolder.get(positionSelected));
                      ContextFrame.loadProcess.loadActivityClose();
                  }catch (Exception e){}
                }
            });
        }
        @Override
        public int getItemCount() {
            return LineavailableOperationsHolder != null ? LineavailableOperationsHolder.size() : 0;
        }
    }



    /*Lista de Operações disponiveis*/


    /*Class de Componentes*/



    public  static  class Components{
        public ApiConfig apiConfig;
        public OperationPartnerBusines operationPartnerModel ;
        public TextView layout_mensage_info_text,input_address_start;
        public ProgressDialog pd;
        public AlertDialog alerta;
        public LoadProcess loadProcess;
        private LinearLayout layout_mensage;
        private String typeList;
        public Config userLogged;
        private Context self;
        public TextView info_tex_upcoming_operations1;
        public ScrollView scrollHomePartens;
        private LinearLayout container;
        private ConfigDataAccess configModel;
        private TextView info_tex_upcoming_operations;
        private Button info_fg_btn_close,info_fg_btn_upcoming_operations;
        private View view;
        private RecyclerView recyclerView;
        private HomeParterns.LineOperationAdapter lineOperationAdapter;
        private OnClick onClick;
        private CardView cardOperationsNext;
        private ScrollView scrollView;

    }


}
