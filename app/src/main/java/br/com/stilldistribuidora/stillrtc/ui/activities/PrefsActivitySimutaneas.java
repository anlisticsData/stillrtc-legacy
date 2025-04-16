package br.com.stilldistribuidora.stillrtc.ui.activities;


/**
 * Created by Still Technology and Development Team on 01/05/2017.
 */

        import android.Manifest;
        import android.annotation.TargetApi;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentSender;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Environment;
        import androidx.annotation.NonNull;
        import android.util.Log;
        import android.widget.TextView;

        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.PendingResult;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.location.LocationSettingsRequest;
        import com.google.android.gms.location.LocationSettingsResult;
        import com.google.android.gms.location.LocationSettingsStatusCodes;

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
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

        import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
        import br.com.stilldistribuidora.stillrtc.R;
        import br.com.stilldistribuidora.stillrtc.api.ApiClient;
        import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
        import br.com.stilldistribuidora.stillrtc.db.Constants;
        import br.com.stilldistribuidora.stillrtc.db.business.PrefsBusiness;
        import br.com.stilldistribuidora.stillrtc.db.models.Prefs;
        import br.com.stilldistribuidora.stillrtc.utils.GPSUtils;
        import okhttp3.ResponseBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        import static br.com.stilldistribuidora.stillrtc.utils.DateUtils.FORMAT_DATE;
        import static br.com.stilldistribuidora.stillrtc.utils.DateUtils.FORMAT_DATE_TIME_ZONE;

/**
 * Created by Ack Lay (Edilson) on 3/11/2017.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

public class PrefsActivitySimutaneas extends Activity {

    private static final int SPLASH_TIME_OUT = 2000;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 224;
    private static final String TAG = PrefsActivity.class.getSimpleName();

    private static final int REQUEST_CHECK_SETTINGS = 110;

    private Context context;

    private TextView tvCheckUpdate;

    private ApiConfig appConfig;

    public static class ApiConfig{
        private AppCompatActivityBase Api;
        public   ApiConfig(Context ctx){
            Api = new AppCompatActivityBase(ctx);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        appConfig =new ApiConfig(this);
        context = this;

        tvCheckUpdate = (TextView) findViewById(R.id.tvCheckUpdate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            insertDummyContactWrapper();
        } else {
            insertDummyContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    insertDummyContact();
                } else {
                    insertDummyContactWrapper();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {

                    checkUpdate();
                } else {
                    displayLocationSettingsRequest(context);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void startOperations() {
        startActivity(new Intent(this, OpslistagenActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }


    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add(getString(R.string.str_camera));
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add(getString(R.string.str_read_white_storage));
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add(getString(R.string.str_localization));
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add(getString(R.string.str_localization));

        if (permissionsList.size() > 0) {
//            if (permissionsNeeded.size() > 0) {
//                // Need Rationale
//
//
//                String message = getString(R.string.str_you_have) + permissionsNeeded.get(0);
//                for (int i = 1; i < permissionsNeeded.size(); i++)
//                    message = message + ", " + permissionsNeeded.get(i);
//                showMessageOKCancel(message,
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
//                                        REQUEST_CODE_ASK_PERMISSIONS);
//                            }
//                        });
//                return;
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }

        insertDummyContact();
    }

    private void insertDummyContact() {
        // Two operations are needed to insert a new contact.
        if (!GPSUtils.isEnable(context)) {
            displayLocationSettingsRequest(context);
        } else {
            checkUpdate();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.wtf(TAG, "All location settings are satisfied.");
                        checkUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.wtf(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(PrefsActivitySimutaneas.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.wtf(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.wtf(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void checkUpdate() {
        checkPrefs(context);
    }

    public Date dateDB, dateAPP;

    public void checkPrefs(final Context context) {

        final PrefsBusiness prefsBusiness = new PrefsBusiness(context);
        final ArrayList<Prefs> list = (ArrayList<Prefs>) prefsBusiness.getList("", Constants.KEY_CREATED_AT + " DESC LIMIT 1");

        final SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DATE_TIME_ZONE);
        final SimpleDateFormat formatterApi = new SimpleDateFormat(FORMAT_DATE);


        ApiEndpointInterface apiService =
                ApiClient.getClient(
                        appConfig.Api.getEndPointAccess().getDataJson()
                ).create(ApiEndpointInterface.class);

        Call<Prefs.Result> call = apiService.resgatarPreferencias(
                appConfig.Api.getDeviceUuid().getDataJson(),
                appConfig.Api.getEndPointAccessToken().getDataJson(),
                appConfig.Api.getEndPointAccessTokenJwt().getDataJson()
        );

        call.enqueue(new Callback<Prefs.Result>() {
            @Override
            public void onResponse(Call<Prefs.Result> call, Response<Prefs.Result> response) {

                if (response != null) {

                    if (response.body().ar.size() > 0) {
                        Prefs prefs = new Prefs();
                        prefs = response.body().ar.get(0);

                        if (list.size() > 0) {

                            try {
                                dateDB = formatterApi.parse(prefs.getCreated_at());
                                dateAPP = formatter.parse(list.get(0).getCreated_at());

                                if (dateDB.after(dateAPP)) {
                                    tvCheckUpdate.setText(getString(R.string.alert_info_loading_update));
                                    prefsBusiness.insert(prefs);
                                    startOperations();

                                    //versionApp(context);

                                } else {
                                    startOperations();
                                    //versionApp(context);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else {

                            tvCheckUpdate.setText(getString(R.string.alert_info_loading_update));
                            prefsBusiness.insert(prefs);

                            //versionApp(context);
                            startOperations();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Prefs.Result> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                //gps.stopUsingGPS();
            }
        });
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

                        if(version_code >  versionCode()){
                            Log.wtf(TAG, "Existe uma versão maior: "+version_code);
                            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context).create();
                            alertDialog.setTitle("Atenção");
                            alertDialog.setMessage("Há uma Atualização do RTC , Disponivel Deseja Install..?");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Não.",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startOperations();
                                        }
                                    });
                            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Sim",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            new DownloadFileFromURL().execute(ApiClient.BASE_BACKOFFICE+"app/"+url);
                                        }
                                    });
                            alertDialog.show();
                        } else {
                            startOperations();
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        startOperations();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    /**
     * Background Async Task to download file
     * */
    private class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

        ProgressDialog mProgressDialog;
        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
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
         * */
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
                mProgressDialog.setMax(lenghtOfFile/1024);



                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),8192);
                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/stillrtc.apk");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //publishProgress((int) ((total * 100) /lenghtOfFile));
                    mProgressDialog.setProgress((int) ((total * 100) / (lenghtOfFile/128)));
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
         * */
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
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded


           /*
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
            */
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/stillrtc.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

    }

    public int versionCode(){

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo != null ? pInfo.versionCode : 0;

    }
}