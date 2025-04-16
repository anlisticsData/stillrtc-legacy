package br.com.stilldistribuidora.pco.ui;

import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
 import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.com.stilldistribuidora.pco.api.ApiPco;

import br.com.stilldistribuidora.pco.db.dao.wsConfig;
import br.com.stilldistribuidora.pco.db.model.WsConfig;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {

    protected Context self;
    protected Toolbar toolbar;
    protected final int WAIT_TIME = 1000;
    protected Boolean isConect = false;
    protected ProgressBar progressBar;
    protected wsConfig wsConfigCotroller;
    protected int progressStatus = 0;
    protected boolean processando = false;
    protected Handler handler = new Handler();
    protected String tokenApi;
    protected String tokenApiAtivo;
    protected String deviceApi;
    protected AlertDialog alerta;
    protected ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        self = this;
        wsConfigCotroller=new wsConfig( self );
        deviceApi = (String) wsConfigCotroller.getBy( new WsConfig( "DEVICE_PCO" ) );
        tokenApiAtivo = (String) wsConfigCotroller.getBy( new WsConfig( "TOKEN_API_PCO_ATIVO" ) );
        getStatusServidor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        self = this;
        wsConfigCotroller=new wsConfig( self );
        deviceApi = (String) wsConfigCotroller.getBy( new WsConfig( "DEVICE_PCO" ) );
        tokenApiAtivo = (String) wsConfigCotroller.getBy( new WsConfig( "TOKEN_API_PCO_ATIVO" ) );
        getStatusServidor();

    }

    private void getStatusServidor() {
        new Thread( new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        appStatus();
                        Thread.sleep( WAIT_TIME );
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


            }


        } ).start();

    }


    private void appStatus() {
        if (Utils.isNetworkAvailable( self ) && Utils.isOnline()) {
            new ApiPco().api().api().enqueue( new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject( response.body().string() );
                            isConect = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            isConect = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                            isConect = false;
                        }
                    } else {
                        //Toast.makeText(MainActivity.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                        isConect = false;
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isConect = false;
                }
            } );
        } else {
            isConect = false;
            processando=false;
        }
    }


    protected void initComponents() {
    }

    protected void initActionGui() {
    }

    public void is_conected() {
        if (!Utils.isNetworkAvailable(self)) {
            AlertDialog alertDialog = new AlertDialog.Builder(self).create();
            alertDialog.setTitle(getString(R.string.msn_title) );
            alertDialog.setMessage(getString(R.string.not_conect));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }
    }


    public void processando(String title, String msn) {

        try {
            if(pd!=null){
               pd.cancel();
               pd.dismiss();
            }
            // Initialize a new instance of progress dialog
            pd = new ProgressDialog(self);
            // Set progress dialog style spinner
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // Set the progress dialog title and message
            pd.setTitle(title);
            pd.setMessage(msn);
            // Set the progress dialog background color
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
            pd.setIndeterminate(false);
            // Finally, show the progress dialog


            // Set the progress status zero on each button click
            progressStatus = 0;
            processando = true;
            // Start the lengthy operation in a background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressStatus < 100 ) {
                        // Update the progress status
                        progressStatus += 1;
                        // Try to sleep the thread for 20 milliseconds
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Update the progress bar
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Update the progress status
                                pd.setProgress(progressStatus);
                                // If task execution completed
                                if (progressStatus == 100) {
                                    // Dismiss/hide the progress dialog
                                    if (!processando) {
                                        pd.dismiss();
                                        pd.cancel();
                                    } else {
                                        progressStatus = 0;
                                    }
                                }
                            }
                        });
                    }
                }
            }).start(); // Start the operation
        }catch (Exception e){
            e.printStackTrace();        }

    }


    private void messagenBox(String Titulo, String menssagen, boolean cancelar) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        //define o titulo
        builder.setTitle( Titulo );
        //define a mensagem
        builder.setMessage( menssagen );
        //define um botão como positivo
        builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText( self, "positivo=" + arg1, Toast.LENGTH_SHORT ).show();
            }
        } );
        if (cancelar) {
            //define um botão como negativo.
            builder.setNegativeButton( "Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText( self, "negativo=" + arg1, Toast.LENGTH_SHORT ).show();
                }
            } );
        }
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }



    protected void alert_comun(String obj) {


        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        //define o titulo
        builder.setTitle(getString( R.string.grf_msn_systema_alert));
        //define a mensagem
        builder.setMessage(obj);
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // Toast.makeText(self, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });

        processando = false;

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();


    }


}
