package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.models.SupervisorOperacao;
import br.com.stilldistribuidora.stillrtc.utils.Const;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Still Technology and Development Team on 23/01/2019.
 */

public class TiposDeAcessos extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000;
    private static final String TAG = PrefsActivity.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 110;
    private static List<SupervisorOperacao> ListSupervisor;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 224;
    private Toolbar toolbar;
    private PrefsHelper prefsHelper;
    private int userID;
    private Context mContext;
    private ProgressDialog progress;
    private Button btnSimpleOperacao;
    private Button btnSimultaneasOperacao;
    private Button btnAtualizacao;
    private Context context;
    private ApiConfig appConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_acesos);
        appConfig = new ApiConfig(this);
        this.btnSimpleOperacao = findViewById(R.id.btn_op_simples);
        this.btnSimultaneasOperacao = findViewById(R.id.btn_op_simultaneas);
        this.btnAtualizacao = findViewById(R.id.btn_op_atualizar);
        this.toolbar = findViewById(R.id.supervisor_toolbar);
        this.toolbar.setTitle(R.string.ops_simultaneas);
        setSupportActionBar(this.toolbar); //diz para o android executar as ações do menu

        this.btnSimpleOperacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TiposDeAcessos.this, PrefsActivity.class));
            }
        });


        this.btnSimultaneasOperacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(TiposDeAcessos.this, OpslistagenActivity.class));
                startActivity(new Intent(TiposDeAcessos.this, PrefsActivitySimutaneas.class));


            }
        });


        this.btnAtualizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                versionApp(mContext);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //habilitando Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tipo_operacoes, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i("###", item.toString());

        //Acoões do Menu
        if (item.getItemId() == R.id.action_close_app) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Const.FINALIZA_APP);
            builder.setCancelable(true);
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(TiposDeAcessos.this, LoginActivity.class);
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


        }

        return true;
    }

    public void showProgress() {


        Toast.makeText(this, R.string.alert_info_loading_operations, Toast.LENGTH_LONG).show();

/*
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.alert_info_loading_operations));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        */

    }

    public void dimissProgress() {
        //  progress.dismiss();

        Toast.makeText(this, "Operação Completa..!", Toast.LENGTH_LONG).show();
    }

    public void versionApp(final Context context) {

        ApiEndpointInterface apiService =
                ApiClient.getClient(
                        appConfig.Api.getEndPointAccess().getDataJson()
                ).create(ApiEndpointInterface.class);

        Call<ResponseBody> call = apiService.recuperarVersaoApp(
                appConfig.Api.getDeviceUuid().getDataJson(),
                appConfig.Api.getEndPointAccessToken().getDataJson(),
                appConfig.Api.getEndPointAccessTokenJwt().getDataJson()
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response != null) {
                    //startOperations();

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().string());
                        JSONArray contacts = jsonObj.getJSONArray("apps");

                        JSONObject c = contacts.getJSONObject(0);
                        int version_code = c.getInt("version_code");
                        final String url = c.getString("url");

                        if (version_code > versionCode()) {
                            Log.wtf(TAG, "Existe uma versão maior: " + version_code);
                            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context).create();
                            alertDialog.setTitle("Atenção");
                            alertDialog.setMessage("Há uma Atualização do RTC , Disponivel Deseja Install..?");
                            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "Não.",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dimissProgress();
                                        }
                                    });
                            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Sim",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            new TiposDeAcessos.DownloadFileFromURL().execute(ApiClient.BASE_BACKOFFICE + "app/" + url);

                                        }
                                    });
                            alertDialog.show();
                        } else {
                            dimissProgress();
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        dimissProgress();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    public int versionCode() {

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo != null ? pInfo.versionCode : 0;

    }

    public static class ApiConfig {
        private final AppCompatActivityBase Api;

        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }

    /**
     * Background Async Task to download file
     */
    private class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

        ProgressDialog mProgressDialog;

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(progress_bar_type);

            // instantiate it within the onCreate method
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Atualizando aplicativo");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);

            mProgressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();
                mProgressDialog.setMax(lenghtOfFile / 1024);


                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/stillrtc.apk");
                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //publishProgress((int) ((total * 100) /lenghtOfFile));
                    mProgressDialog.setProgress((int) ((total * 100) / (lenghtOfFile / 128)));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
//        protected void onProgressUpdate(String... progress) {
//            // setting progress percentage
//            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
//        }
        protected void onProgressUpdate(Integer... progress) {
            // doSomething(progress[0]);

            // mProgressDialog.setProgress(progress[0]);
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/stillrtc.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

    }

}
