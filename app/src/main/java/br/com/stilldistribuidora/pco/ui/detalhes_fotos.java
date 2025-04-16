        package br.com.stilldistribuidora.pco.ui;
        import android.annotation.SuppressLint;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.os.Build;

        import android.os.HandlerThread;
        import android.os.StrictMode;
        import androidx.annotation.RequiresApi;
        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import androidx.recyclerview.widget.DefaultItemAnimator;
        import androidx.recyclerview.widget.GridLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import androidx.appcompat.widget.Toolbar;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;

        import br.com.stilldistribuidora.pco.Interface.ClickRecyclerView_Interface;
        import br.com.stilldistribuidora.pco.adapter.PicturePcoAdapter;
        import br.com.stilldistribuidora.pco.api.ApiPco;
        import br.com.stilldistribuidora.pco.config.Constante;
        import br.com.stilldistribuidora.pco.db.dao.OperationsModelAcess;
        import br.com.stilldistribuidora.pco.db.dao.PictureDao;
        import br.com.stilldistribuidora.pco.db.dao.TblSincronizarDao;
        import br.com.stilldistribuidora.pco.db.model.Movimentos;
        import br.com.stilldistribuidora.pco.db.model.PictureImageGrafica;
        import br.com.stilldistribuidora.pco.db.model.TblSincronizar;
        import br.com.stilldistribuidora.pco.db.model.WsConfig;
        import br.com.stilldistribuidora.pco.servicos.GpsPco;
        import br.com.stilldistribuidora.stillrtc.R;
        import br.com.stilldistribuidora.stillrtc.ui.activities.LoginActivity;
        import br.com.stilldistribuidora.stillrtc.utils.Const;
        import okhttp3.ResponseBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        public class detalhes_fotos extends BaseActivity {
            private  Boolean ativo=true;
            private String jwt_token,jwt_device,USER_ATIVO;
            private Movimentos operacao;
            private PictureDao wsPictureController;
            private ArrayList<PictureImageGrafica> ar = new ArrayList<>();
            private ArrayList<PictureImageGrafica> all =new ArrayList<>();
            private RecyclerView mRecyclerView;
            private TextView fwb_materia;
            private TextView fwb_versao_formato;
            private TextView fwb_dt_init_dt_fim;
            private TextView fwb_qt_fotos;
            private TextView fwb_qt_retirada;
            private TextView fwb_qt_entrega;
            private GridLayoutManager linearlayoutManager;
            private PicturePcoAdapter adapter;
            private  Intent it;
            private TblSincronizarDao sincronizarController;
            private String timeStartOperation="";
            private GpsPco gps;
            public HandlerThread handlerThread;
            private   TextView fwb_time_display;
            private OperationsModelAcess operationsModelAcess;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_detalhes_fotos);
                self=this;
                it = getIntent();

                sincronizarController = new TblSincronizarDao(self);
                jwt_token=(String)wsConfigCotroller.getBy( new WsConfig( Constante.TOKEN_JWT ) );
                jwt_device=(String)wsConfigCotroller.getBy( new WsConfig( Constante.DEVICE_ACESS ) );
                USER_ATIVO = (String) wsConfigCotroller.getBy( new WsConfig( Constante.USER_ATIVO ) );
                operacao=(Movimentos)it.getSerializableExtra("operacao");
                wsPictureController= new PictureDao(self);
                all = wsPictureController.getAll(operacao.getMv_codigo());
                TblSincronizar operacaoSincronizada = new TblSincronizar();
                operacaoSincronizada.setCodigo_mv(operacao.getMv_codigo());
                final Object byTime = sincronizarController.getByTime(operacaoSincronizada);
                if(all.size() >0){
                    ar=all;
                }
                // instancia o service, GPSTracker gps
                gps = new GpsPco(self);
                initComponents();
                isConected();
                recyclerViewDevelop();
                operationsModelAcess = new OperationsModelAcess(self);
                setInfoOperacao(operacao.getMv_codigo());
                Display display = new Display(self);
                display.execute("");



            }
            @Override
            protected void onDestroy() {
                super.onDestroy();
                ativo=false;
            }

            private void setInfoOperacao(String mv_codigo) {
                List<Movimentos> operation = (List<Movimentos>) operationsModelAcess.findBy(mv_codigo);
                if(operation.size()==0){
                }
                String mt_dt_retirada = operation.get(0).getMv_dt_retirada();
                String mt_dt_entrega =operation.get(0).getMv_dt_entrega();
                String mt_nome=operation.get(0).getMt_nome();
                fwb_materia.setText(mt_nome);
                fwb_versao_formato.setText(operation.get(0).getVm_versao()+" / "+operation.get(0).getMt_formato());
                fwb_dt_init_dt_fim.setText(mt_dt_retirada+"  "+mt_dt_retirada);
                fwb_qt_fotos.setText("Total de Fotos : "+"0");
                getServerImagens(mv_codigo);
            }



            private void getServerImagens(final String mv_codigo) {
                Toast.makeText(self,getString(R.string.sys_processando_agarde),Toast.LENGTH_SHORT).show();
               new Thread(new Runnable() {
                   @Override
                   public void run() {


                       new ApiPco().api().ws_gf_status_operation(jwt_token,USER_ATIVO,mv_codigo).enqueue(new Callback<ResponseBody>() {
                           @RequiresApi(api = Build.VERSION_CODES.O)
                           @Override
                           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                               if (response.isSuccessful()) {
                                   try{
                                       JSONObject jsonObject = new JSONObject( response.body().string() );
                                       String error = jsonObject.getString( "error" );
                                       String http = jsonObject.getString( "http" );
                                       if (error.equals( "false" ) && http.equals( "200" )) {
                                           String data = jsonObject.getString("data");
                                           JSONArray array_data = new JSONArray(data);
                                           JSONObject c = new JSONObject( array_data.get( 0 ).toString() );
                                           String retirada = c.getString("retirada");
                                           JSONObject objRetirada = new JSONObject( retirada);
                                           if(ar.size()==0) {
                                               String fotos = c.getString("fotos");
                                               JSONArray array_datap = new JSONArray(fotos);
                                               for (int i=0;i<array_datap.length();i++){
                                                   String[] dados = array_datap.get(i).toString().split(";");
                                                   String[] coordenadas = dados[2].split(",");
                                                   String[] tmpUser = USER_ATIVO.trim().replace("|", ";").split(";");
                                                   final PictureImageGrafica imagen = new PictureImageGrafica();
                                                   imagen.setDevice(jwt_device);
                                                   imagen.setSincronizado("1");
                                                   if(coordenadas.length>1) {
                                                       imagen.setLoc(coordenadas[0] + "|" + coordenadas[1]);
                                                   }else{
                                                       imagen.setLoc("0|0");
                                                   }
                                                   imagen.setPath_file(ApiPco.BASE_BACKOFFICE+dados[0]);
                                                   imagen.setTb_retirada_gf_codigo(dados[1]);
                                                   imagen.setName_file("");
                                                   imagen.setUser(tmpUser[0]);
                                                   long insert = wsPictureController.insert(imagen);
                                               }
                                               all = wsPictureController.getAll(operacao.getMv_codigo());
                                               recyclerViewDevelop();
                                           }
                                           fwb_qt_fotos.setText("Total de Fotos : "+c.getString("qt_fotos"));
                                       }
                                   }catch (Exception e){
                                       e.printStackTrace();
                                   }
                               }

                           }
                           @Override
                           public void onFailure(Call<ResponseBody> call, Throwable t) {

                           }
                       });

                   }
               }).start();


                

            }
            @SuppressLint("WrongViewCast")
            @Override
            protected void initComponents() {
                super.initComponents();
                toolbar = (Toolbar) findViewById( R.id.toolbar_login_rtc );
                mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                fwb_materia=(TextView)findViewById(R.id.fwb_materia);
                fwb_versao_formato=(TextView)findViewById(R.id.fwb_versao_formato);
                fwb_dt_init_dt_fim=(TextView)findViewById(R.id.fwb_dt_init_dt_fim);
                fwb_qt_fotos=(TextView)findViewById(R.id.fwb_qt_foto);
                fwb_qt_retirada=(TextView)findViewById(R.id.fwb_qt_retirada);
                fwb_qt_entrega=(TextView)findViewById(R.id.fwb_qt_entrega);
                fwb_time_display=(TextView)findViewById(R.id.fwb_time_display);
                this.setTitle( R.string.still_pco_title_menu );
                setSupportActionBar( toolbar );
                getSupportActionBar().setDisplayHomeAsUpEnabled( true ); //Mostrar o botão
                getSupportActionBar().setHomeButtonEnabled( true );      //Ativar o botão
            }
            private void recyclerViewDevelop() {
                ar=all;
                ClickRecyclerView_Interface click=new ClickRecyclerView_Interface() {
                    @Override
                    public void onCustomClick(Object object) {
                    }
                };
                // improve performance if you know that changes in content
                // do not change the size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);
                linearlayoutManager = new GridLayoutManager(this, 4);
                // use a linear layout manager
                mRecyclerView.setLayoutManager(linearlayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                // Create the recyclerViewAdapter
                adapter = new PicturePcoAdapter(this,ar, click);
                mRecyclerView.setAdapter(adapter);
            }
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
                switch (item.getItemId()) {
                    case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                        Intent graphicInAdamento = new Intent(self, grafica_em_transporte.class);
                        graphicInAdamento.putExtra( "id_use", USER_ATIVO );
                        graphicInAdamento.putExtra( "operacao", operacao );
                        ativo=false;
                        startActivity( graphicInAdamento);  //O efeito ao ser pressionado do botão (no caso abre a activity)
                        finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                        finish();
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
                if (!isConect) {
                    status.setIcon( getResources().getDrawable( R.drawable.ic_signal_wifi_off_black_no_conect_24dp ) );
                } else {
                    status.setIcon( getResources().getDrawable( R.drawable.ic_wifi_black_conect_32dp ) );
                }
                return true;
            }
            @SuppressLint("NewApi")
            private Bitmap DownloadImage(String pURL){
                StrictMode.ThreadPolicy vPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(vPolicy);
                InputStream inStream = null;
                Bitmap vBitmap = null;
                try{
                    URL url = new URL(pURL);
                    HttpURLConnection pConnection = (HttpURLConnection)url.openConnection();
                    pConnection.setDoInput(true);
                    pConnection.connect();
                    if(pConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        inStream = pConnection.getInputStream();
                        vBitmap = BitmapFactory.decodeStream(inStream);
                        inStream.close();
                        return vBitmap;
                    }
                }
                catch(Exception ex){
                    Log.e("Exception",ex.toString());
                }
                return null;
            }
            @Override
            protected void onResume() {
                super.onResume();
                jwt_token=(String)wsConfigCotroller.getBy( new WsConfig( Constante.TOKEN_JWT ) );
                jwt_device=(String)wsConfigCotroller.getBy( new WsConfig( Constante.DEVICE_ACESS ) );
                USER_ATIVO = (String) wsConfigCotroller.getBy( new WsConfig( Constante.USER_ATIVO ) );
                sincronizarController = new TblSincronizarDao(self);
            }
            private void isConected() {
                new Thread( new Runnable() {
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

                } ).start();
            }
            private  class  Display extends AsyncTask<String,Void,Void>{
                private final Context context;
                public Display(Context context){
                    this.context=context;
                }
                @Override
                protected Void doInBackground(String... strings) {
                    while (true && ativo){
                        try{
                            System.out.println("##59 ---------------------");
                            try {
                                TblSincronizar t = new TblSincronizar();
                                t.setCodigo_mv(operacao.getMv_codigo());
                                Object byTime1 = sincronizarController.getByTime(t);
                                String timeDisplay = (String) byTime1;
                                System.out.println("C :"+timeDisplay);
                                fwb_time_display.setText(timeDisplay);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Thread.sleep(1 * 1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            }
        }