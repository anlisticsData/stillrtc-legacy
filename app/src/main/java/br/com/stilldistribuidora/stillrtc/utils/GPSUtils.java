package br.com.stilldistribuidora.stillrtc.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import br.com.stilldistribuidora.stillrtc.R;

/**
 * Created by Still Technology and Development Team on 21/04/2017.
 */

public class GPSUtils {

    private static final String TAG = GPSUtils.class.getSimpleName();

    private Context context;

    public GPSUtils(Context context){
        this.context = context;
    }

    /**
     * Este metodo exite uma alerta para configuração do GPS
     */
    public void showSettingsAlert(){

            String[] array = {
                    context.getString(R.string.alert_title_gps_disable),
                    context.getString(R.string.alert_content_gps_disable),
                    context.getString(R.string.btn_cancel),
                    context.getString(R.string.btn_enable)};

            new Dialogs(context).showDialog(array, new DialogResultListener() {
                @Override
                public void onResult(boolean result) {
                    if (result) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }
            });

    }

    public static boolean isEnable(Context context){
        LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        return manager.isProviderEnabled( LocationManager.GPS_PROVIDER);
    }

}
