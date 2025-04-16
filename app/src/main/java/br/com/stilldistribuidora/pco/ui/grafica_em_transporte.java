package br.com.stilldistribuidora.pco.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;


import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import br.com.stilldistribuidora.stillrtc.utils.InternalStorageContentProvider;

@RequiresApi(api = Build.VERSION_CODES.N)
public class grafica_em_transporte extends BaseActivity {
    private static final int CAMERA = 3;
    private Movimentos operacao;
    private String USER_ATIVO = "";
    private SimpleDateFormat dateFormat ;
    private Date date_atual;
    private TextView str_txt_data_atual;
    private EditText grf_mv_txt_st_material;
    private EditText grf_mv_txt_st_codigo;
    private EditText grf_mv_txt_st_empresa_cliente;
    private EditText grf_mv_txt_st_versao;
    private EditText grf_mv_txt_st_quantidade;
    private EditText grf_mv_txt_st_a_retirar;
    private PictureDao wsPictoresCotroller;
    private Button grf__pmv_btn_st_stop1;
    private File arquivoFoto = null;
    private List<Movimentos> movimentos = new ArrayList<>();
    private String jwt_token = "";
    private String jwt_device = "";
    private GpsPco gps;
    private OperationsModelAcess operationsModelAcess;

    private Intent detalhe_foto = null;
    private TblSincronizarDao sincronizarController;
    private String namePath = "StillPCO";


    public grafica_em_transporte() {
        self = this;
        wsPictoresCotroller = new PictureDao(self);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jwt_token = (String) wsConfigCotroller.getBy(new WsConfig(Constante.TOKEN_JWT));
        jwt_device = (String) wsConfigCotroller.getBy(new WsConfig(Constante.DEVICE_ACESS));
        detalhe_foto = new Intent(self, detalhes_fotos.class);
        USER_ATIVO = (String) wsConfigCotroller.getBy(new WsConfig(Constante.USER_ATIVO));
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        InitPermisionGeral();
        operationsModelAcess = new OperationsModelAcess(self);
        sincronizarController = new TblSincronizarDao(self);
        setContentView(R.layout.activity_grafica_em_transporte);
        Intent intent = getIntent();
        operacao = (Movimentos) intent.getSerializableExtra("operacao");
        date_atual = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date_atual);

        initComponents();

        initActionGui();

        initializeData();
        detalhe_foto.putExtra("id_use", USER_ATIVO);
        detalhe_foto.putExtra("operacao", operacao);
    }
    @Override
    protected void initActionGui() {
        super.initActionGui();
        grf__pmv_btn_st_stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processando = false;
                finish_operacao();
                // Toast.makeText( self, "sssss", Toast.LENGTH_LONG ).show();
            }
        });
    }
    private void finish_operacao() {

        Date dataAtual = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String dataFinish = dateFormat.format(dataAtual);
            //Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //define o titulo
            builder.setTitle(getResources().getString(R.string.grf_msn_systema_alert));
            //define a mensagem
            builder.setMessage("Deseja Finalizar Esta Operação..?");
            //define um botão como positivo
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                        final ProgressDialog barProgressDialog = new ProgressDialog(self);
                        barProgressDialog.setTitle(getString(R.string.grf_msn_systema_alert));
                        barProgressDialog.setMessage(getString(R.string.create_retirada_grafica));
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setCancelable(false);
                        barProgressDialog.show();
                        String status = "E";
                        int update = (int) operationsModelAcess.operationsFinishOperations(operacao.getMv_codigo(), status,dataFinish);
                        if(update !=0){
                            TblSincronizar t = new TblSincronizar();
                            t.setCodigo_mv(operacao.getMv_codigo());
                            t.setStatus(status);
                            if(sincronizarController.updateStatus(t) > 0) {

                            }
                            operationsModelAcess.operationsUpdateNotSynchronized(operacao.getMv_codigo());
                            startActivity(new Intent(self,movimentos_list_grf.class));
                            finish();


                        }
                     barProgressDialog.dismiss();

              }
            });
            //define um botão como negativo.
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    alert_comun("Ação Cancelada");
                }
            });
            //cria o AlertDialog
            alerta = builder.create();
            //Exibe
            alerta.show();

    }
    private void initializeData() {

        List<Movimentos> movimento=(List<Movimentos>)operationsModelAcess.findBy(operacao.getMv_codigo());
        operacao = movimento.get(0);
        setOperacaoData();
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
       startActivity(new Intent(self,movimentos_list_grf.class));
    }
    private void setOperacaoData() {
        grf_mv_txt_st_codigo.setText(operacao.getMv_codigo());
        grf_mv_txt_st_empresa_cliente.setText(operacao.getEm_nome() + " (" + operacao.getCl_nome() + ") ");
        grf_mv_txt_st_material.setText(operacao.getMt_nome() + " (" + operacao.getMt_formato() + ") ");
        String vm_versao = operacao.getVm_versao();
        String mv_qt_retirar = operacao.getMv_qt_retirar();
        grf_mv_txt_st_versao.setText(vm_versao);
        grf_mv_txt_st_quantidade.setText(mv_qt_retirar);
        str_txt_data_atual.setText(dateFormat.format(date_atual));
       grf_mv_txt_st_a_retirar.setText(operacao.getMv_qt_retirada());
    }
    @Override
    protected void initComponents() {
        super.initComponents();
        this.toolbar = (Toolbar) findViewById(R.id.toolbar_login_rtc_trans);
        this.setTitle(R.string.still_pco_title_menu);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        grf_mv_txt_st_material = (EditText) findViewById(R.id.grf_mv_txt_st_material);
        grf_mv_txt_st_codigo = (EditText) findViewById(R.id.grf_mv_txt_st_codigo);
        grf_mv_txt_st_empresa_cliente = (EditText) findViewById(R.id.grf_mv_txt_st_empresa_cliente);
        grf_mv_txt_st_versao = (EditText) findViewById(R.id.grf_mv_txt_st_versao);
        grf_mv_txt_st_quantidade = (EditText) findViewById(R.id.grf_mv_txt_st_quantidade);
        grf_mv_txt_st_a_retirar = (EditText) findViewById(R.id.grf_mv_txt_st_a_retirar);
        grf__pmv_btn_st_stop1 = (Button) findViewById(R.id.grf__pmv_btn_st_stop1);
        str_txt_data_atual = (TextView) findViewById(R.id.str_txt_data_atual);
    }


    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(self, movimentos_list_grf.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            case R.id.gfr_camera_foto:
                try {

                    dispatchTakePictureIntent();
                } catch (Exception e) {
                    super.alert_comun("Erro ao Acessar A Camera.!");
                }
                break;
            case R.id.gfr__foto_view:
                startActivity(detalhe_foto);
                //Toast.makeText(self,"gggg",Toast.LENGTH_LONG).show();
                break;
            //Acoões do Menu
            case R.id.active_stop_rtc:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(Const.FINALIZA_APP);
                builder.setCancelable(true);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
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
        inflater.inflate(R.menu.menu_rtc_app_transp, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem status = menu.findItem(R.id.menu_status_server);
        if (!isConect) {
            status.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_no_conect_24dp));
        } else {
            status.setIcon(getResources().getDrawable(R.drawable.ic_wifi_black_conect_32dp));
        }
        return true;
    }
    /*Permisoes Gerais*/
    public void InitPermisionGeral() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
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
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(grafica_em_transporte.this,
                        new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.INSTALL_SHORTCUT,
                                Manifest.permission.LOCATION_HARDWARE,
                                Manifest.permission.SYSTEM_ALERT_WINDOW
                        }, 10000);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == CAMERA && resultCode == RESULT_OK) {
                sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(arquivoFoto))


                );
                // instancia o service, GPSTracker gps
                gps = new GpsPco(self);
                double latitude = 0.0;
                double longitude = 0.0;
                // verifica ele
                if (gps.canGetLocation()) {
                    // passa sua latitude e longitude para duas variaveis
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                }
                String[] tmpUser = USER_ATIVO.trim().replace("|", ";").split(";");
                final PictureImageGrafica imagen = new PictureImageGrafica();
                imagen.setDevice(jwt_device);
                imagen.setSincronizado("0");
                imagen.setLoc(String.valueOf(latitude) + "|" + String.valueOf(longitude));
                imagen.setPath_file(arquivoFoto.getAbsolutePath());
                imagen.setTb_retirada_gf_codigo(operacao.getMv_codigo());
                imagen.setName_file(arquivoFoto.getName());
                imagen.setUser(tmpUser[0]);
                long insert = wsPictoresCotroller.insert(imagen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private File criarArquivo() throws IOException {
        String timeStamp = new
                SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        timeStamp = operacao.getMv_codigo() + "_" + timeStamp;
        File pasta = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imagem = new File(pasta.getPath() + File.separator
                + "JPG_" + timeStamp + ".jpg");
        return imagem;
    }
    private void dispatchTakePictureIntent() {
        InitPermisionGeral();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
         //   try {
                String state = Environment.getExternalStorageState();
                String nameFile = this.generateNameFile();
                File mFileTemp = this.pathPictures(nameFile);
                arquivoFoto = this.pathPictures(nameFile);
         //   } catch (IOException ex) {
// Manipulação em caso de falha de criação do arquivo
        //.    }
            if (arquivoFoto != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        getBaseContext().getApplicationContext().getPackageName() +
                                ".provider", arquivoFoto);

                Uri mImageCaptureUri = null;
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    if(Build.VERSION.SDK_INT >=24){
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        mImageCaptureUri = Uri.fromFile(mFileTemp);
                    }else {
                        mImageCaptureUri = Uri.fromFile(mFileTemp);
                    }
                } else {
                    mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                takePictureIntent.putExtra("return-data", true);
                startActivityForResult(takePictureIntent, CAMERA);

            }
        }
    }
   @Override
    protected void onResume() {
        super.onResume();
        jwt_token = (String) wsConfigCotroller.getBy(new WsConfig(Constante.TOKEN_JWT));
        jwt_device = (String) wsConfigCotroller.getBy(new WsConfig(Constante.DEVICE_ACESS));
        USER_ATIVO = (String) wsConfigCotroller.getBy(new WsConfig(Constante.USER_ATIVO));
    }




    public String generateNameFile() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd_hhmmss");
        return ft.format(dNow) + ".jpg";
    }

    public File pathPictures(String nameFile) {
        String state = Environment.getExternalStorageState();
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + namePath);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (!folder.exists()) {
                folder.mkdir();
            }
            return new File(folder, nameFile);
        } else {
            return new File(getFilesDir() + File.separator + namePath, nameFile);
        }
    }



}