package br.com.stilldistribuidora.partners.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.stilldistribuidora.common.LoadProcess;
import br.com.stilldistribuidora.partners.views.core.entity.PartnersOperationControlStateEntity;
import br.com.stilldistribuidora.partners.views.core.models.OperationControlStateModel;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.business.OperationPartnerBusines;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.AvailableOperation;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.db.models.MyRouterSerialized;
import br.com.stilldistribuidora.stillrtc.interfac.OnActionParentActivity;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;

public class PerfilActivity extends Fragment {
    private static Components componentsStatics = new Components();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        componentsStatics.self = getContext();
        return inflater.inflate(R.layout.perfil_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            componentsStatics.btnOpenDate = (Button) view.findViewById(R.id.btnOpenDate);
            componentsStatics.selectDateText = (TextView) view.findViewById(R.id.selectDateText);
            componentsStatics.recycler_view_layour_recycler = (RecyclerView) view.findViewById(R.id.recycler_view_layour_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(componentsStatics.self);
            componentsStatics.recycler_view_layour_recycler.setLayoutManager(layoutManager);
            componentsStatics.container_message = (LinearLayout) view.findViewById(R.id.layout_mensage);
            Date data = new Date();
            SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
            componentsStatics.selectDateText.setText(formatador.format(data));
            initSession();
            getLoadOperationInDate();
            OnClick onClickSelectDate = new OnClick() {
                @Override
                public void onClick(Object obj) {
                    componentsStatics.selectDateText.setText((String) obj);
                    getLoadOperationInDate();
                }
            };

            componentsStatics.selectDateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDataPicButton();
                }
            });
            componentsStatics.btnOpenDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDataPicButton();
                }
            });
            initDataPicButton(onClickSelectDate);
        } catch (Exception e) {

            //Cria o gerador do AlertDialog
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(componentsStatics.self);
            //define o titulo
            builder.setTitle("Informação");
            //define a mensagem
            builder.setMessage(e.getMessage());
            //define um botão como positivo
            builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    componentsStatics.alerta.dismiss();

                }
            });

            //cria o AlertDialog
            componentsStatics.alerta = builder.create();
            //Exibe
            componentsStatics.alerta.show();

        }

    }

    private void getLoadOperationInDate() {

        componentsStatics.recycler_view_layour_recycler.setVisibility(View.GONE);
        String where = " (" + Constants.PARTNER_OPERATION_UUID_PARTNER + "=" + componentsStatics.uuid +
                " and " + Constants.PARTNER_OPERATION_CREATED_AT + "  like '" +
                componentsStatics.selectDateText.getText().toString() + "%')";


        System.out.println("#4 " + where);
        componentsStatics.operationPartnerModel.getList(where, "");

        componentsStatics.myOperation = (List<AvailableOperation>) componentsStatics.operationPartnerModel.getList(where, "");
        getOperationDateAll();
    }

    private void initSession() throws JSONException {
        componentsStatics.apiConfig = new ApiConfig(componentsStatics.self);
        componentsStatics.configModel = new ConfigDataAccess(componentsStatics.self);
        componentsStatics.userLogged = (Config) componentsStatics.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));
        componentsStatics.uuid = (new JSONObject(componentsStatics.userLogged.getDataJson())).getString("uuid");

        componentsStatics.token = (Config) componentsStatics.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_TOKEN_JWT));
        componentsStatics.operationPartnerModel = new OperationPartnerBusines(componentsStatics.self);


    }

    private void getOperationDateAll() {


        componentsStatics.loadProcess = new LoadProcess(componentsStatics.self, "Carregando Informação ...", false);
        componentsStatics.loadProcess.loadActivityClose();
        OnClick onClickGetAllDb = new OnClick() {
            @Override
            public void onClick(Object obj) {

            }
        };

        OnActionParentActivity onActionParentActivity = new OnActionParentActivity() {
            @Override
            public void onStartActivity(String type, Object objetct) {
                if (Integer.valueOf(type) == Constants.FINISH_OPERATION_VIEW_PHOTO_CLOSE) {
                    componentsStatics.loadProcess = new LoadProcess(componentsStatics.self, "Carregando Informação ...", false);
                    AvailableOperation operationSelectd = (AvailableOperation) objetct;
                    Intent intent = new Intent(componentsStatics.self, GaleriaOperationViewActivity.class);
                    intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_SELECT_IMAGE, operationSelectd);
                    startActivity(intent);
                    componentsStatics.loadProcess.loadActivityClose();


                } else if (Integer.valueOf(type) == Constants.OPEN_INICIAL_ROUTER) {

                    componentsStatics.loadProcess = new LoadProcess(componentsStatics.self, "Carregando Rota ...", false);
                    AvailableOperation operationSelectd = (AvailableOperation) objetct;
                    MyRouterSerialized routerSerialize = new MyRouterSerialized();
                    routerSerialize.setOperation(operationSelectd.getOperation());
                    routerSerialize.setDeviceId(operationSelectd.getDeviceId());
                    routerSerialize.setInstruction_router(operationSelectd.getRouter_id_instructions());
                    routerSerialize.setRouter_id(String.valueOf(operationSelectd.getRouter_id()));
                    routerSerialize.setCreated(operationSelectd.getCreated());
                    routerSerialize.setPointStart(operationSelectd.getPointStart());
                    routerSerialize.setStoreName(operationSelectd.getStoreName());
                    routerSerialize.setStatus(operationSelectd.getState());
                    Intent intent = new Intent(componentsStatics.self, viewingOperationActivity.class);
                    intent.putExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE, routerSerialize);
                    startActivity(intent);
                    componentsStatics.loadProcess.loadActivityClose();


                }
            }
        };
        if (componentsStatics.myOperation.size() > 0) {
            componentsStatics.container_message.setVisibility(View.GONE);
            componentsStatics.recycler_view_layour_recycler.setVisibility(View.VISIBLE);
            componentsStatics.
                    recycler_view_layour_recycler
                    .setAdapter(new LineToOperatpionAcepted(componentsStatics.myOperation,
                            onClickGetAllDb, onActionParentActivity));
        } else {
            componentsStatics.container_message.setVisibility(View.VISIBLE);
        }
    }


    private String makeDateString(int day, int month, int year) {
        return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }

    private void openDataPicButton() {
        componentsStatics.datePickerDialog.show();
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
        componentsStatics.datePickerDialog = new DatePickerDialog(componentsStatics.self, style, dateSetListener, year, month, day);
    }


    public static class OperationsRowsAcepted extends RecyclerView.ViewHolder {
        public OperationsRowsAcepted(View itemView) {
            super(itemView);

        }
    }


    public static class LineToOperatpionAcepted extends RecyclerView.Adapter<OperationsRowsAcepted> {

        private final OnActionParentActivity onClickEventsParents;
        private final OnClick onClick;
        private List<AvailableOperation> typesRoutes = new ArrayList<>();
        private TextView router_id_text;
        private TextView created_id_text;
        private TextView store_name;
        private TextView operation_id;
        private TextView store_id;
        private TextView router_uuid;
        private TextView addres_name;
        private TextView statusOperation;
        private Button openViewPicturesOperations;
        private OperationControlStateModel control;
        private Button openViewStarOperation;


        public LineToOperatpionAcepted(List<AvailableOperation> typesRoutes, OnClick onClick, OnActionParentActivity onClickEventsParents) {
            this.typesRoutes = typesRoutes;
            this.onClick = onClick;
            this.onClickEventsParents = onClickEventsParents;
            this.control = new OperationControlStateModel(componentsStatics.self);
        }

        private String setState(String state) {
            String action = "";
            switch (state) {
                case "1":
                    action = "AGUARDANDO LIBERAÇÂO";
                    break;

                case "2":
                    action = "OPERAÇÂO LIBERADA";
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

        @Override
        public OperationsRowsAcepted onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OperationsRowsAcepted(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rows_operation_acepted, parent, false));
        }

        @Override
        public void onBindViewHolder(OperationsRowsAcepted holder, int position) {

            AvailableOperation availableOperation = this.typesRoutes.get(position);
            this.router_id_text = holder.itemView.findViewById(R.id.router_id_text);
            this.router_id_text.setText(availableOperation.getUuid());
            this.created_id_text = (TextView) holder.itemView.findViewById(R.id.created_id_text);
            this.created_id_text.setText(availableOperation.getCreated());
            this.store_name = (TextView) holder.itemView.findViewById(R.id.store_name);
            this.store_name.setText(availableOperation.getStoreName());
            this.operation_id = (TextView) holder.itemView.findViewById(R.id.operation_id);
            this.operation_id.setText(availableOperation.getOperation());
            this.store_id = (TextView) holder.itemView.findViewById(R.id.store_id);
            this.store_id.setText(availableOperation.getStore());
            this.router_uuid = (TextView) holder.itemView.findViewById(R.id.router_uuid);
            this.router_uuid.setText(availableOperation.getIdUserSelectonOperation());
            this.addres_name = (TextView) holder.itemView.findViewById(R.id.addres_name);
            this.addres_name.setText(availableOperation.getStartAddressInit());
            this.statusOperation = (TextView) holder.itemView.findViewById(R.id.statusOperation);
            this.statusOperation.setText(setState(availableOperation.getState()));
            this.openViewPicturesOperations = (Button) holder.itemView.findViewById(R.id.openViewPicturesOperations);
            this.openViewPicturesOperations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickEventsParents.onStartActivity(String.valueOf(Constants.FINISH_OPERATION_VIEW_PHOTO_CLOSE), availableOperation);
                }
            });
            this.openViewStarOperation = (Button) holder.itemView.findViewById(R.id.openViewStarOperation);
            this.openViewStarOperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickEventsParents.onStartActivity(String.valueOf(Constants.OPEN_INICIAL_ROUTER), availableOperation);
                }
            });


            List<PartnersOperationControlStateEntity> controlOp =
                    (List<PartnersOperationControlStateEntity>) this.control.operationStateIsAtivo(
                            new String[]{componentsStatics.uuid, availableOperation.getOperation().trim(), "P"});

            if (controlOp.size() > 0 && controlOp.get(0).getDelivery_fragment_id().trim().equals(availableOperation.getOperation().trim())) {
                this.statusOperation.setText(setState("6"));
                this.openViewStarOperation.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public int getItemCount() {
            return typesRoutes != null ? typesRoutes.size() : 0;
        }

    }


    public static class ApiConfig {
        private final AppCompatActivityBase Api;

        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }

    public static class Components {
        public ApiConfig apiConfig;
        public Context self;
        public Config userLogged, token;
        public OperationPartnerBusines operationPartnerModel;
        public String uuid;
        public androidx.appcompat.app.AlertDialog alerta;
        public ProgressDialog pd;
        public LinearLayout container_message;
        public LoadProcess loadProcess;
        public ConfigDataAccess configModel;
        public TextView selectDateText;
        private DatePickerDialog datePickerDialog;
        private Button btnOpenDate;
        private List<AvailableOperation> myOperation;

        private RecyclerView recycler_view_layour_recycler;


    }


}
