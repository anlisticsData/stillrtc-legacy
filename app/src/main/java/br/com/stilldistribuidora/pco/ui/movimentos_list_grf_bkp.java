    package br.com.stilldistribuidora.pco.ui;


    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.icu.text.SimpleDateFormat;
    import android.os.Build;
    import android.os.Bundle;
    import androidx.annotation.RequiresApi;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.appcompat.widget.Toolbar;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.Spinner;
    import android.widget.TextView;

    import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.List;

    import br.com.stilldistribuidora.pco.Interface.ClickRecyclerView_Interface;
    import br.com.stilldistribuidora.pco.adapter.MovimentosViewHolder;
    import br.com.stilldistribuidora.pco.api.ApiPco;
    import br.com.stilldistribuidora.pco.config.Constante;
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
    import okhttp3.ResponseBody;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public class movimentos_list_grf_bkp extends BaseActivity implements DatePickerDialog.OnDateSetListener {
        private static String USER_ATIVO = "";
        private SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm" );
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
        private MovimentosViewHolder adapter = null;
        private LinearLayoutManager llm;
        private TextView grf_txt_empty;
        private String jwt_token="";
        private String jwt_device="";
        private TblSincronizarDao sincronizarController;
        private ImageView btnImg;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_movimentos_list_grf );
            //  processando("Processando","Aguarde...(Checando Op)");
            initServicGrafica();
            initComponents();
            //isConected();
            jwt_token=(String)wsConfigCotroller.getBy( new WsConfig( Constante.TOKEN_JWT ) );
            jwt_device=(String)wsConfigCotroller.getBy( new WsConfig( Constante.DEVICE_ACESS ) );
            String dataSelecionada = (String) wsConfigCotroller.getBy( new WsConfig( Constante.ULT_DATA_SELECIONADA ) );
            USER_ATIVO = (String) wsConfigCotroller.getBy( new WsConfig( Constante.USER_ATIVO ) );
            String split[] = new String[3];
            String replace = dataSelecionada.replace( "|", ";" );
            String[] arrOfStr = replace.split( ";" );
            btnDate.setText( DateUtils.convertDatetimeStringInDate( arrOfStr[0] + "-" + arrOfStr[1] + "-" + arrOfStr[2] + " 00:00:00" ) );
            initActionGui();
            initializeData();
        }

        @Override
        protected void initActionGui() {
            super.initActionGui();
            btnDate.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePickerDialog.setThemeDark( false );
                    datePickerDialog.showYearPickerFirst( false );
                    //datePickerDialog.setAccentColor(Color.parseColor("#009688"));
                    datePickerDialog.setTitle( getString( R.string.str_title_datepicker ) );
                    datePickerDialog.show( getFragmentManager(), "DatePickerDialog" );
                }
            } );


            btnImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initializeData();
                }
            });
            grf_select_option.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    List<String> array_spinner = getStatusSelecao();
                    if (parent.getItemAtPosition( position ).toString().equals( array_spinner.get( 0 ) )) {
                        statusSelecionado = "A";
                    } else if (parent.getItemAtPosition( position ).toString().equals( array_spinner.get( 1 ) )) {
                        statusSelecionado = "T";
                    } else if (parent.getItemAtPosition( position ).toString().equals( array_spinner.get( 2 ) )) {
                        statusSelecionado = "E";
                    } else if (parent.getItemAtPosition( position ).toString().equals( array_spinner.get( 3 ) )) {
                        statusSelecionado = "P";
                    } else {
                        statusSelecionado = "G";
                    }

                   // initializeData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            } );


        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            //Acoões do Menu
            if (item.getItemId() == R.id.active_stop_rtc) {

                AlertDialog.Builder builder = new AlertDialog.Builder( this );
                builder.setMessage( Const.FINALIZA_APP );
                builder.setCancelable( true );
                builder.setPositiveButton( "Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent( movimentos_list_grf_bkp.this, LoginActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity( intent );
                        finish();

                    }
                } );
                builder.setNegativeButton( "Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                } );
                AlertDialog alert = builder.create();
                alert.show();

            } else if (item.getItemId() == R.id.action_refresh) {


                //Check e traz as Operações..


            }


            return true;
        }


        private void initServicGrafica() {
            String serviceIsActive= (String) wsConfigCotroller.getBy( new WsConfig( Constante.SERVICE_IS_ACTIVE) );
            if (serviceIsActive.trim().isEmpty() || serviceIsActive.trim().equals("0")) {
                intentService = new Intent( self, ServiceAppGrafica.class );
                startService( intentService );
                wsConfigCotroller.update( new WsConfig( Constante.SERVICE_IS_ACTIVE,"1"));

            }

            currenDate = DateUtils.currentDateOnly();
            currenDate_busca = currenDate.replaceAll( "-", "|" );

            currenDate_busca = (String) wsConfigCotroller.getBy( new WsConfig( Constante.ULT_DATA_SELECIONADA ) );
            if (currenDate_busca.trim().isEmpty()) {
                wsConfigCotroller.insert( new WsConfig( Constante.ULT_DATA_SELECIONADA, currenDate.replaceAll( "-", "|" ) ) );
            }

            String tmpSelecao = (String) wsConfigCotroller.getBy( new WsConfig( Constante.ULT_STATUS_SELECIONADO ) );
            if (currenDate_busca.trim().isEmpty()) {
                wsConfigCotroller.insert( new WsConfig( Constante.ULT_STATUS_SELECIONADO, "T" ) );
                statusSelecionado = "T";
            } else {
                wsConfigCotroller.update( new WsConfig( Constante.ULT_STATUS_SELECIONADO, tmpSelecao ) );
                statusSelecionado = tmpSelecao;

            }


        }

        private void isConected() {
          /*  new Thread( new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            invalidateOptionsMenu();
                            Thread.sleep( WAIT_TIME );
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            } ).start();*/


        }


        private List<String> getStatusSelecao() {
            List<String> tipos = new ArrayList<>();
            tipos.add( "Abertos" );
            tipos.add( "Em Transporte" );
            tipos.add( "Entregues" );
            tipos.add( "Pausadas" );
            tipos.add( "Todos" );
            return tipos;
        }

        @Override
        protected void initComponents() {
            super.initComponents();
            grf_select_option = (Spinner) findViewById( R.id.grf_select_option );
            ArrayAdapter adapter_tipo = new ArrayAdapter( movimentos_list_grf_bkp.this,
                    android.R.layout.simple_spinner_item, getStatusSelecao() );

            grf_select_option.setAdapter( adapter_tipo );
            int pos = setSpinner( "Todos" );
            grf_select_option.setSelection( pos );

            mRecyclerView = (RecyclerView) findViewById( R.id.recycler_view_grf );
            mRecyclerView.setHasFixedSize( true );
            llm = new LinearLayoutManager( self );
            mRecyclerView.setLayoutManager( llm );


            date_atual = new Date();
            Calendar cal = Calendar.getInstance();
            toolbar = (Toolbar) findViewById( R.id.toolbar_login_rtc );
            this.setTitle( R.string.still_pco_title_menu );
            setSupportActionBar( toolbar );
            btnDate = (Button) findViewById( R.id.btnDate_gf );
            btnImg=(ImageView)findViewById(R.id.btnDate_gf_img);
            calendar = Calendar.getInstance();
            Year = calendar.get( Calendar.YEAR );
            Month = calendar.get( Calendar.MONTH );
            Day = calendar.get( Calendar.DAY_OF_MONTH );
            Year = calendar.get( Calendar.YEAR );
            Month = calendar.get( Calendar.MONTH );
            Day = calendar.get( Calendar.DAY_OF_MONTH );
            datePickerDialog = DatePickerDialog.newInstance( movimentos_list_grf_bkp.this, Year, Month, Day );

            grf_txt_empty = (TextView) findViewById( R.id.grf_txt_empty );


        }

        private void initializeData() {


            processando=true;
          //  processando(getResources().getString(R.string.sys_busc_operacao), getResources().getString(R.string.sys_processando_agarde) );
            movimentos.clear();

            //mRecyclerView.getRecycledViewPool().clear();
            mRecyclerView.setAdapter( null );
            mRecyclerView.setVisibility( View.VISIBLE );
            grf_txt_empty.setVisibility( View.VISIBLE );
            String[] tmpUser = USER_ATIVO.trim().replace( '|', ';' ).split( ";" );
            new ApiPco().get_material_grafica_data_status().get_material_grafica_data_status( jwt_token,jwt_device,
                    currenDate_busca, tmpUser[0], statusSelecionado ).enqueue( new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    processando = false;
                    if (response.isSuccessful()) {
                        ClickRecyclerView_Interface clicks = new ClickRecyclerView_Interface() {
                            @Override
                            public void onCustomClick(Object object) {
                                Movimentos movimento = (Movimentos) object;
                                //Toast.makeText(self, movimento.getMv_codigo(), Toast.LENGTH_LONG).show();
                                Intent start_movimento = null;
                                if (movimento.getMv_startus().equals( "A" )) {
                                    start_movimento = new Intent( self, start_movimento.class );

                                } else if (movimento.getMv_startus().equals( "T" ) || movimento.getMv_startus().equals( "P" )) {
                                    start_movimento = new Intent( self, grafica_em_transporte.class );
                                } else if (movimento.getMv_startus().equals( "E" )) {
                                     start_movimento = new Intent(self,grafica_finalizar.class);
                                }

                                start_movimento.putExtra( "id_use", USER_ATIVO );
                                start_movimento.putExtra( "operacao", movimento );
                                startActivity( start_movimento );
                                finish();
                            }
                        };


                        ClickRecyclerView_Interface ClickPause = new ClickRecyclerView_Interface() {
                            @Override
                            public void onCustomClick(Object object) {
                                final Movimentos movimento = (Movimentos) object;
                                sincronizarController = new TblSincronizarDao(self);
                                new ApiPco().api().ws_gf_status_operation_set(jwt_token,USER_ATIVO,movimento.getMv_codigo(),"P")
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                processando = false;
                                                if (response.isSuccessful()) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject( response.body().string() );
                                                        String error = jsonObject.getString( "error" );
                                                        String http = jsonObject.getString( "http" );
                                                        if (error.equals( "false" ) && http.equals( "200" )) {
                                                            TblSincronizar t = new TblSincronizar();
                                                            t.setCodigo_mv(movimento.getMv_codigo());
                                                            t.setStatus("P");
                                                            sincronizarController.updateStatus(t);

                                                                startActivity( new Intent( self, movimentos_list_grf_bkp.class ) );  //O efeito ao ser pressionado do botão (no caso abre a activity)
                                                                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem


                                                        }


                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                  }
                                                }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });




                            }
                        };


                        ClickRecyclerView_Interface ClickContinue = new ClickRecyclerView_Interface() {
                            @Override
                            public void onCustomClick(Object object) {
                                final Movimentos movimento = (Movimentos) object;
                                sincronizarController = new TblSincronizarDao(self);
                                new ApiPco().api().ws_gf_status_operation_set(jwt_token,USER_ATIVO,movimento.getMv_codigo(),"T")
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                processando = false;
                                                if (response.isSuccessful()) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject( response.body().string() );
                                                        String error = jsonObject.getString( "error" );
                                                        String http = jsonObject.getString( "http" );
                                                        if (error.equals( "false" ) && http.equals( "200" )) {
                                                            TblSincronizar t = new TblSincronizar();
                                                            t.setCodigo_mv(movimento.getMv_codigo());
                                                            t.setStatus("T");
                                                            sincronizarController.updateStatus(t);

                                                                startActivity( new Intent( self, movimentos_list_grf_bkp.class ) );  //O efeito ao ser pressionado do botão (no caso abre a activity)

                                                            finish();
                                                                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem





                                                        }


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });




                            }
                        };

                       try {
                            JSONObject jsonObject = new JSONObject( response.body().string() );
                            String error = jsonObject.getString( "error" );
                            String http = jsonObject.getString( "http" );
                            if (error.equals( "false" ) && http.equals( "200" )) {
                                String data = jsonObject.getString( "data" );
                                JSONArray array_data = new JSONArray( data );
                                int total_de_itens = array_data.length();
                                sincronizarController = new TblSincronizarDao(self);
                                TblSincronizar t = new TblSincronizar();

                                for (int poss = 0; poss < total_de_itens; poss++) {
                                    JSONObject c = new JSONObject( array_data.get( poss ).toString() );
                                    Movimentos mv = new Movimentos();
                                    mv.setMv_codigo( c.getString( Constante.WS_PCO_GRF_MV_KEY ) );
                                    mv.setMt_codido( c.getString( Constante.WS_PCO_GRF_MV_MT_KEY ) );
                                    mv.setMt_nome( c.getString( Constante.WS_PCO_GRF_MV_MT_NOME ) );
                                    mv.setMt_formato( c.getString( Constante.WS_PCO_GRF_MV_MT_FORMATO ) );
                                    mv.setVm_versao( c.getString( Constante.WS_PCO_GRF_MV_VM_VERSAO ) );
                                    mv.setEm_nome( c.getString( Constante.WS_PCO_GRF_MV_EM_NOME ) );
                                    mv.setEm_img( c.getString( Constante.WS_PCO_GRF_MV_EM_IMG ) );
                                    mv.setCl_codigo( c.getString( Constante.WS_PCO_GRF_MV_CL_KEY ) );
                                    mv.setCl_nome( c.getString( Constante.WS_PCO_GRF_MV_CL_NOME ) );
                                    mv.setMv_qt_retirar( c.getString( Constante.WS_PCO_GRF_MV_QT_RT ) );
                                    mv.setMv_create_at( c.getString( Constante.WS_PCO_GRF_MV_DT_CREATE ) );
                                    mv.setMv_startus( c.getString( Constante.WS_PCO_GRF_MV_MV_STATUS ) );
                                    mv.setMv_dt_retirada( c.getString( Constante.WS_PCO_GRF_MV_DT_RT ) );
                                    mv.setMv_qt_retirada( c.getString( Constante.WS_PCO_GRF_MV_MV_QT_RT ) );
                                    mv.setMv_dt_entrega( c.getString( Constante.WS_PCO_GRF_MV_DT_ENTREGA ) );
                                    mv.setMv_qt_entregue( c.getString( Constante.WS_PCO_GRF_MV_QT_ENTREGE ) );
                                    mv.setMv_time_process(c.getString( Constante.WS_PCO_GRF_MV_MT_TIME )  );

                                    t.setCodigo_mv(mv.getMv_codigo());
                                    String is_register=(String)sincronizarController.getBy(t);
                                    if(is_register.trim().isEmpty()){
                                        t.setCodigo_mv(mv.getMv_codigo());
                                        t.setDt_create(mv.getMv_time_process());
                                        t.setStatus(mv.getMv_startus());
                                        sincronizarController.insert(t);
                                    }else{
                                        t.setCodigo_mv(mv.getMv_codigo());
                                        t.setDt_create(mv.getMv_time_process());
                                        t.setStatus(mv.getMv_startus());
                                        sincronizarController.update(t);

                                    }


                                    movimentos.add( mv );

                                }
                                //Verificar se tem na Base
                                adapter = new MovimentosViewHolder( self, movimentos, clicks,ClickPause,ClickContinue);
                                //mRecyclerView.setAdapter( adapter );
                                //adapter.notifyDataSetChanged();
                                mRecyclerView.setAdapter( adapter );
                                adapter.notifyDataSetChanged();

                                //mRecyclerView.requestFocus();
                                System.out.println("##059 --- "+movimentos.size());

                          /*      runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRecyclerView.setAdapter( adapter );
                                        adapter.notifyDataSetChanged();
                                        mRecyclerView.requestFocus();
                                    }
                                });
*/

                            }

                            if (movimentos.size() == 0) {

                                mRecyclerView.setAdapter( null );
                                mRecyclerView.setVisibility( View.INVISIBLE );
                                grf_txt_empty.setVisibility( View.VISIBLE );
                            } else {
                                grf_txt_empty.setVisibility( View.GONE );
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    processando = false;
                }
            } );

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            //getMenuInflater().inflate(R.menu.current_place_menu, menu);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate( R.menu.menu_rtc_app, menu );
            return true;
        }

        @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
            final MenuItem status = menu.findItem( R.id.menu_status_server );
            /*if (!isConect) {
                status.setIcon( getResources().getDrawable( R.drawable.ic_signal_wifi_off_black_no_conect_24dp ) );
            } else {
                status.setIcon( getResources().getDrawable( R.drawable.ic_wifi_black_conect_32dp ) );
            }*/

            return true;
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            String month = String.valueOf( monthOfYear + 1 );
            currenDate = year + "-" + month + "-" + dayOfMonth;
            currenDate_busca = year + "|" + month + "|" + dayOfMonth;
            btnDate.setText( DateUtils.convertDatetimeStringInDate( year + "-" + month + "-" + dayOfMonth + " 00:00:00" ) );
            wsConfigCotroller.update( new WsConfig( Constante.ULT_DATA_SELECIONADA, currenDate_busca ) );

               // initializeData();

        }

        int setSpinner(String value) {
            int pos = 0;
            if (value != null) {
                List<String> opcoes = getStatusSelecao();
                int qtItens = opcoes.size();
                for (int i = 0; i < qtItens; i++) {
                    if (opcoes.get( i ).equals( value.trim() )) {
                        return i;
                    }
                }

            }

            return pos;


        }


        @Override
        protected void onResume() {
            super.onResume();
            wsConfigCotroller=new wsConfig(self);
            jwt_token=(String)wsConfigCotroller.getBy( new WsConfig( Constante.TOKEN_JWT ) );
            jwt_device=(String)wsConfigCotroller.getBy( new WsConfig( Constante.DEVICE_ACESS ) );
            String dataSelecionada = (String) wsConfigCotroller.getBy( new WsConfig( Constante.ULT_DATA_SELECIONADA ) );
            USER_ATIVO = (String) wsConfigCotroller.getBy( new WsConfig( Constante.USER_ATIVO ) );



        }
    }
