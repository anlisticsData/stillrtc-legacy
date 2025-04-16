    package br.com.stilldistribuidora.pco.ui;

    import android.app.AlertDialog;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    //import android.icu.text.SimpleDateFormat;
    import java.text.DateFormat;
    import java.text.SimpleDateFormat;
    import android.os.Build;
    import android.os.Environment;
    import androidx.annotation.RequiresApi;
    import androidx.appcompat.app.AppCompatActivity;
    import android.os.Bundle;
    import androidx.appcompat.widget.Toolbar;
    import android.util.Base64;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.ByteArrayOutputStream;
    import java.util.Calendar;
    import java.util.Date;

    import br.com.stilldistribuidora.pco.Interface.iGui;
    import br.com.stilldistribuidora.pco.config.Constante;
    import br.com.stilldistribuidora.pco.db.dao.OperationsModelAcess;
    import br.com.stilldistribuidora.pco.db.dao.TblSincronizarDao;
    import br.com.stilldistribuidora.pco.db.model.Movimentos;
    import br.com.stilldistribuidora.pco.db.model.TblSincronizar;
    import br.com.stilldistribuidora.pco.db.model.WsConfig;
    import br.com.stilldistribuidora.stillrtc.R;
    import br.com.stilldistribuidora.stillrtc.ui.activities.LoginActivity;
    import br.com.stilldistribuidora.stillrtc.utils.Const;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public class start_movimento extends BaseActivity implements iGui {

        private String USER_ATIVO;
        private SimpleDateFormat dateFormat;
        private Date date_atual;
        private TextView str_txt_data_atual;
        private Movimentos operacao;
        private EditText grf_mv_txt_st_material;
        private EditText grf_mv_txt_st_codigo;
        private EditText grf_mv_txt_st_empresa_cliente;
        private EditText grf_mv_txt_st_versao;
        private EditText grf_mv_txt_st_quantidade;
        private EditText grf_mv_txt_st_a_retirar;
        private Button grf_mv_btn_st_copy_qt;
        private Button grf_mv_btn_st_start;
        private String Autorizacao="0";
        private int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2019;
        private String jwt_token="";
        private String jwt_device="";
        private TblSincronizarDao sincronizadoController;
        private ProgressDialog barProgressDialog=null;
        private OperationsModelAcess  operationsModelAcess;
        private TblSincronizarDao sincronizarController;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_start_movimento );
            dateFormat= new SimpleDateFormat( "dd/MM/yyyy HH:mm" );
            sincronizadoController = new TblSincronizarDao(self);
            jwt_token=(String)wsConfigCotroller.getBy( new WsConfig( Constante.TOKEN_JWT ) );
            jwt_device=(String)wsConfigCotroller.getBy( new WsConfig( Constante.DEVICE_ACESS ) );
            USER_ATIVO = (String) wsConfigCotroller.getBy( new WsConfig( Constante.USER_ATIVO ) );
            initComponents();
            date_atual=new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date_atual);
            Intent intent = getIntent();
            operacao = (Movimentos) intent.getSerializableExtra("operacao");
            setDadoDaOperacao();
            initActionGui();

            operationsModelAcess= new OperationsModelAcess(self);
            sincronizarController = new TblSincronizarDao(self);


        }

        private void setDadoDaOperacao() {
            barProgressDialog = new ProgressDialog(self);
            barProgressDialog.setTitle(getString(R.string.grf_msn_systema_alert));
            barProgressDialog.setMessage(getString(R.string.sys_processando_agarde));
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
            barProgressDialog.setCancelable(false);
            barProgressDialog.show();
            grf_mv_txt_st_codigo.setText(operacao.getMv_codigo());
            grf_mv_txt_st_empresa_cliente.setText(operacao.getEm_nome() + " (" + operacao.getCl_nome() + ") ");
            grf_mv_txt_st_material.setText(operacao.getMt_nome() + " (" + operacao.getMt_formato() + ") ");
            String vm_versao = operacao.getVm_versao();
            String mv_qt_retirar = operacao.getMv_qt_retirar();
            grf_mv_txt_st_versao.setText(vm_versao);
            grf_mv_txt_st_quantidade.setText(mv_qt_retirar);
            str_txt_data_atual.setText(  dateFormat.format(date_atual));
            processando=false;
            barProgressDialog.cancel();
            barProgressDialog.dismiss();
        }


        @Override
        protected void initComponents() {
            super.initComponents();
            this.toolbar = (Toolbar) findViewById( R.id.toolbar_login_rtc );
            this.setTitle( R.string.still_pco_title_menu );
            setSupportActionBar( this.toolbar );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true ); //Mostrar o botão
            getSupportActionBar().setHomeButtonEnabled( true );      //Ativar o botão

            grf_mv_txt_st_material =  (EditText) findViewById(R.id.grf_mv_txt_st_material);
            grf_mv_txt_st_codigo = (EditText) findViewById(R.id.grf_mv_txt_st_codigo);
            grf_mv_txt_st_empresa_cliente =  (EditText) findViewById(R.id.grf_mv_txt_st_empresa_cliente);
            grf_mv_txt_st_versao  = (EditText) findViewById(R.id.grf_mv_txt_st_versao);
            grf_mv_txt_st_quantidade  = (EditText) findViewById(R.id.grf_mv_txt_st_quantidade);
            grf_mv_txt_st_a_retirar  = (EditText) findViewById(R.id.grf_mv_txt_st_a_retirar);
            grf_mv_btn_st_copy_qt  = (Button) findViewById(R.id.grf_mv_btn_st_copy_qt);
            grf_mv_btn_st_start  = (Button) findViewById(R.id.grf_mv_btn_st_start);
            str_txt_data_atual=(TextView)findViewById(R.id.str_txt_data_atual);






        }

        @Override
        protected void initActionGui() {
            super.initActionGui();
            /*Botao de Copiar Valor*/
            grf_mv_btn_st_copy_qt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grf_mv_txt_st_a_retirar.setText(operacao.getMv_qt_retirar());
                }
            });
            /*Botao de Copiar Valor*/


            /*Botao de Copiar Start Operação*/
            grf_mv_btn_st_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(true) {
                      start_movimento_retirada();
                    }else{
                      onBackPressed();
                   }

                }
            });
            /*Botao de Copiar Start Operação*/


        }


        private void start_movimento_retirada() {
            if (true) {
                String quantidade = grf_mv_txt_st_a_retirar.getText().toString();
                if (quantidade.trim().isEmpty() || quantidade.trim().equals( "0" )) {
                    alert_comun(getString( R.string.grf_invalido));
                    return;
                }
                Date dataAtual = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dataFormatada = dateFormat.format(dataAtual);

                /*Faz a inicialização da Operação*/
                final ProgressDialog barProgressDialog = new ProgressDialog(self);
                barProgressDialog.setTitle(getString(R.string.grf_msn_systema_alert));
                barProgressDialog.setMessage(getString(R.string.create_retirada_grafica));
                barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                barProgressDialog.setCancelable(false);
                barProgressDialog.show();
                String status="T";
                int update = (int) operationsModelAcess.operationsStartOperations(operacao.getMv_codigo(), status,quantidade,dataFormatada);
                if(update !=0){
                    TblSincronizar t = new TblSincronizar();
                    t.setCodigo_mv(operacao.getMv_codigo());
                    t.setStatus(status);
                    if(sincronizarController.updateStatus(t) > 0){
                        operationsModelAcess.operationsUpdateNotSynchronized(operacao.getMv_codigo());
                    }

                    Intent movimento= new Intent(self,grafica_em_transporte.class);
                    movimento.putExtra("id_use",USER_ATIVO);
                    movimento.putExtra("operacao",operacao);
                    TblSincronizar operacaoSincronizada = new TblSincronizar();
                    operacaoSincronizada.setCodigo_mv(operacao.getMv_codigo());
                    operacaoSincronizada.setDt_create(String.valueOf("00:00:00"));
                    operacaoSincronizada.setStatus(status);
                    String op =(String)sincronizadoController.getBy(operacaoSincronizada);
                    if(op.isEmpty()){
                        if( sincronizadoController.insert(operacaoSincronizada) > 0){
                            operationsModelAcess.operationsUpdateNotSynchronized(operacao.getMv_codigo());
                        }
                    }else{

                        if(sincronizadoController.update(operacaoSincronizada) > 0){
                            operationsModelAcess.operationsUpdateNotSynchronized(operacao.getMv_codigo());
                        }

                    }
                    barProgressDialog.cancel();
                    barProgressDialog.dismiss();
                    startActivity(movimento);
                    finish();
                }
                /*Faz a inicialização da Operação*/
            } else {
                Toast.makeText(self, "Falhar  ao Iniciar Operação..!(Verifique a Sua Conexção..!)", Toast.LENGTH_SHORT).show();
            }
        }
        private void Autorize_retirada_valor_diferente(JSONArray obj) {
           try {
                 JSONObject c = obj.getJSONObject(0);
                //Cria o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //define o titulo
                builder.setTitle("Autorização");
                //define a mensagem
                builder.setMessage(c.getString("msn"));
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //    Toast.makeText(self, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                        Autorizacao="1";
                        start_movimento_retirada();
                    }
                });
                //define um botão como negativo.
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                //cria o AlertDialog
                alerta = builder.create();
                //Exibe
                alerta.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void finish() {
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
            switch (item.getItemId()) {
                case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                    startActivity( new Intent( self, movimentos_list_grf.class ) );  //O efeito ao ser pressionado do botão (no caso abre a activity)
                    finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                    break;
                //Acoões do Menu
                case R.id.active_stop_rtc:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(Const.FINALIZA_APP);
                    builder.setCancelable(true);
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(self, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
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

                    break;




                default:
                    break;
            }
            return true;
        }


        @Override
        public void onBackPressed() {
            //super.onBackPressed();
            startActivity(new Intent(self,movimentos_list_grf.class));
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
           /* if (!isConect) {
                status.setIcon( getResources().getDrawable( R.drawable.ic_signal_wifi_off_black_no_conect_24dp ) );
            } else {
                status.setIcon( getResources().getDrawable( R.drawable.ic_wifi_black_conect_32dp ) );
            }*/

            return true;
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            System.gc(); // garbage colector
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 3;
                        Bitmap imageBitmap = BitmapFactory.decodeFile( Environment.getExternalStorageDirectory() + "/arquivo.jpg", options);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        boolean validaCompressao = imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                        byte[] fotoBinario = outputStream.toByteArray();
                        String encodedImage = Base64.encodeToString(fotoBinario, Base64.DEFAULT);
                        //   ibt_foto.setImageBitmap(imageBitmap); // ImageButton, seto a foto como imagem do botão

                        boolean isImageTaken = true;
                    } catch (Exception e) {
                        Toast.makeText(this, "Picture Not taken", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Picture was not taken 1 ", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(this, "Picture was not taken 2 ", Toast.LENGTH_SHORT);
                }
            }

        }






        @Override
        protected void onResume() {
            super.onResume();
            jwt_token=(String)wsConfigCotroller.getBy( new WsConfig( Constante.TOKEN_JWT ) );
            jwt_device=(String)wsConfigCotroller.getBy( new WsConfig( Constante.DEVICE_ACESS ) );
            USER_ATIVO = (String) wsConfigCotroller.getBy( new WsConfig( Constante.USER_ATIVO ) );

        }

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
    }
