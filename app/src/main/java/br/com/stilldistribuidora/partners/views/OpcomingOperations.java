package br.com.stilldistribuidora.partners.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.stilldistribuidora.partners.Adapter.ConfigurationsUser;
import br.com.stilldistribuidora.partners.Base.LoadProgressBar;
import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateReaderDbHelper;
import br.com.stilldistribuidora.partners.Casts.AvailableOperationsCast;
import br.com.stilldistribuidora.partners.Casts.HelperParser;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Commom.Enum;
import br.com.stilldistribuidora.partners.Contracts.OnLoadRecycleview;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Models.PointStartAddress;
import br.com.stilldistribuidora.partners.Models.Router;
import br.com.stilldistribuidora.partners.Models.TypeOperation;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.partners.resources.Resources;
import br.com.stilldistribuidora.partners.views.core.models.OperationControlStateModel;
import br.com.stilldistribuidora.pco.ui.BaseActivity;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.MyRouterSerialized;
import br.com.stilldistribuidora.stillrtc.interfac.OnActionParentActivity;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;

public class OpcomingOperations extends BaseActivity {
    private static final ContextFrame ContextFrame = new ContextFrame();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_opcoming);
        ContextFrame.contextActivity = this;
        Gson gson = new Gson();
        ContextFrame.toolbar = findViewById(R.id.toolbar);
        ContextFrame.btnOpenDate = findViewById(R.id.btnOpenDate);
        ContextFrame.selectDateText = findViewById(R.id.selectDateText);
        ContextFrame.conf = new ServerConfiguration(ContextFrame.contextActivity);
        ContextFrame.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(this));
        ContextFrame.appOpeationsRepository = new AppOpeationsRepository(ContextFrame.contextActivity);
        ContextFrame.DeliveryStateReaderDbHelper = new DeliveryStateReaderDbHelper(ContextFrame.contextActivity);
        ContextFrame.recycler_view_layour_recycler = findViewById(R.id.recycler_view_layour_recycler);
        ContextFrame.container_message = findViewById(R.id.layout_mensage);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ContextFrame.contextActivity);
        ContextFrame.recycler_view_layour_recycler.setLayoutManager(layoutManager);
        ContextFrame.servicesHttp = new ServicesHttp(ContextFrame.contextActivity, ContextFrame.conf, CallBackOnResponseGlobalApi());
        String userLogged = ContextFrame.conf.getUserLogged();
        ContextFrame.userLoginCast = gson.fromJson(userLogged, UserLoginCast.class);
        initDataActivity();
        initEventsActivy();
        Intent intent = getIntent();
        if(intent.hasExtra("onHasOperations")) {
            String onHasOperations = intent.getStringExtra("onHasOperations");
            if(Boolean.valueOf(onHasOperations)){
                updateDataBaseOperations();
            }
        }
    }

    @SuppressLint("InflateParams")
    void startLoadingdialog() {

        // adding ALERT Dialog builder object and passing activity as parameter
        AlertDialog.Builder builder = new AlertDialog.Builder(ContextFrame.contextActivity);

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = OpcomingOperations.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);

        ContextFrame.dialog = builder.create();
        ContextFrame.dialog.show();
    }

    // dismiss method
    void dismissdialog() {
        ContextFrame.dialog.dismiss();
    }


    private void initEventsActivy() {


        ContextFrame.receiveAnswerFromChildren = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Constants.UPDATE_OK_ACTIONS) {
                            Intent intent = new Intent(ContextFrame.contextActivity, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            ContextFrame.receiveAnswerFromChildren.launch(intent);

                        }
                    }
                });


    }



    private void updateDataBaseOperations() {

        ContextFrame.loadProgressBar = new LoadProgressBar(ContextFrame.contextActivity);
        ContextFrame.loadProgressBar.setTitle("Loading...");
        ContextFrame.loadProgressBar.setMessage("Buscando Operações...");
        ContextFrame.loadProgressBar.setCancelable(false);
        ContextFrame.loadProgressBar.show();


        ContextFrame.recycler_view_layour_recycler.setVisibility(View.GONE);
        OnLoadRecycleview onLoadRecycleview = new OnLoadRecycleview() {
            @Override
            public void setAdapterView(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ContextFrame.myOperation = (List<OperationModelRepository>) data;
                        if (ContextFrame.myOperation.size() > 0) {
                            OnClick onClickGetAllDb = new OnClick() {
                                @Override
                                public void onClick(Object obj) {

                                }
                            };

                            OnActionParentActivity onActionParentActivity = new OnActionParentActivity() {
                                @Override
                                public void onStartActivity(String type, Object objetct) {
                                    if (Integer.valueOf(type) == Constants.FINISH_OPERATION_VIEW_PHOTO_CLOSE) {
                                        setFinishOperatonView_PhotoClose(objetct);

                                    } else if (Integer.valueOf(type) == Constants.OPEN_INICIAL_ROUTER) {
                                        openInitRouter(objetct);
                                    } else if (Integer.valueOf(type) == Constants.CANCELED_ACTIONS) {
                                        openInitCancelOperation(objetct);
                                    } else if (Integer.valueOf(type) == Constants.SCRIPTWRITE) {
                                        openInitCancelOperationRouter(objetct);
                                    } else if (Integer.valueOf(type) == Constants.ACCEPT_THE_OPERATION) {

                                        OperationModelRepository operation = (OperationModelRepository) objetct;
                                        String createdAt = operation.getCreatedAt();
                                        String[] data = createdAt.split("\\-");
                                        String[] siaHora = data[2].split("\\ ");
                                        int dayLast = Integer.parseInt(siaHora[0]) - 1;
                                        String dia =String.format("%02d-%02d-%s ",dayLast,Integer.valueOf(data[1]),data[0]);
                                        //Cria o gerador do AlertDialog
                                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContextFrame.contextActivity);
                                        //define o titulo
                                        builder.setTitle("Atenção.");
                                        //define a mensagem
                                        builder.setMessage(String.format("Você tem Operações até o dia (%s) , para Cancelar a Operação sem Ser Penalizado..!",dia));
                                        //define um botão como positivo
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                ContextFrame.alerta.dismiss();
                                                acceptOperaion(objetct);
                                            }
                                        });
                                        //cria o AlertDialog
                                        ContextFrame.alerta = builder.create();
                                        //Exibe
                                        ContextFrame.alerta.show();
                                    } else if (Integer.valueOf(type) == Constants.ACCEPT_THE_OPERATION_ACTIVE) {
                                        hasOpenOperation(objetct);
                                    }
                                }
                            };
                            ContextFrame.container_message.setVisibility(View.GONE);
                            ContextFrame.recycler_view_layour_recycler.setVisibility(View.VISIBLE);
                            ContextFrame.
                                    recycler_view_layour_recycler
                                    .setAdapter(new ListOperation(ContextFrame.appOpeationsRepository).lineToOperatpion(ContextFrame.myOperation,
                                            onClickGetAllDb, onActionParentActivity));
                        } else {
                            ContextFrame.container_message.setVisibility(View.VISIBLE);
                        }

                    }
                });

            }
        };


        Date data = new Date();
        SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
        String dataCurrent = ContextFrame.selectDateText.getText().toString();



            OnResponseHttp onResponseHttp = new OnResponseHttp() {
                @Override
                public void onResponse(Object data) {
                    ContextFrame.runHttp = false;

                    try {
                        closeProgressBar();
                    } catch (Exception e) {
                    }

                    AvailableOperationsCast response = (AvailableOperationsCast) data;
                    AppOpeationsRepository deliveryRepository = new AppOpeationsRepository(ContextFrame.contextActivity);

                    for (TypeOperation op : response.getOuts()) {
                        int typeOperation =(op.typeOperation==null) ? 0 : Integer.parseInt(op.typeOperation);
                        StringBuffer zonas = new HelperParser().getToJsonZonas(op.zonasJson);
                        StringBuffer rotaMap = new HelperParser().getToJsonRouter(op.router_instruction);
                        String routerJson = new HelperParser().getRouterJson(Integer.valueOf(op.router_id), op.router_name);
                        OperationModelRepository entityOperation = new OperationModelRepository();
                        entityOperation.setOperationID(Integer.parseInt(op.operation));
                        entityOperation.setTypeOperation(typeOperation);

                        entityOperation.setRouterID(Integer.parseInt(op.router_id));
                        entityOperation.setStoreID(Integer.parseInt(op.store));
                        entityOperation.setParternID(Integer.parseInt(ContextFrame.userLoginCast.getUuid()));
                        entityOperation.setDistanceMm(op.distanceM);
                        entityOperation.setDistanceKm(op.distancekm);
                        entityOperation.setStorename(op.storeName);
                        entityOperation.setRouterJson(routerJson);
                        if (op.pointStartAddress.size() > 0) {
                            entityOperation.setPontStart(op.pointStartAddress.get(0).toJson());
                        }
                        entityOperation.setDistanceMm(op.distanceM);
                        entityOperation.setOperationID(Integer.parseInt(op.operation));
                        entityOperation.setZonasJson(zonas.toString());
                        entityOperation.setRouterMap(rotaMap.toString());
                        entityOperation.setStorename(op.storeName);
                        entityOperation.setCreatedAt(op.created);
                        deliveryRepository.create(entityOperation);

                    }
                    closeProgressBar();
                    List<OperationModelRepository> operations = deliveryRepository.getAllPartnerId(ContextFrame.userLoginCast.getUuid(), dataCurrent, "0");
                    onLoadRecycleview.setAdapterView(operations);

                }

                @Override
                public void onError(Object error) {
                    closeProgressBar();
                    ContextFrame.runHttp = false;
                }
            };

        AppOpeationsRepository deliveryRepository = new AppOpeationsRepository(ContextFrame.contextActivity);
        List<String> strings = deliveryRepository.bringAllPartnerOperationId(ContextFrame.userLoginCast.getUuid());

        if(!ContextFrame.runHttp) {

            ContextFrame.servicesHttp.operationsNearAndFarBy(ContextFrame.userLoginCast, dataCurrent, strings, onResponseHttp);
        }

    }

    private void closeProgressBar() {
        if(ContextFrame.loadProgressBar!=null){ContextFrame.loadProgressBar.dismiss(); ContextFrame.loadProgressBar=null;}
    }


    private void hasOpenOperation(Object objetct) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Cria o gerador do AlertDialog
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContextFrame.contextActivity);
                //define o titulo
                builder.setTitle("Ação Cancelada.");
                //define a mensagem
                builder.setMessage("Você tem Operações em Aberto. Finalizar. para Continuar em Outra Operação.");
                //define um botão como positivo
                builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContextFrame.alerta.dismiss();
                        Intent intent = new Intent(ContextFrame.contextActivity, MyOperations.class);
                        intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, "6");
                        startActivity(intent);
                        finish();
                    }

                });
                //cria o AlertDialog
                ContextFrame.alerta = builder.create();
                //Exibe
                ContextFrame.alerta.show();
            }
        });
    }

    private void acceptOperaion(Object objetct) {
        OperationModelRepository operation = (OperationModelRepository) objetct;
        AppOpeationsRepository deliveryRepository = new AppOpeationsRepository(ContextFrame.contextActivity);
        if (deliveryRepository.setAcceptionoperation(String.valueOf(operation.getOperationID()),
                String.valueOf(operation.getParternID()), String.valueOf(Enum.STATUS_ACCEPTED_SAVE_DEVICE)) > 0) {
                deliveryRepository.setAcceptionoperationUpdate(String.valueOf(operation.getOperationID()),
                        String.valueOf(operation.getParternID()), "N");

                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        Intent intent = new Intent(ContextFrame.contextActivity, MyOperations.class);
                        intent.putExtra(Resources.STATE_OPERATION_PARTNER_OPEN, Enum.STATUS_ACCEPTED_SAVE_DEVICE);
                        startActivity(intent);
                        finish();

                    }
                });
        }
    }

    private void openInitCancelOperationRouter(Object objetct) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                OperationModelRepository operationSelected = (OperationModelRepository) objetct;
                MyRouterSerialized routerSerialize = new MyRouterSerialized();
                routerSerialize.setOperation(String.valueOf(operationSelected.getOperationID()));
                routerSerialize.setDeviceId("00");
                routerSerialize.setInstruction_router(operationSelected.getRouterMap());
                routerSerialize.setRouter_id(String.valueOf(operationSelected.getRouterID()));
                routerSerialize.setCreated(operationSelected.getCreatedAt());
                routerSerialize.setPointStart(operationSelected.getPontStart());
                routerSerialize.setStoreName(operationSelected.getStorename());
                routerSerialize.setStatus(String.valueOf(operationSelected.getState()));
                Intent intent = new Intent(ContextFrame.contextActivity, RouteGuidanceActivity.class);
                intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE, routerSerialize);
                intent.putExtra(Constants.ROUTER_OPERATION_SELECIONADA, new Gson().toJson(operationSelected));
                startActivity(intent);


            }
        });
    }

    private void openInitCancelOperation(Object objetct) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ContextFrame.contextActivity, CancelOperationActivity.class);
                intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE, (OperationModelRepository) objetct);
                ContextFrame.receiveAnswerFromChildren.launch(intent);
            }
        });


    }

    private void openInitRouter(Object objetct) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OperationModelRepository operationSelectd = (OperationModelRepository) objetct;
                MyRouterSerialized routerSerialize = new MyRouterSerialized();
                routerSerialize.setOperation(String.valueOf(operationSelectd.getOperationID()));
                routerSerialize.setDeviceId("00");
                routerSerialize.setInstruction_router(operationSelectd.getRouterMap());
                routerSerialize.setRouter_id(String.valueOf(operationSelectd.getRouterID()));
                routerSerialize.setCreated(operationSelectd.getCreatedAt());
                routerSerialize.setPointStart(operationSelectd.getPontStart());
                routerSerialize.setStoreName(operationSelectd.getStorename());
                routerSerialize.setStatus(String.valueOf(operationSelectd.getState()));
                Intent intent = new Intent(ContextFrame.contextActivity, viewingOperationActivity.class);
                intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE, routerSerialize);
                startActivity(intent);


            }
        });


    }

    private void setFinishOperatonView_PhotoClose(Object objetct) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OperationModelRepository operationSelectd = (OperationModelRepository) objetct;
                Intent intent = new Intent(ContextFrame.contextActivity, GaleriaOperationViewActivity.class);
                intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_SELECT_IMAGE, operationSelectd);
                startActivity(intent);

            }
        });


    }


    private OnResponseHttp CallBackOnResponseGlobalApi() {
        OnResponseHttp onResponseHttp = new OnResponseHttp() {
            @Override
            public void onResponse(Object data) {
                System.out.println("d");
            }

            @Override
            public void onError(Object error) {

            }
        };
        return onResponseHttp;
    }


    private void initDataActivity() {


        try {
            Date data = new Date();
            SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
            ContextFrame.selectDateText.setText(formatador.format(data));
            OnClick onClickSelectDate = new OnClick() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(Object obj) {
                    ContextFrame.selectDateText.setText((String) obj);
                    getLoadOperationInDate();
                }
            };

            ContextFrame.selectDateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDataPicButton();
                }
            });
            ContextFrame.btnOpenDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDataPicButton();
                }
            });

            initDataPicButton(onClickSelectDate);

        } catch (Exception e) {
            //Cria o gerador do AlertDialog
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContextFrame.contextActivity);
            //define o titulo
            builder.setTitle("Informação");
            //define a mensagem
            builder.setMessage(e.getMessage());
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


    private String makeDateString(int day, int month, int year) {
        return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }

    private void openDataPicButton() {
        ContextFrame.datePickerDialog.show();
    }

    private void initDataPicButton(OnClick onClickSelectDate) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                onClickSelectDate.onClick(date);


            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        ContextFrame.datePickerDialog = new DatePickerDialog(ContextFrame.contextActivity, style, dateSetListener, year, month, day);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getLoadOperationInDate() {


        updateDataBaseOperations();
        ///ContextFrame.servicesHttp.operationsNearAndFarBy(ContextFrame.userLoginCast,dateActive,onResponseHttp);
    }

    private static class ContextFrame {
        public Boolean v =  false;
        public ActivityResultLauncher<Intent> receiveAnswerFromChildren;
        public Context contextActivity;
        public Toolbar toolbar;
        public Button btnOpenDate;
        public TextView selectDateText;
        public DatePickerDialog datePickerDialog;
        public androidx.appcompat.app.AlertDialog alerta;
        public ServerConfiguration conf;
        public ConfigurationsUser configurationsAdapter;
        public ServicesHttp servicesHttp;
        public UserLoginCast userLoginCast;
        public ProgressDialog progressDoalog;
        public Handler handle;
        public RecyclerView recycler_view_layour_recycler;
        public List<OperationModelRepository> myOperation;
        public LinearLayout container_message;
        public AppOpeationsRepository appOpeationsRepository;
        public DeliveryStateReaderDbHelper DeliveryStateReaderDbHelper;


        public Dialog dialog;
        public ProgressDialog progressDialog;
        public LoadProgressBar loadProgressBar;
        public boolean runHttp =false;
    }


    public class ListOperation {

        private final AppOpeationsRepository appOpeationsRepository;
        public LineToOperatpion lineToOperatpion;

        public ListOperation(AppOpeationsRepository appOpeationsRepository) {
            this.appOpeationsRepository = appOpeationsRepository;
        }

        public RecyclerView.Adapter lineToOperatpion(List<OperationModelRepository> myOperation, OnClick onClickGetAllDb,
                                                     OnActionParentActivity onActionParentActivity) {


            return this.lineToOperatpion = new LineToOperatpion(myOperation, onClickGetAllDb, onActionParentActivity, this.appOpeationsRepository);


        }

        public class OperationsRows extends RecyclerView.ViewHolder {
            public OperationsRows(View itemView) {
                super(itemView);
            }
        }

        public class LineToOperatpion extends RecyclerView.Adapter<OperationsRows> {

            private final OnActionParentActivity onClickEventsParents;
            private final OnClick onClick;
            private final AppOpeationsRepository appOpeationsRepository;
            private final OperationControlStateModel control;
            private List<OperationModelRepository> typesRoutes = new ArrayList<>();
            private TextView router_id_text;
            private TextView created_id_text;
            private TextView store_name;
            private TextView operation_id;
            private TextView store_id;
            private TextView router_uuid;
            private TextView addres_name;
            private TextView statusOperation;
            private Button openViewPicturesOperations;
            private Button openViewStarOperation, openRouterInMap, openCancel, openrouter, confirme;
            private Button synchronizeOperation;


            public LineToOperatpion(List<OperationModelRepository> typesRoutes, OnClick onClick,
                                    OnActionParentActivity onClickEventsParents, AppOpeationsRepository appOpeationsRepository) {

                this.typesRoutes = typesRoutes;
                this.onClick = onClick;
                this.onClickEventsParents = onClickEventsParents;
                this.control = new OperationControlStateModel(ContextFrame.contextActivity);
                this.appOpeationsRepository = appOpeationsRepository;

            }


            @Override
            public OperationsRows onCreateViewHolder(ViewGroup parent, int viewType) {
                return new OperationsRows(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rows_operation_acepted, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull OperationsRows holder, int position) {

                OperationModelRepository operationModelRepository = this.typesRoutes.get(position);
                PointStartAddress pointStartAddress = new Gson().fromJson(operationModelRepository.getPontStart(), PointStartAddress.class);
                Router router = new Gson().fromJson(operationModelRepository.getRouterJson(), Router.class);


                this.router_id_text = holder.itemView.findViewById(R.id.router_id_text);
                this.router_id_text.setText(String.valueOf(operationModelRepository.getRouterID()));
                this.created_id_text = holder.itemView.findViewById(R.id.created_id_text);
                this.created_id_text.setText(operationModelRepository.getCreatedAt());
                this.store_name = holder.itemView.findViewById(R.id.store_name);
                this.store_name.setText(operationModelRepository.getStorename());
                this.operation_id = holder.itemView.findViewById(R.id.operation_id);
                this.operation_id.setText(String.valueOf(operationModelRepository.getOperationID()));
                this.store_id = holder.itemView.findViewById(R.id.store_id);
                this.store_id.setText(String.valueOf(operationModelRepository.getStoreID()));
                this.router_uuid = holder.itemView.findViewById(R.id.router_uuid);
                this.router_uuid.setText(String.valueOf(operationModelRepository.getRouterID()));
                this.addres_name = holder.itemView.findViewById(R.id.addres_name);
                this.addres_name.setText(pointStartAddress.endereco);
                this.statusOperation = holder.itemView.findViewById(R.id.statusOperation);
                this.statusOperation.setText(setState(String.valueOf(operationModelRepository.getState())));

                this.openViewPicturesOperations = holder.itemView.findViewById(R.id.openViewPicturesOperations);
                this.openViewPicturesOperations.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickEventsParents.onStartActivity(String.valueOf(Constants.FINISH_OPERATION_VIEW_PHOTO_CLOSE), operationModelRepository);
                    }
                });




                this.openRouterInMap = holder.itemView.findViewById(R.id.openRouterInMap);
                this.openRouterInMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickEventsParents.onStartActivity(String.valueOf(Constants.OPEN_INICIAL_ROUTER), operationModelRepository);
                    }
                });


                this.openCancel = holder.itemView.findViewById(R.id.operationcancel);
                if (operationModelRepository.getState()!=3){
                    this.openCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickEventsParents.onStartActivity(String.valueOf(Constants.CANCELED_ACTIONS), operationModelRepository);
                        }
                    });

                }else{
                    this.openCancel.setVisibility(View.GONE);
                }

                this.confirme = holder.itemView.findViewById(R.id.confirme);
                if(operationModelRepository.getState()!=3) {

                    this.confirme.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //01
                            List<OperationModelRepository> activeOperations = appOpeationsRepository.hasOpenOperation(String.valueOf(operationModelRepository.getParternID()));
                            if (activeOperations.size() > 0) {
                                onClickEventsParents.onStartActivity(String.valueOf(Constants.ACCEPT_THE_OPERATION_ACTIVE), activeOperations);
                            } else {
                                onClickEventsParents.onStartActivity(String.valueOf(Constants.ACCEPT_THE_OPERATION), operationModelRepository);
                            }
                        }
                    });
                }else{
                    this.confirme.setVisibility(View.GONE);
                }
                if (operationModelRepository.getAcceptOperation() != null &&
                        operationModelRepository.getAcceptOperation()=="1") {
                    this.confirme.setVisibility(View.GONE);
                    this.openrouter = holder.itemView.findViewById(R.id.openrouter);
                    this.openrouter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickEventsParents.onStartActivity(String.valueOf(Constants.SCRIPTWRITE), operationModelRepository);
                        }
                    });
                    this.openrouter.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public int getItemCount() {
                return typesRoutes != null ? typesRoutes.size() : 0;
            }

            private String setState(String state) {
                String action = "";
                switch (state) {
                    case "0":
                        action = "OPERAÇÂO LIBERADA";
                        break;

                    case "1":
                        action = "AGUARDANDO LIBERAÇÂO";
                        break;


                    case "3":
                        action = "OPERAÇAO CANCELADA (você)";
                        break;

                    case "4":
                        action = "OPERAÇAO FINALIZADA ";
                        break;


                    case "5":
                        action = "OPERAÇAO CANCELADA (Administrador)";
                        break;

                    case "6":
                        action = "EM ANDAMENTO (*)";
                        break;


                    default:
                }

                return action;
            }


        }


    }

}
