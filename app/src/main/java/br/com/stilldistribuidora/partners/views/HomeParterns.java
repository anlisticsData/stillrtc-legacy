package br.com.stilldistribuidora.partners.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.common.CastResponseApi;
import br.com.stilldistribuidora.common.LoadProcess;
import br.com.stilldistribuidora.httpService.partners.PartnersServices;
import br.com.stilldistribuidora.partners.Base.Playler;
import br.com.stilldistribuidora.partners.resources.SERVICES;
import br.com.stilldistribuidora.partners.views.core.models.OperationsPartnersModel;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.business.OperationPartnerBusines;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.AvailableOperation;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.db.models.MyRouterSerialized;
import br.com.stilldistribuidora.stillrtc.db.models.StateOperation;
import br.com.stilldistribuidora.stillrtc.interfac.OnActionParentActivity;
import br.com.stilldistribuidora.stillrtc.interfac.OnApiResponse;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeParterns extends Fragment {
    private static final Components ContextFrame = new Components();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ContextFrame.self = container.getContext();
        return inflater.inflate(R.layout.main_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ContextFrame.view = view;
            ContextFrame.loadProcess = new LoadProcess(getContext(), "Carregando Informações..", false);
            ContextFrame.configModel = new ConfigDataAccess(ContextFrame.view.getContext());
            ContextFrame.container = view.findViewById(R.id.home_container);
            ContextFrame.layout_next_operation = view.findViewById(R.id.layout_next_operation);
            ContextFrame.layout_mensage = view.findViewById(R.id.layout_mensage);
            ContextFrame.info_tex_upcoming_operations = view.findViewById(R.id.info_tex_upcoming_operations);
            ContextFrame.info_tex_upcoming_operations1 = view.findViewById(R.id.info_tex_upcoming_operations1);
            ContextFrame.info_fg_btn_upcoming_operations = view.findViewById(R.id.info_fg_btn_upcoming_operations);
            ContextFrame.info_fg_btn_all_opcoming_operations = view.findViewById(R.id.info_fg_btn_all_operations);
            ContextFrame.info_fg_btn_close = (ImageView) view.findViewById(R.id.info_fg_btn_close);
            ContextFrame.recyclerView = view.findViewById(R.id.recycler_view_layour_recycler);
            ContextFrame.userLogged = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));
            ContextFrame.token = (Config) ContextFrame.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_TOKEN_JWT));
            ContextFrame.cardOperationsNext = view.findViewById(R.id.cardOperationsNext);
            ContextFrame.layout_next_operation.requestFocus();
            initEvents();
            getAll();

            ContextFrame.apiConfig = new ApiConfig(ContextFrame.self);
            ContextFrame.partnersServices = new PartnersServices(
                    new ConfigDataAccess(ContextFrame.view.getContext()), ContextFrame.userLogged,
                    ContextFrame.apiConfig.Api.getEndPointAccess(), ContextFrame.token,
                    ContextFrame.apiConfig.Api.getEndPointAccessToken());


            List<MyRouterSerialized> geoOperations = userLoginOperation();
            List<MyRouterSerialized> geoOperationsOthers = userLoginOperationOthers();
            ContextFrame.info_tex_upcoming_operations.setText(String.valueOf(geoOperations.size()));
            ContextFrame.info_tex_upcoming_operations1.setText(String.valueOf(geoOperationsOthers.size()));

            try {
                JSONObject userJson = new JSONObject(ContextFrame.userLogged.getDataJson());
                ContextFrame.uuid = userJson.getString("uuid");
            } catch (Exception e) {
            }


            //  ContextFrame.scrollHomePartens = ContextFrame.view.findViewById(R.id.scrollHomePartens);
            //  ContextFrame.scrollHomePartens.fling(0);
            // ContextFrame.scrollHomePartens.smoothScrollTo(0, 0);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {

                            if (!SERVICES.STATE_OPERATION) {
                                SERVICES.STATE_OPERATION = true;
                                //59
                                System.out.println("#3 consultando..!");
                                checkService();
                                checkServiceOperation();
                                endOperation();

                            }

                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                    }
                }
            }).start();
            ContextFrame.loadProcess.loadActivityClose();

        } catch (Exception e) {
        }


        ContextFrame.responseHttp = new OnApiResponse() {
            @Override
            public void onSucess(Object responseObject) {
                SERVICES.STATE_OPERATION = false;
            }

            @Override
            public void onError(Object responseObject) {
                SERVICES.STATE_OPERATION = false;
            }
        };


    }



    private void endOperation() {


        OperationPartnerBusines operationPartnerModel = new OperationPartnerBusines(getContext());
        List<AvailableOperation> finishedOperations = (List<AvailableOperation>) operationPartnerModel.getList(
                Constants.PARTNER_OPERATION_STATE_OPERATION + "=4   ", "");
        if (finishedOperations.size() > 0) {
            for (AvailableOperation operation : finishedOperations) {
                System.out.println("#4 " + operation.getOperation());
                if (Utils.isNetworkAvailable(getContext()) && Utils.isOnline()) {
                    ContextFrame.partnersServices.
                            partnerCompletedTheTransaction(operation.getOperation(), operation.getIrPartrner(),
                                    operation.getDeviceId(), operation.getRouter_id(), ContextFrame.responseHttp);
                } else {
                    ContextFrame.responseHttp.onError("Sem Internet.");
                }
            }
        }

    }

    private void checkServiceOperation() {


        OperationPartnerBusines operationPartnerModel = new OperationPartnerBusines(getContext());
        List<AvailableOperation> operationsAwaitingApproval = (List<AvailableOperation>) operationPartnerModel.getList(
                Constants.PARTNER_OPERATION_STATE_OPERATION + "=1 or " +
                        Constants.PARTNER_OPERATION_STATE_OPERATION + "=2", "");
        StringBuffer operationsIds = new StringBuffer();
        for (AvailableOperation op : operationsAwaitingApproval) {
            operationsIds.append(op.getOperation());
            operationsIds.append(",");
        }
        OnApiResponse responseHttp = new OnApiResponse() {
            @Override
            public void onSucess(Object responseObject) {
                OperationPartnerBusines operationPartnerModel = new OperationPartnerBusines(getContext());
                List<StateOperation> stateOperation = (List<StateOperation>) responseObject;
                for (StateOperation operationState : stateOperation) {
                    if (!operationState.getState().equals("1")) {
                        operationPartnerModel.updateState(operationState.getState(),
                                operationState.getOperation(),
                                ContextFrame.uuid, operationState.getDeviceId());
                    }
                }
                SERVICES.STATE_OPERATION = false;
            }

            @Override
            public void onError(Object responseObject) {
                SERVICES.STATE_OPERATION = false;
            }
        };

        if (Utils.isNetworkAvailable(getContext()) && Utils.isOnline()) {
            ContextFrame.partnersServices.checkServiceOperation(operationsIds.toString(), responseHttp);
        }


    }

    private void checkService() {

        try {

            if (Utils.isNetworkAvailable(getContext()) && Utils.isOnline()) {
                String partnerId = (new JSONObject(ContextFrame.userLogged.getDataJson())).getString("uuid");
                JSONObject geo = (new JSONObject(ContextFrame.userLogged.getDataJson()))
                        .getJSONObject("address").getJSONObject("geo");
                ApiEndpointInterface apiService = ApiClient.getClient(ContextFrame.apiConfig.Api.getEndPointAccess().
                        getDataJson().trim()).create(ApiEndpointInterface.class);

                Call<ResponseBody> availiableOperations = apiService.
                        operationsNearAndFar(ContextFrame.apiConfig.Api.getEndPointAccessToken()
                                .getDataJson(), geo.getString("lat") + "," + geo.getString("lng"), "1", partnerId);


                availiableOperations.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful()) {
                                JSONObject listOfOperations = ((new JSONObject(response.body().string())));

                                ContextFrame.info_tex_upcoming_operations.setText(String.valueOf(listOfOperations.getJSONArray("next").length()));
                                ContextFrame.info_tex_upcoming_operations1.setText(String.valueOf(listOfOperations.getJSONArray("outs").length()));
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        SERVICES.STATE_OPERATION = false;
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println(t.getMessage());
                        SERVICES.STATE_OPERATION = false;
                    }
                });
            } else {
                SERVICES.STATE_OPERATION = false;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            SERVICES.STATE_OPERATION = false;
        }


    }

    public List<MyRouterSerialized> userLoginOperation() {
        List<MyRouterSerialized> operationsInGeo = new ArrayList<>();
        try {
            JSONArray nextServicesGeo = (new JSONObject(ContextFrame.userLogged.getDataJson())).getJSONArray("next_services");
            int rows = nextServicesGeo.length();
            for (int next = 0; next < rows; next++) {
                MyRouterSerialized geoOperations = new MyRouterSerialized();
                operationsInGeo.add(geoOperations);
            }
            System.out.println("s");
        } catch (Exception e) {
        }
        return operationsInGeo;
    }

    public List<MyRouterSerialized> userLoginOperationOthers() {
        List<MyRouterSerialized> operationsInGeo = new ArrayList<>();
        try {
            JSONArray nextServicesGeoOthers = (new JSONObject(ContextFrame.userLogged.getDataJson())).getJSONArray("others");
            int rows = nextServicesGeoOthers.length();
            for (int next = 0; next < rows; next++) {
                MyRouterSerialized geoOperations = new MyRouterSerialized();
                operationsInGeo.add(geoOperations);
            }
        } catch (Exception e) {
        }
        return operationsInGeo;
    }

    private List<MyRouterSerialized> loadMyOperations() throws JSONException {
        List<MyRouterSerialized> myoperationList = new ArrayList<>();
        Config userLogged = ContextFrame.userLogged;
        String uuid = (new JSONObject(userLogged.getDataJson())).getString("uuid");
        String where = String.format("(%s=%s or %s=%s ) and %s=%s", Constants.PARTNER_OPERATION_STATE_OPERATION, "1",
                Constants.PARTNER_OPERATION_STATE_OPERATION, 2, Constants.PARTNER_OPERATION_UUID_PARTNER, uuid);
        OperationPartnerBusines operationPartnerModel = new OperationPartnerBusines(getContext());
        List<AvailableOperation> list = (List<AvailableOperation>) operationPartnerModel.getList(where, "");
        for (AvailableOperation op : list) {
            if (!op.getState().equals("5")) {
                MyRouterSerialized selectOperation = new MyRouterSerialized();
                selectOperation.setOperation(op.getOperation());
                selectOperation.setStoreName(op.getStoreName());
                selectOperation.setRouter_id(String.format("%03d", op.getRouter_id()));
                selectOperation.setPointStart(op.getPointStart());
                selectOperation.setInstruction_router(op.getRouter_id_instructions());
                selectOperation.setStore(op.getStore());
                selectOperation.setStatus(op.getState());
                selectOperation.setDeviceId(op.getDeviceId());


                myoperationList.add(selectOperation);
            }

        }
        return myoperationList;
    }

    public void getAll() {
        try {
            List<MyRouterSerialized> myoperations = loadMyOperations();
            if (myoperations.size() == 0) {
                ContextFrame.recyclerView.setVisibility(View.GONE);
                ContextFrame.layout_mensage.setVisibility(View.VISIBLE);
            } else {

                OnActionParentActivity onClickEventsParents = new OnActionParentActivity() {
                    @Override
                    public void onStartActivity(String type, Object objetct) {
                        Intent intent;
                        MyRouterSerialized routerSerialize = (MyRouterSerialized) objetct;
                        switch (type) {
                            case Constants.CANCELED_OPERATION:

                                ContextFrame.loadProcess = new LoadProcess(getContext(), "Processando...", false);
                                intent = new Intent(ContextFrame.self, CancelOperationActivity.class);
                                intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE, routerSerialize);
                                ContextFrame.receiveAnswerFromChildren.launch(intent);
                                ContextFrame.loadProcess.loadActivityClose();

                                break;

                            case Constants.ROUTER_OPERATION_LOCAL_MAP:
                                ContextFrame.loadProcess = new LoadProcess(getContext(), "Processando...", false);
                                intent = new Intent(ContextFrame.self, whereAmTheMapActivity.class);
                                intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE, routerSerialize);
                                ContextFrame.receiveAnswerFromChildren.launch(intent);
                                ContextFrame.loadProcess.loadActivityClose();

                                break;

                            case Constants.ROUTER_OPERATION_OPERATION:
                                ContextFrame.loadProcess = new LoadProcess(getContext(), "Processando...", false);

                                intent = new Intent(ContextFrame.self, viewingOperationActivity.class);
                                intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE, routerSerialize);
                                ContextFrame.receiveAnswerFromChildren.launch(intent);
                                ContextFrame.loadProcess.loadActivityClose();
                                break;


                            default:

                        }

                    }
                };

                OnClick onClick = new OnClick() {
                    @Override
                    public void onClick(Object obj) {
                        CastResponseApi castResponseApi = (CastResponseApi) obj;
                        if (castResponseApi.getState() != 200) {
                            StringBuffer mensage = new StringBuffer();
                            for (String msn : castResponseApi.getMensage()) {
                                mensage.append(msn);
                                mensage.append("\n");
                            }

                            //Cria o gerador do AlertDialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(ContextFrame.self);
                            //define o titulo
                            builder.setTitle("Informação");
                            //define a mensagem
                            builder.setMessage(mensage.toString());
                            //define um botão como positivo
                            builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    ContextFrame.alerta.dismiss();
                                }
                            });

                            //cria o AlertDialog
                            ContextFrame.alerta = builder.create();
                            //Exibe
                            ContextFrame.alerta.show();
                        }
                    }
                };
                ContextFrame.recyclerView.setVisibility(View.VISIBLE);
                ContextFrame.layout_mensage.setVisibility(View.GONE);
                ContextFrame.recyclerView.setAdapter(new LineOperationAdapter(myoperations, onClick, onClickEventsParents));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        ContextFrame.photosPartnersModel.close();
        ContextFrame.operationsPartnersModel.close();
        super.onDestroy();

    }

    private void initEvents() {

        /*
         * Carrega as Base
         * */

        ContextFrame.photosPartnersModel = new PhotosPartnersModel(ContextFrame.self);
        ContextFrame.operationsPartnersModel = new OperationsPartnersModel(ContextFrame.self);













        /*
         * Carrega as Base
         * */


        ContextFrame.receiveAnswerFromChildren = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Constants.UPDATE_OK_ACTIONS) {
                            Intent intent = new Intent(ContextFrame.self, HomeParceiro.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            ContextFrame.receiveAnswerFromChildren.launch(intent);

                        }
                    }
                });


        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ContextFrame.self, LinearLayoutManager.VERTICAL, false);
        ContextFrame.recyclerView.setLayoutManager(horizontalLayoutManagaer);
        ContextFrame.onClick = new OnClick() {
            @Override
            public void onClick(Object obj) {

            }
        };


        ContextFrame.info_fg_btn_upcoming_operations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextFrame.loadProcess=new LoadProcess(getContext(),"Processando...",false);
                Intent intent = new Intent(ContextFrame.self, NewOperations.class);
                intent.putExtra("type_list", "next");
                startActivity(intent);
                ContextFrame.loadProcess.loadActivityClose();


            }
        });

        ContextFrame.info_fg_btn_all_opcoming_operations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextFrame.loadProcess=new LoadProcess(getContext(),"Processando...",false);
                Intent intent = new Intent(ContextFrame.self, NewOperations.class);
                intent.putExtra("type_list", "outs");
                ContextFrame.receiveAnswerFromChildren.launch(intent);
                ContextFrame.loadProcess.loadActivityClose();

            }
        });


        ContextFrame.info_fg_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContextFrame.self, LoginAppActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ContextFrame.receiveAnswerFromChildren.launch(intent);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        ContextFrame.scrollHomePartens.fling(0);
        //       ContextFrame.scrollHomePartens.smoothScrollTo(0, 0);

    }

    public static class ApiConfig {
        private final AppCompatActivityBase Api;

        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }

    public static class LineOperationHolder extends RecyclerView.ViewHolder {
        private final EditText reasonJustificationForCancellation;
        public TextView text_store, operationsRtc, startPoint, router_id_text, statusOperation;
        public Button verificar_position, reject_operation, views_maps_start_point, canceled_confirmed, canceled_last;
        public LinearLayout controllers_liberation, viewsJustificationForCancellation;


        public LineOperationHolder(View itemView) {
            super(itemView);
            this.operationsRtc = (TextView) itemView.findViewById(R.id.operationsRtc);
            this.text_store = (TextView) itemView.findViewById(R.id.text_store);
            this.startPoint = (TextView) itemView.findViewById(R.id.startPoint);
            this.router_id_text = (TextView) itemView.findViewById(R.id.router_id_text);
            this.statusOperation = (TextView) itemView.findViewById(R.id.statusOperation);
            this.controllers_liberation = (LinearLayout) itemView.findViewById(R.id.controllers_liberation);
            this.verificar_position = (Button) itemView.findViewById(R.id.verificar_position);
            this.reject_operation = (Button) itemView.findViewById(R.id.reject_operation);
            this.canceled_confirmed = (Button) itemView.findViewById(R.id.canceled_confirmed);
            this.canceled_last = (Button) itemView.findViewById(R.id.canceled_last);
            this.views_maps_start_point = (Button) itemView.findViewById(R.id.views_maps_start_point);
            this.viewsJustificationForCancellation = (LinearLayout) itemView.findViewById(R.id.viewsJustificationForCancellation);
            this.reasonJustificationForCancellation = (EditText) itemView.findViewById(R.id.reasonJustificationForCancellation);
        }
    }

    public static class LineOperationAdapter extends RecyclerView.Adapter<LineOperationHolder> {

        private final OnClick onClick;
        private final OnActionParentActivity onClickEventsParents;
        private List<MyRouterSerialized> typesRoutes = new ArrayList<>();


        public LineOperationAdapter(List<MyRouterSerialized> typesRoutes, OnClick onClick, OnActionParentActivity onClickEventsParents) {
            this.typesRoutes = typesRoutes;
            this.onClick = onClick;
            this.onClickEventsParents = onClickEventsParents;
        }

        @Override
        public LineOperationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LineOperationHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_list_line_view, parent, false));
        }

        @Override
        public void onBindViewHolder(LineOperationHolder holder, int position) {


            MyRouterSerialized route = typesRoutes.get(position);
            String stateName = "Aguardando Liberação";
            holder.controllers_liberation.setVisibility(View.GONE);
            if (route.getStatus() != null && route.getStatus().equals("2")) {
                stateName = "Autorizado";
                holder.controllers_liberation.setVisibility(View.VISIBLE);
            }
            holder.operationsRtc.setText(route.getOperation());
            holder.text_store.setText(route.getRouter_id() + " - " + route.getStoreName());
            holder.text_store.setTextColor(ContextCompat.getColor(ContextFrame.self, R.color.colorPrimaryDark));
            holder.startPoint.setText(route.getPointStart());
            holder.router_id_text.setText(route.getRouter_id());
            holder.statusOperation.setText(stateName);
            holder.reject_operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickEventsParents.onStartActivity(Constants.CANCELED_OPERATION, route);
                }
            });

            holder.views_maps_start_point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickEventsParents.onStartActivity(Constants.ROUTER_OPERATION_OPERATION, route);
                }
            });

            holder.verificar_position.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickEventsParents.onStartActivity(Constants.ROUTER_OPERATION_LOCAL_MAP, route);
                }
            });


            holder.canceled_last.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // holder.viewsJustificationForCancellation.setVisibility(View.GONE);
                }
            });


            holder.canceled_confirmed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnApiResponse onApiResponse = new OnApiResponse() {
                        @Override
                        public void onSucess(Object responseObject) {
                            holder.viewsJustificationForCancellation.setVisibility(View.GONE);
                            OperationPartnerBusines operationPartnerModel = new OperationPartnerBusines(view.getContext());
                            operationPartnerModel.updateState("3",
                                    holder.operationsRtc.getText().toString());
                            onClick.onClick(responseObject);
                        }

                        @Override
                        public void onError(Object responseObject) {
                            onClick.onClick(responseObject);
                        }
                    };

                    if (holder.reasonJustificationForCancellation.getText().toString().trim().length() > 0) {
                        ContextFrame.partnersServices.
                                usersSetOperationStatus(route.getRouter_id(), route.getOperation(), "3",
                                        holder.reasonJustificationForCancellation.getText().toString().trim(), onApiResponse);

                    } else {
                        CastResponseApi castResponseApi = new CastResponseApi();
                        castResponseApi.setState(500);
                        castResponseApi.setMensage("Campo de Justificativa Obrigatório.");
                        onApiResponse.onError(castResponseApi);
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return typesRoutes != null ? typesRoutes.size() : 0;
        }

    }

    public static class Components {
        public ActivityResultLauncher<Intent> receiveAnswerFromChildren;


        public AlertDialog alerta = null;

        public PartnersServices partnersServices;
        public ProgressDialog pd;
        public PhotosPartnersModel photosPartnersModel;
        public PhotosPartnersModel OperationsPartnersModel;
        public OperationsPartnersModel operationsPartnersModel;
        public String uuid;
        public Config userLogged, token;
        public TextView info_tex_upcoming_operations1;
        public ScrollView scrollHomePartens;
        public ApiConfig apiConfig;
        public OnApiResponse responseHttp;
        public LoadProcess loadProcess;
        private String typeList;
        private LinearLayout container, layout_next_operation, layout_mensage;
        private ConfigDataAccess configModel;
        private Context self;
        private TextView info_tex_upcoming_operations;
        private Button info_fg_btn_upcoming_operations, info_fg_btn_all_opcoming_operations;
        private ImageView info_fg_btn_close;
        private View view;
        private RecyclerView recyclerView;
        private LineOperationAdapter lineOperationAdapter;
        private OnClick onClick;
        private CardView cardOperationsNext;
        private ScrollView scrollView;



    }


}
