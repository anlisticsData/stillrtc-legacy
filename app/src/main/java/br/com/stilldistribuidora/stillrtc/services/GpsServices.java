package br.com.stilldistribuidora.stillrtc.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.Core;
import br.com.stilldistribuidora.stillrtc.db.business.DeviceStatusBusiness;
import br.com.stilldistribuidora.stillrtc.db.models.DeviceStatus;
import br.com.stilldistribuidora.stillrtc.db.models.Prefs;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

/**
 * Created by Still Technology and Development Team on 11/02/2019.
 */

public class GpsServices extends Service {

    private String TAG=getClass().getName();



    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    static final int CHANNEL_ID = 201913;
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // return super.onStartCommand( intent, flags, startId );
        System.out.println("#Y");
        context=this;



        try {
            Bundle extras = intent.getExtras();
            String deviceIdentifier = extras.getString( "deviceIdentifier" );
            int deliveryFragmentId = extras.getInt( "deliveryFragmentId" );
            GpsServicesGoogle26_ gps = new GpsServicesGoogle26_(context);
            gps.setData( deviceIdentifier,deliveryFragmentId );
            gps.initGps();


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                String NOTIFICATION_CHANNEL_ID = "123";
                String channelName = "My Background Service";
                NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
                chan.setLightColor( Color.BLUE);
                chan.setLockscreenVisibility( Notification.VISIBILITY_PRIVATE);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert manager != null;
                manager.createNotificationChannel(chan);
                Intent n=new Intent( context , ServiceDeviceStatus.class);
                PendingIntent pendingIntent=PendingIntent.getActivity( context,0,n,0 );
                Notification notification=new NotificationCompat.Builder( context, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle("Rtc-GPS" )
                        .setContentText( "Ativo -OK" )
                        .setSmallIcon( R.drawable.ic_audiotrack_light )
                        .setContentIntent( pendingIntent )
                        .build();
                startForeground( CHANNEL_ID,notification );
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return START_NOT_STICKY;

    }






    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }





    private class GpsServicesGoogle26_ implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener, LocationListener {


        Location mLastLocation;
        private  Context self;
        private GoogleApiClient mGoogleApiClient;
        private LocationRequest mLocationRequest;
        String lat, lon,disp_id="";
        private Prefs prefs;

        private Context context;


        int operacao_id=0;




        public GpsServicesGoogle26_(Context self){
            this.self=self;
            this.context=self;
            this.prefs = Core.getPrefs(context);

        }


        public void initGps(){
            buildGoogleApiClient();
           // MapsActivity.isInOperations=true;
        }


        public void setData(String disp,int operacao){

            this.disp_id=disp;
            this.operacao_id=operacao;

        }



        synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(self)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi( LocationServices.API)
                    .build();


            mGoogleApiClient.connect();


        }




        @SuppressLint("MissingPermission")
        @Override
        public void onConnected(@Nullable Bundle bundle) {

            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
            mLocationRequest.setInterval( 5*10000 ); // Update location every second

            //noinspection missingpermission
            LocationServices.FusedLocationApi.requestLocationUpdates( mGoogleApiClient, mLocationRequest, this );


            if (ActivityCompat.checkSelfPermission(self, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission( self, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient );
            if (mLastLocation != null) {
                lat = String.valueOf(mLastLocation.getLatitude());
                lon = String.valueOf(mLastLocation.getLongitude());
                setDadosDeliverys();

            }







        }

        private void setDadosDeliverys() {

            if(!lat.isEmpty() ){


                final DeviceStatus deviceStatus = new DeviceStatus();

                deviceStatus.setId(0);
                deviceStatus.setLatitude(Double.valueOf(lat));
                deviceStatus.setLongitude(Double.valueOf(lon));
                deviceStatus.setBatery( Utils.getBatteryPercentage(context));
                deviceStatus.setIp(Utils.getIPAddress(true));
                deviceStatus.setDeviceIdentifier(disp_id);
                deviceStatus.setDeviceFragmentId(operacao_id);
                deviceStatus.setCreatedAt( DateUtils.recuperarDateTimeAtual());




                if(Utils.isNetworkAvailable(context) && Utils.isOnline()) {

                    if(deviceStatus.getLatitude()!= 0 && deviceStatus.getLongitude()!=0){

                        Log.i( "#r999", "A ----" + lat + " " + lon +"  D : "+disp_id+"  --  "+String.valueOf(operacao_id ));
                        Core.registrarDispositivoStatusWS(context, deviceStatus);


                    }
                    verificarDispositivoStatus(context);
                }else {
                    if(deviceStatus.getLatitude()!= 0 && deviceStatus.getLongitude()!=0) {
                        DeviceStatusBusiness deviceStatusBusiness = new DeviceStatusBusiness(context);
                        deviceStatusBusiness.insert(deviceStatus);
                    } else {

                    }
                }

            }
        }

        public void verificarDispositivoStatus(Context context){
            DeviceStatusBusiness deviceStatusBusiness = new DeviceStatusBusiness(context);
            List<DeviceStatus> _list = (ArrayList<DeviceStatus>) deviceStatusBusiness.getList( Constants.KEY_SYNC+" = 0","");

            if(_list.size()>0){
                for(DeviceStatus deviceStatus: _list){
                    Core.registrarDispositivoStatusWS(context, deviceStatus);
                }
            }
        }


        @Override
        public void onConnectionSuspended(int i) {
            buildGoogleApiClient();

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            buildGoogleApiClient();
        }

        @Override
        public void onLocationChanged(Location location) {
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
            setDadosDeliverys();
        }
        public void StopService(){
            mGoogleApiClient.disconnect();
        }

    }


}
