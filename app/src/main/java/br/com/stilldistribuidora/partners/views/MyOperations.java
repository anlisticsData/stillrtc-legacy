package br.com.stilldistribuidora.partners.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import br.com.stilldistribuidora.partners.Base.OnResponse;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Commom.Enum;
import br.com.stilldistribuidora.partners.Contracts.OnLoadRecycleview;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Models.PointStartAddress;
import br.com.stilldistribuidora.partners.Models.Router;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.Services.ServicesStore;
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

public class MyOperations extends BaseActivity {
    private static final ContextFrame ContextFrame = new ContextFrame();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_my);
        ContextFrame.contextActivity = this;
        Intent intent = getIntent();
        if(intent!=null &&  intent.hasExtra(Resources.STATE_OPERATION_PARTNER_OPEN)) {
            Bundle extras = getIntent().getExtras();
            ContextFrame.stateReceive=extras.getString(Resources.STATE_OPERATION_PARTNER_OPEN);
        }

        Gson gson = new Gson();
        ContextFrame.btnOpenDate = (Button) findViewById(R.id.btnOpenDate);
        ContextFrame.selectDateText = (TextView) findViewById(R.id.selectDateText);
        ContextFrame.conf = new ServerConfiguration(ContextFrame.contextActivity);
        ContextFrame.servicesStore = new ServicesStore(ContextFrame.contextActivity);
        ContextFrame.appOpeationsRepository = new AppOpeationsRepository(ContextFrame.contextActivity);
        ContextFrame.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(this));
        ContextFrame.recycler_view_layour_recycler = (RecyclerView) findViewById(R.id.recycler_view_layour_recycler);
        ContextFrame.container_message = findViewById(R.id.container_message);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ContextFrame.contextActivity);
        ContextFrame.recycler_view_layour_recycler.setLayoutManager(layoutManager);
        ContextFrame.servicesHttp = new ServicesHttp(ContextFrame.contextActivity, ContextFrame.conf, CallBackOnResponseGlobalApi());
        ContextFrame.userLoginCast = gson.fromJson(ContextFrame.conf.getUserLogged(), UserLoginCast.class);
        initDataActivity();
        initEventsActivity();
    }

    private void initEventsActivity() {
        ContextFrame.container_message.setVisibility(View.GONE);
        updateDataBaseOperations();
        ContextFrame.receiveAnswerFromChildren = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
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
        ContextFrame.container_message.setVisibility(View.GONE);
        ContextFrame.recycler_view_layour_recycler.setVisibility(View.GONE);
        OnLoadRecycleview onLoadRecycleview = new OnLoadRecycleview() {
            @Override
            public void setAdapterView(Object data) {
                closeProgressBar();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ContextFrame.container_message.setVisibility(View.GONE);
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
                                        acceptOperaion(objetct);
                                    } else if (Integer.valueOf(type) == Constants.OPEN_INICIAL_FINISH) {
                                        finishOperaion(objetct);
                                    } else if (Integer.valueOf(type) == Constants.OPEN_INICIAL_CONTINUE) {
                                        openInitCancelOperationRouter(objetct);
                                    } else if (Integer.valueOf(type) == Constants.SYNCHRONIZE_OPERATION) {
                                        synchronizedOperations(objetct);
                                    }


                                }
                            };
                            ContextFrame.container_message.setVisibility(View.GONE);
                            ContextFrame.recycler_view_layour_recycler.setVisibility(View.VISIBLE);
                            ContextFrame.
                                    recycler_view_layour_recycler
                                    .setAdapter(new ListOperation().lineToOperatpion(ContextFrame.myOperation,
                                            onClickGetAllDb, onActionParentActivity));
                        } else {
                            ContextFrame.container_message.setVisibility(View.VISIBLE);
                        }

                    }
                });

            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Date data = new Date();
                SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
                String dataCurrent = ContextFrame.selectDateText.getText().toString();
                OnResponseHttp onResponseHttp = new OnResponseHttp() {
                    @Override
                    public void onResponse(Object data) {

                        List<OperationModelRepository> operations = (List<OperationModelRepository>) data;
                        onLoadRecycleview.setAdapterView(operations);

                    }

                    @Override
                    public void onError(Object error) {
                    }
                };

                if(ContextFrame.stateReceive !=null){
                    ContextFrame.servicesStore.operationsNearAndFarBy(ContextFrame.userLoginCast,
                            dataCurrent, ContextFrame.stateReceive, onResponseHttp);
                }else{
                    ContextFrame.servicesStore.operationsNearAndFarBy(ContextFrame.userLoginCast,
                            dataCurrent, "-1", onResponseHttp);
                }



            }
        }).start();
    }
     private void synchronizedOperations(Object objetct) {
         final OperationModelRepository delivery = (OperationModelRepository) objetct;
         Intent i = new Intent(ContextFrame.contextActivity, DeliveryInformation.class);
         i.putExtra("operationCode", String.valueOf(delivery.getOperationID()));
         startActivity(i);
    }

    private void closeProgressBar() {
        ContextFrame.loadProgressBar.dismiss();
    }

    private void finishOperaion(Object objetct) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OperationModelRepository item = (OperationModelRepository) objetct;
                ContextFrame.appOpeationsRepository.updateStateOperations(
                        String.valueOf(item.getOperationID()),
                        String.valueOf(item.getParternID()), Enum.STATUS_CLOSE_SAVE_DEVICE);

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContextFrame.contextActivity);
                OnResponse click = new OnResponse() {
                    @Override
                    public void OnResponseType(String type, Object object) {
                        ContextFrame.dialog.dismiss();
                        finish();
                    }
                };
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click.OnResponseType("OK", null);
                    }
                });
                builder.setTitle("Operação Finalizada..!");
                ContextFrame.dialog = builder.create();
                ContextFrame.dialog.show();

            }
        });


    }

    private void acceptOperaion(Object objetct) {
        OperationModelRepository operation = (OperationModelRepository) objetct;
        AppOpeationsRepository deliveryRepository = new AppOpeationsRepository(ContextFrame.contextActivity);
        if (deliveryRepository.setAcceptionoperation(String.valueOf(operation.getOperationID()),
                String.valueOf(operation.getParternID()), "1") > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDataBaseOperations();
                }
            });
        }
    }

    private void openInitCancelOperationRouter(Object objetct) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent;
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
                if(operationSelected.type()==1){
                    intent = new Intent(ContextFrame.contextActivity, SelectcaraudioActivity.class);
                }else{
                    intent = new Intent(ContextFrame.contextActivity, RouteGuidanceActivity.class);
                }
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
        try{ContextFrame.datePickerDialog.show();}catch (Exception e){e.printStackTrace();}
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

    private void getLoadOperationInDate() {
        updateDataBaseOperations();
        ///ContextFrame.servicesHttp.operationsNearAndFarBy(ContextFrame.userLoginCast,dateActive,onResponseHttp);
    }

    private static class ContextFrame {
        public ActivityResultLauncher<Intent> receiveAnswerFromChildren;
        public Context contextActivity;
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
        public TextView container_message;
        public ServicesStore servicesStore;
        public AppOpeationsRepository appOpeationsRepository;
        public Dialog dialog;
        public String stateReceive=null;
        public LoadProgressBar loadProgressBar;
    }

    public class ListOperation {
        public LineToOperatpion lineToOperatpion;

        public RecyclerView.Adapter lineToOperatpion(List<OperationModelRepository> myOperation, OnClick onClickGetAllDb, OnActionParentActivity onActionParentActivity) {
            return this.lineToOperatpion = new LineToOperatpion(myOperation, onClickGetAllDb, onActionParentActivity);
        }

        public class OperationsRows extends RecyclerView.ViewHolder {
            public OperationsRows(View itemView) {
                super(itemView);
            }
        }

        public class LineToOperatpion extends RecyclerView.Adapter<OperationsRows> {

            private final OnActionParentActivity onClickEventsParents;
            private final OnClick onClick;
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
            private Button openViewPicturesOperations, openrouterFinalizar;
            private Button openViewStarOperation, openRouterInMap, openCancel, openrouter, confirme,openrouterContinuar;
            private LinearLayout lnContinue,isOperationSoundCar;
            private Button synchronizeOperation;


            public LineToOperatpion(List<OperationModelRepository> typesRoutes, OnClick onClick, OnActionParentActivity onClickEventsParents) {
                this.typesRoutes = typesRoutes;
                this.onClick = onClick;
                this.onClickEventsParents = onClickEventsParents;
                this.control = new OperationControlStateModel(ContextFrame.contextActivity);
            }


            @Override
            public OperationsRows onCreateViewHolder(ViewGroup parent, int viewType) {
                return new OperationsRows(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rows_operation_acepted_my, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull OperationsRows holder, int position) {

                OperationModelRepository operationModelRepository = this.typesRoutes.get(position);
                PointStartAddress pointStartAddress = new Gson().fromJson(operationModelRepository.getPontStart(), PointStartAddress.class);
                Router router = new Gson().fromJson(operationModelRepository.getRouterJson(), Router.class);
                this.router_id_text = holder.itemView.findViewById(R.id.router_id_text);
                this.lnContinue = (LinearLayout) holder.itemView.findViewById(R.id.lnContinue);
                this.openrouterFinalizar = (Button) holder.itemView.findViewById(R.id.openrouterFinalizar);



                this.router_id_text.setText(String.valueOf(operationModelRepository.getRouterID()));
                this.created_id_text = (TextView) holder.itemView.findViewById(R.id.created_id_text);
                this.created_id_text.setText(operationModelRepository.getCreatedAt());
                this.store_name = (TextView) holder.itemView.findViewById(R.id.store_name);
                this.store_name.setText(operationModelRepository.getStorename());
                this.operation_id = (TextView) holder.itemView.findViewById(R.id.operation_id);
                this.operation_id.setText(String.valueOf(operationModelRepository.getOperationID()));
                this.store_id = (TextView) holder.itemView.findViewById(R.id.store_id);
                this.store_id.setText(String.valueOf(operationModelRepository.getStoreID()));
                this.router_uuid = (TextView) holder.itemView.findViewById(R.id.router_uuid);
                this.router_uuid.setText(String.valueOf(operationModelRepository.getRouterID()));
                this.addres_name = (TextView) holder.itemView.findViewById(R.id.addres_name);
                this.addres_name.setText(pointStartAddress.endereco);
                this.statusOperation = (TextView) holder.itemView.findViewById(R.id.statusOperation);
                this.statusOperation.setText(setState(String.valueOf(operationModelRepository.getState())));
                this.openViewPicturesOperations = (Button) holder.itemView.findViewById(R.id.openViewPicturesOperations);
                this.openrouterContinuar=(Button)holder.itemView.findViewById(R.id.openrouterContinuar);
                this.isOperationSoundCar =(LinearLayout)holder.itemView.findViewById(R.id.isOperationSoundCar);
                if(operationModelRepository.type()==1){
                    this.isOperationSoundCar.setVisibility(View.VISIBLE);
                }


                this.openViewPicturesOperations.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickEventsParents.onStartActivity(String.valueOf(Constants.FINISH_OPERATION_VIEW_PHOTO_CLOSE), operationModelRepository);
                    }
                });


                this.openRouterInMap = (Button) holder.itemView.findViewById(R.id.openRouterInMap);
                this.openRouterInMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickEventsParents.onStartActivity(String.valueOf(Constants.OPEN_INICIAL_ROUTER), operationModelRepository);
                    }
                });


                this.synchronizeOperation = holder.itemView.findViewById(R.id.synchronizeOperation);
                this.synchronizeOperation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickEventsParents.onStartActivity(String.valueOf(Constants.SYNCHRONIZE_OPERATION), operationModelRepository);
                    }
                });


                this.openrouterFinalizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickEventsParents.onStartActivity(String.valueOf(Constants.OPEN_INICIAL_FINISH), operationModelRepository);
                    }
                });

                this.openCancel = (Button) holder.itemView.findViewById(R.id.operationcancel);
                this.openCancel.setVisibility(View.GONE);
                if(operationModelRepository.getState()!=4 && operationModelRepository.getState()!=3){
                    this.openCancel.setVisibility(View.VISIBLE);
                    this.openCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickEventsParents.onStartActivity(String.valueOf(Constants.CANCELED_ACTIONS), operationModelRepository);
                        }
                    });
                }

                this.confirme = (Button) holder.itemView.findViewById(R.id.confirme);
                this.confirme.setVisibility(View.GONE);
                if (operationModelRepository.getState() == 0 && operationModelRepository.getState()!=4 && operationModelRepository.getState()!=3) {
                    this.confirme.setVisibility(View.VISIBLE);
                    this.confirme.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickEventsParents.onStartActivity(String.valueOf(Constants.ACCEPT_THE_OPERATION), operationModelRepository);
                        }
                    });
                }


                if (operationModelRepository.getAcceptOperation() != null && operationModelRepository.getAcceptOperation().equals("1")
                        && operationModelRepository.getState() != 6 && operationModelRepository.getState()!=4
                        && operationModelRepository.getState()!=3) {
                    this.confirme.setVisibility(View.GONE);
                    this.openrouter = (Button) holder.itemView.findViewById(R.id.openrouter);
                    this.openrouter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickEventsParents.onStartActivity(String.valueOf(Constants.SCRIPTWRITE), operationModelRepository);
                        }
                    });
                    this.openrouter.setVisibility(View.VISIBLE);
                }

                this.openrouterContinuar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickEventsParents.onStartActivity(String.valueOf(Constants.OPEN_INICIAL_CONTINUE), operationModelRepository);
                    }
                });


                if (operationModelRepository.getAcceptOperation() != null && operationModelRepository.getState() == 6 && operationModelRepository.getState()!=4) {
                    this.lnContinue.setVisibility(View.VISIBLE);




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
                        action =getResources().getString(R.string.OPERATION_RELEASED);
                        break;

                    case "1":
                        action = getResources().getString(R.string.WAITING_RELEASE);
                        break;


                    case "3":
                        action = getResources().getString(R.string.OPERATION_CANCELED);
                        break;

                    case "4":
                        action =getResources().getString(R.string.OPERATION_COMPLETED);
                        break;


                    case "5":
                        action =getResources().getString(R.string.OPERATION_CANCELED_ADMINISTRATOR);
                        break;

                    case "6":
                        action = getResources().getString(R.string.IN_PROGRESS);
                        break;


                    default:
                }

                return action;
            }


        }


    }

    public void onBack (View view) {
        startActivity(new Intent(ContextFrame.contextActivity, HomeActivity.class));
    }
}
