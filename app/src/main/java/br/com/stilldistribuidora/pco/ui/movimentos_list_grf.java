package br.com.stilldistribuidora.pco.ui;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import br.com.stilldistribuidora.pco.Interface.iGui;
import br.com.stilldistribuidora.pco.Interface.onClickType;
import br.com.stilldistribuidora.pco.adapter.MovimentosViewHolder;
import br.com.stilldistribuidora.pco.api.ApiPco;
import br.com.stilldistribuidora.pco.config.Constante;
import br.com.stilldistribuidora.pco.db.dao.OperationsModelAcess;
import br.com.stilldistribuidora.pco.db.dao.TblSincronizarDao;
import br.com.stilldistribuidora.pco.db.dao.wsConfig;
import br.com.stilldistribuidora.pco.db.model.Movimentos;
import br.com.stilldistribuidora.pco.db.model.TblSincronizar;
import br.com.stilldistribuidora.pco.db.model.WsConfig;
import br.com.stilldistribuidora.pco.servicos.ServiceAppGrafica;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.ui.activities.LoginActivity;
import br.com.stilldistribuidora.stillrtc.utils.Const;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class movimentos_list_grf extends BaseActivity implements DatePickerDialog.OnDateSetListener , iGui {
    private static String USER_ATIVO = "";
    private SimpleDateFormat dateFormat;
    private Date date_atual;
    private Intent intentService;
    private List<Movimentos> movimentos = new ArrayList<>();
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int Year, Month, Day;
    private String currenDate;
    private RecyclerView mRecyclerView;
    private String currenDate_busca;
    private Button btnDate;
    private String statusSelecionado = "T";
    private Spinner grf_select_option;
    private MovimentosViewHolder mv_adapter;
    private AdapterAssociated adapter = null;
    private LinearLayoutManager llm;
    private TextView grf_txt_empty;
    private String jwt_token = "";
    private String jwt_device = "";
    private TblSincronizarDao sincronizarController;
    private ImageView btnImg;
    private onClickType onClickType;
    private ArrayAdapter adapter_tipo = null;
    private  boolean ativo_=false;
    private ProgressDialog barProgressDialog=null;
    private OperationsModelAcess operationsModelAcess;
    private Boolean processing=true,notprocessing=true;
    private TblSincronizar operacaoSincronizada = new TblSincronizar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self=this;
        setContentView(R.layout.activity_movimentos_list_grf);
        operationsModelAcess = new OperationsModelAcess(self);
        initServicGrafica();
        initComponents();
        dateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        jwt_token = (String) wsConfigCotroller.getBy(new WsConfig(Constante.TOKEN_JWT));
        jwt_device = (String) wsConfigCotroller.getBy(new WsConfig(Constante.DEVICE_ACESS));
        String dataSelecionada = (String) wsConfigCotroller.getBy(new WsConfig(Constante.ULT_DATA_SELECIONADA));
        USER_ATIVO = (String) wsConfigCotroller.getBy(new WsConfig(Constante.USER_ATIVO));
        String split[] = new String[3];
        String replace = dataSelecionada.replace("|", ";");
        String[] arrOfStr = replace.split(";");
        btnDate.setText(DateUtils.convertDatetimeStringInDate(arrOfStr[0] + "-" + arrOfStr[1] + "-" + arrOfStr[2] + " 00:00:00"));
        initActionGui();
        LayoutInflater inflater = getLayoutInflater();
        if (statusSelecionado.equals("A")) {
            grf_select_option.setSelection(0);
        } else if (statusSelecionado.equals("T")) {
            grf_select_option.setSelection(1);
        } else if (statusSelecionado.equals("E")) {
            grf_select_option.setSelection(2);
        } else if (statusSelecionado.equals("P")) {
            grf_select_option.setSelection(3);
        } else if (statusSelecionado.equals("G")) {
            grf_select_option.setSelection(4);
        }

    }




    private int  rowsGetOperationsInBase(){
        Map<String, String> searchTypes = new HashMap<String, String>();
        searchTypes.put("status","");
        searchTypes.put("data",String.format("%s-%s",currenDate_busca,USER_ATIVO));
        final int rowsCount=(int)operationsModelAcess.findAll(searchTypes);
        return rowsCount;
    }




    private void checkAvailableOperation() {
        invalidateOptionsMenu();
        Map<String, String> searchTypes = new HashMap<String, String>();
        searchTypes.put("status","");
        searchTypes.put("data",String.format("%s-%s",currenDate_busca,USER_ATIVO));

        final int rowsCount=(int)operationsModelAcess.findAll(searchTypes);
        final String[] tmpUser = USER_ATIVO.trim().replace('|', ';').split(";");
        if (!Utils.isNetworkAvailable(self) || !Utils.isOnline()) {
            Toast.makeText(self,getString(R.string.not_conect),Toast.LENGTH_LONG).show();
            return ;
        }
        notprocessing=true;
        new ApiPco().get_material_grafica_data_status().get_material_grafica_data_status(jwt_token, jwt_device,
                currenDate_busca, tmpUser[0], "G").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    StringBuffer ids= new StringBuffer();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String error = jsonObject.getString("error");
                        String http = jsonObject.getString("http");
                        if (error.equals("false") && http.equals("200")) {
                            String data = jsonObject.getString("data");
                            JSONArray array_data = new JSONArray(data);
                            int total_de_itens = array_data.length();
                            sincronizarController = new TblSincronizarDao(self);
                            if(rowsCount != total_de_itens){
                                TblSincronizar t = new TblSincronizar();
                                Map<String,String> eventos = null;
                                for (int poss = 0; poss < total_de_itens; poss++) {
                                    JSONObject c = new JSONObject(array_data.get(poss).toString());
                                    String uuid = c.getString(Constante.WS_PCO_GRF_MV_KEY);
                                    String status = c.getString(Constante.WS_PCO_GRF_MV_MV_STATUS);
                                    String time = c.getString(Constante.WS_PCO_GRF_MV_MT_TIME);
                                    eventos=new HashMap<>();
                                    eventos.put("uuid",uuid);
                                    eventos.put("status",status);
                                    eventos.put("time",time);
                                    eventos.put("dt_busca",String.format("%s-%s",currenDate_busca,USER_ATIVO));
                                    eventos.put("structs",array_data.get(poss).toString());
                                    if( ((long)operationsModelAcess.insert(eventos)) !=0){
                                        ids.append(uuid);
                                    }
                                    t.setCodigo_mv(uuid);
                                    String is_register = (String) sincronizarController.getBy(t);
                                    if (is_register.trim().isEmpty()) {
                                        t.setCodigo_mv(uuid);
                                        t.setDt_create(time);
                                        t.setStatus(status);
                                        sincronizarController.insert(t);
                                    }

                                }
                                lockRecordsInApi(tmpUser[0],ids.toString());
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {

                    }
                    notprocessing=false;
                    getOperationsList();
                }
                invalidateOptionsMenu();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                notprocessing=false;
                invalidateOptionsMenu();
                getOperationsList();
            }
        });
    }
    private void lockRecordsInApi(String user, String ids) {
        new ApiPco().blockDownloadedRecord().blockDownloadedRecord(jwt_token,jwt_device,ids,user).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        invalidateOptionsMenu();
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        invalidateOptionsMenu();

                    }
                });
    }
    @Override
    protected void initActionGui() {
        super.initActionGui();

        onClickType = new onClickType() {
            @Override
            public void onCustomClick(String type, Object object) {
                ativo_ = false;
                for (Thread t : times) {
                    try {
                        t.interrupt();
                    } catch (Exception e) {
                    }
                }
                times.clear();
                final Movimentos movimento = (Movimentos) object;
                sincronizarController = new TblSincronizarDao(self);
                List<String> byTimeCronometro = (List<String>) sincronizarController.getByTimeCronometroUuid(movimento.getMv_codigo());
                String[] split = byTimeCronometro.get(0).split(",");
                String time=split[2];

                //21
                if (type.equals("OPEN")) {
                    Intent start_movimento = null;
                    if (movimento.getMv_startus().equals("A")) {
                        start_movimento = new Intent(self, start_movimento.class);
                    } else if (movimento.getMv_startus().equals("T") || movimento.getMv_startus().equals("P")) {
                        start_movimento = new Intent(self, grafica_em_transporte.class);
                    } else if (movimento.getMv_startus().equals("E")) {
                        start_movimento = new Intent(self, grafica_finalizar.class);
                    }
                    start_movimento.putExtra("id_use", USER_ATIVO);
                    start_movimento.putExtra("operacao", movimento);
                    //21
                    startActivity(start_movimento);
                    finish();

                } else if (type.equals("PAUSE")) {
                    int update = (int) operationsModelAcess.operationsPaused(movimento.getMv_codigo(), "P",time);
                    if(update !=0){
                        TblSincronizar t = new TblSincronizar();
                        t.setCodigo_mv(movimento.getMv_codigo());
                        t.setDt_create(time);
                        t.setStatus("P");
                        if(sincronizarController.updateStatus(t) > 0){
                            operationsModelAcess.operationsUpdateNotSynchronized(movimento.getMv_codigo());

                        }

                    }
                } else if (type.equals("CONTINUE")) {
                    movimento.setMv_time_process(time);
                    int update = (int) operationsModelAcess.operationsPaused(movimento.getMv_codigo(), "T",time);
                    if(update !=0){
                        TblSincronizar t = new TblSincronizar();
                        t.setCodigo_mv(movimento.getMv_codigo());
                        t.setDt_create(time);
                        t.setStatus("T");
                        if(sincronizarController.updateStatus(t) > 0){
                            operationsModelAcess.operationsUpdateNotSynchronized(movimento.getMv_codigo());
                        }


                    }



                }
              getOperationsList();
           }
        };

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                //datePickerDialog.setAccentColor(Color.parseColor("#009688"));
                datePickerDialog.setTitle(getString(R.string.str_title_datepicker));
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
                btnImg.callOnClick();
            }
        });
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(self,"Processando...!",Toast.LENGTH_SHORT).show();
                int rows = rowsGetOperationsInBase();
                checkAvailableOperation();
                getOperationsList();
            }
        });
       set_display_actions();
    }

    private void getOperationsList() {

        sincronizarController = new TblSincronizarDao(self);
        TblSincronizar t = new TblSincronizar();

        movimentos.clear();
        mRecyclerView.setAdapter(null);
        mRecyclerView.setVisibility(View.VISIBLE);
        grf_txt_empty.setVisibility(View.VISIBLE);
        List<Movimentos> result = (List<Movimentos>) operationsModelAcess.listOperationByStatusAndDate(String.format("%s-%s",currenDate_busca,USER_ATIVO), statusSelecionado);
        if(result.size()==0){
            set_display(false, true);
        }else{
            adapter = new AdapterAssociated(self, result, onClickType);
            ativo_ = true;
            mRecyclerView.setAdapter(adapter);
            set_display(true, false);
            for(Movimentos movimento : result){
                t.setCodigo_mv(movimento.getMv_codigo());
                String is_register = (String) sincronizarController.getBy(t);
                if (is_register.trim().isEmpty()) {
                    t.setCodigo_mv(movimento.getMv_codigo());
                    t.setDt_create(movimento.getMv_time_process());
                    t.setStatus(movimento.getMv_startus());
                    sincronizarController.insert(t);
                } else {
                    t.setCodigo_mv(movimento.getMv_codigo());
                    t.setDt_create(movimento.getMv_time_process());
                    t.setStatus(movimento.getMv_startus());
                    sincronizarController.update(t);
                }
            }

        }




    }

    private void set_display_actions() {
        grf_select_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> array_spinner = getStatusSelecao();
                if (parent.getItemAtPosition(position).toString().equals(array_spinner.get(0))) {
                    statusSelecionado = "A";
                } else if (parent.getItemAtPosition(position).toString().equals(array_spinner.get(1))) {
                    statusSelecionado = "T";
                } else if (parent.getItemAtPosition(position).toString().equals(array_spinner.get(2))) {
                    statusSelecionado = "E";
                } else if (parent.getItemAtPosition(position).toString().equals(array_spinner.get(3))) {
                    statusSelecionado = "P";
                } else {
                    statusSelecionado = "G";
                }
                wsConfigCotroller.getBy(new WsConfig(Constante.ULT_STATUS_SELECIONADO));
                wsConfigCotroller.update(new WsConfig(Constante.ULT_STATUS_SELECIONADO, statusSelecionado));
                System.out.println("##-status " + statusSelecionado);
                // initializeData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter_tipo.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Acoões do Menu
        if (item.getItemId() == R.id.active_stop_rtc) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Const.FINALIZA_APP);
            builder.setCancelable(true);
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(movimentos_list_grf.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    if(barProgressDialog !=null){
                        barProgressDialog.dismiss();
                        barProgressDialog=null;

                    }
                    finish();
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (item.getItemId() == R.id.action_refresh) {
            //Check e traz as Operações..
        }
        return true;
    }
    private void initServicGrafica() {
        String serviceIsActive = (String) wsConfigCotroller.getBy(new WsConfig(Constante.SERVICE_IS_ACTIVE));
        if (serviceIsActive.trim().isEmpty() || serviceIsActive.trim().equals("0")) {
            intentService = new Intent(self, ServiceAppGrafica.class);
            startService(intentService);
            wsConfigCotroller.update(new WsConfig(Constante.SERVICE_IS_ACTIVE, "1"));
        }
        currenDate = DateUtils.currentDateOnly();
        currenDate_busca = currenDate.replaceAll("-", "|");
        currenDate_busca = (String) wsConfigCotroller.getBy(new WsConfig(Constante.ULT_DATA_SELECIONADA));
        if (currenDate_busca.trim().isEmpty()) {
            wsConfigCotroller.insert(new WsConfig(Constante.ULT_DATA_SELECIONADA, currenDate.replaceAll("-", "|")));
        }
        String tmpSelecao = (String) wsConfigCotroller.getBy(new WsConfig(Constante.ULT_STATUS_SELECIONADO));
        if (currenDate_busca.trim().isEmpty()) {
            wsConfigCotroller.insert(new WsConfig(Constante.ULT_STATUS_SELECIONADO, "T"));
            statusSelecionado = "T";
        } else {
            wsConfigCotroller.update(new WsConfig(Constante.ULT_STATUS_SELECIONADO, tmpSelecao));
            statusSelecionado = tmpSelecao;
        }
    }
    private List<String> getStatusSelecao() {
        List<String> tipos = new ArrayList<>();
        tipos.add("Abertos");
        tipos.add("Em Transporte");
        tipos.add("Entregues");
        tipos.add("Pausadas");
        tipos.add("Todos");
        return tipos;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        grf_select_option = (Spinner) findViewById(R.id.grf_select_option);
        adapter_tipo = new ArrayAdapter(movimentos_list_grf.this,
                android.R.layout.simple_spinner_item, getStatusSelecao());
        grf_select_option.setAdapter(adapter_tipo);
        int pos = setSpinner("Todos");
        grf_select_option.setSelection(pos);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_grf);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(self);
        mRecyclerView.setLayoutManager(llm);
        date_atual = new Date();
        Calendar cal = Calendar.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar_login_rtc);
        this.setTitle(R.string.still_pco_title_menu);
        setSupportActionBar(toolbar);
        btnDate = (Button) findViewById(R.id.btnDate_gf);
        btnImg = (ImageView) findViewById(R.id.btnDate_gf_img);
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = DatePickerDialog.newInstance(movimentos_list_grf.this, Year, Month, Day);
        grf_txt_empty = (TextView) findViewById(R.id.grf_txt_empty);
    }


    private void set_display(boolean list, boolean msn) {
        if (!list && msn) {
            mRecyclerView.setAdapter(null);
            mRecyclerView.setVisibility(View.INVISIBLE);
            grf_txt_empty.setVisibility(View.VISIBLE);
        } else {
            grf_txt_empty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.current_place_menu, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rtc_app, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem status = menu.findItem(R.id.menu_status_server);
           if (!notprocessing) {
                status.setIcon( getResources().getDrawable( R.drawable.ic_launcher_foreground ) );
                status.setVisible(true);
            } else {
                status.setVisible(false);
            }

        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = String.valueOf(monthOfYear + 1);
        currenDate = year + "-" + month + "-" + dayOfMonth;
        currenDate_busca = year + "|" + month + "|" + dayOfMonth;
        btnDate.setText(DateUtils.convertDatetimeStringInDate(year + "-" + month + "-" + dayOfMonth + " 00:00:00"));
        wsConfigCotroller.update(new WsConfig(Constante.ULT_DATA_SELECIONADA, currenDate_busca));
        // initializeData();
    }

    int setSpinner(String value) {
        int pos = 0;
        if (value != null) {
            List<String> opcoes = getStatusSelecao();
            int qtItens = opcoes.size();
            for (int i = 0; i < qtItens; i++) {
                if (opcoes.get(i).equals(value.trim())) {
                    return i;
                }
            }

        }
        return pos;
    }
    @Override
    protected void onResume() {
        super.onResume();
        wsConfigCotroller = new wsConfig(self);
        jwt_token = (String) wsConfigCotroller.getBy(new WsConfig(Constante.TOKEN_JWT));
        jwt_device = (String) wsConfigCotroller.getBy(new WsConfig(Constante.DEVICE_ACESS));
        String dataSelecionada = (String) wsConfigCotroller.getBy(new WsConfig(Constante.ULT_DATA_SELECIONADA));
        USER_ATIVO = (String) wsConfigCotroller.getBy(new WsConfig(Constante.USER_ATIVO));
        getOperationsList();
    }
    /////////////////////
    private List<Thread> times = new ArrayList<>();
    @Override
    public void processando(Context context, String title, String msn) {
        try{

            barProgressDialog = new ProgressDialog(self);
            barProgressDialog.setTitle(title);
            barProgressDialog.setMessage(msn);
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
            barProgressDialog.setCancelable(false);
            barProgressDialog.show();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void messagenBox(Context context, String Titulo, String menssagen, boolean cancelar) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //define o titulo
        builder.setTitle(Titulo);
        //define a mensagem
        builder.setMessage(menssagen);
        //define um botão como positivo
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText( self, "positivo=" + arg1, Toast.LENGTH_SHORT ).show();
            }
        });
        if (cancelar) {
            //define um botão como negativo.
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //Toast.makeText( self, "negativo=" + arg1, Toast.LENGTH_SHORT ).show();
                }
            });
        }
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
    public class AdapterAssociated extends RecyclerView.Adapter<AdapterAssociated.Shopper> {
        private List<Movimentos> movimentos;
        private onClickType mOnClickedAndInteractingWithEvents;
        public Context mctx;
        private TblSincronizarDao sincronizarController;
        public Boolean ativo = true;
        public AdapterAssociated(Context self, List<Movimentos> objects, onClickType listener) {
            this.mctx = self;
            this.movimentos = objects;
            mOnClickedAndInteractingWithEvents = listener;
            sincronizarController = new TblSincronizarDao(mctx);
        }
        @NonNull
        @Override
        public Shopper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_my_movimentos, parent, false);
            return new Shopper(v);
        }
        @SuppressLint("NewApi")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final Shopper holder, final int i) {
            final int poss = i;
            try {
                holder.grf_txt_qt__entregue.setVisibility(View.GONE);
                holder.grf_txt_qt__entregue_label.setVisibility(View.GONE);
                holder.grf_txt_operacao.setText("OPERAÇAO : " + movimentos.get(i).getMv_codigo());
                holder.grf_txt_cl_empresa.setText("Client/Empresa : " + movimentos.get(i).getEm_nome() + "(" + movimentos.get(i).getCl_nome() + ")");
                holder.grf_txt_material_formato.setText("Material: " + movimentos.get(i).getMt_nome());
                holder.grf_txt_qt_a_retirar.setText(movimentos.get(i).getMv_qt_retirar());
                holder.grf_txt_data_creat.setText(movimentos.get(i).getMv_create_at());
                if (movimentos.get(i).getMv_startus().equals("A")) {
                    holder.grf_txt_status.setText("PENDENTE");
                    holder.grf_lnl_time.setVisibility(View.GONE);
                } else if (movimentos.get(i).getMv_startus().equals("T")) {
                    holder.grf_txt_status.setTextColor(Color.GREEN);
                    holder.grf_txt_status.setText("TRANSPORTE");
                    holder.grf_lnl_time.setVisibility(View.VISIBLE);
                    holder.grf_txt_m_btn_pause_continue.setText("PAUSAR");
                    holder.grf_m_img_display_pause_continue.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                } else if (movimentos.get(i).getMv_startus().equals("P")) {
                    TblSincronizar t = new TblSincronizar();
                    t.setCodigo_mv(movimentos.get(i).getMv_codigo());
                    Object byTime1 = sincronizarController.getByTime(t);
                    String timeDisplay = (String) byTime1;
                    holder.grf_txt_m_time_display.setText(timeDisplay);
                    //ic_pause_circle_filled_black  ic_play_circle_filled_black_24dp
                    holder.grf_txt_m_btn_pause_continue.setText("CONTINUAR");
                    holder.grf_m_img_display_pause_continue.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    holder.grf_txt_status.setTextColor(Color.RED);
                    holder.grf_txt_status.setText("PAUSADO");
                    holder.grf_lnl_time.setVisibility(View.VISIBLE);
                } else {
                    holder.grf_txt_status.setTextColor(Color.BLUE);
                    holder.grf_txt_status.setText("ENTREGUE");
                    holder.grf_lnl_time.setVisibility(View.GONE);
                    if (!movimentos.get(i).getMv_qt_entregue().trim().isEmpty()) {
                        holder.grf_txt_qt__entregue.setVisibility(View.VISIBLE);
                        holder.grf_txt_qt__entregue_label.setVisibility(View.VISIBLE);
                        holder.grf_txt_qt__entregue.setText(movimentos.get(i).getMv_qt_entregue());
                    }
                }
                if (movimentos.get(i).getMv_startus().equals("T")) {
                    holder.grf_txt_m_time_display.setText("Processando..");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public int getItemCount() {
            return movimentos.size();
        }
        public class Shopper extends RecyclerView.ViewHolder {
            private TextView grf_txt_operacao;
            private TextView grf_txt_cl_empresa;
            private TextView grf_txt_material_formato;
            private TextView grf_txt_qt_a_retirar;
            private TextView grf_txt_status;
            private TextView grf_txt_data_creat;
            private TextView grf_txt_qt__entregue;
            private TextView grf_txt_qt__entregue_label;
            private TextView grf_txt_m_time_display;
            private CardView grf_m_cdv_time;
            private LinearLayout grf_lnl_time,lnl_detalhes;
            private Button grf_txt_m_btn_pause_continue;
            private ImageView grf_m_img_display_pause_continue;
            public Shopper(final View itemView) {
                super(itemView);
                grf_txt_operacao = (TextView) itemView.findViewById(R.id.grf_txt_operacao);
                grf_txt_cl_empresa = (TextView) itemView.findViewById(R.id.grf_txt_cl_empresa);
                grf_txt_material_formato = (TextView) itemView.findViewById(R.id.grf_txt_material_formato);
                grf_txt_qt_a_retirar = (TextView) itemView.findViewById(R.id.grf_txt_qt_a_retirar);
                grf_txt_status = (TextView) itemView.findViewById(R.id.grf_txt_status);
                grf_txt_data_creat = (TextView) itemView.findViewById(R.id.grf_txt_data_creat);
                grf_txt_qt__entregue = (TextView) itemView.findViewById(R.id.grf_txt_qt__entregue);
                grf_txt_qt__entregue_label = (TextView) itemView.findViewById(R.id.grf_txt_qt__entregue_label);
                grf_txt_m_time_display = (TextView) itemView.findViewById(R.id.grf_txt_m_time_display);
                grf_m_cdv_time = (CardView) itemView.findViewById(R.id.grf_m_cdv_time);
                grf_lnl_time = (LinearLayout) itemView.findViewById(R.id.grf_lnl_time);
                grf_txt_m_btn_pause_continue = (Button) itemView.findViewById(R.id.grf_txt_m_btn_pause_continue);
                grf_m_img_display_pause_continue = (ImageView) itemView.findViewById(R.id.grf_m_img_display_pause_continue);
                lnl_detalhes=(LinearLayout)itemView.findViewById(R.id.lnl_detalhes);
                grf_txt_m_btn_pause_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ativo = false;
                        times.clear();
                        sincronizarController = new TblSincronizarDao(mctx);
                        TblSincronizar t = new TblSincronizar();
                        t.setCodigo_mv(movimentos.get(getLayoutPosition()).getMv_codigo());
                        Object retirada = sincronizarController.getBy(t);
                        String statusOperation = ((String) retirada);
                        String[] camposOperation = statusOperation.split(",");
                        ativo = false;
                        for (Thread time : times) {
                            try {
                                time.interrupt();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        times.clear();
                        if (camposOperation[3].equals("P")) {
                            mOnClickedAndInteractingWithEvents.
                                    onCustomClick("CONTINUE", movimentos.get(getLayoutPosition()));
                        } else {
                            mOnClickedAndInteractingWithEvents.onCustomClick("PAUSE", movimentos.get(getLayoutPosition()));
                        }
                        grf_txt_operacao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                });
                grf_txt_operacao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lnl_detalhes.callOnClick();
                    }
                });
                lnl_detalhes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ativo = false;
                                for (Thread time : times) {
                                    try {
                                        time.interrupt();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                times.clear();
                                mOnClickedAndInteractingWithEvents.onCustomClick("OPEN",movimentos.get(getLayoutPosition()));
                            }
                        });
            }
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        processing=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        processing=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        processing=false;
    }




}
