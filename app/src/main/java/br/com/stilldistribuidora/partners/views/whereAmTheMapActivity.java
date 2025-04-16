package br.com.stilldistribuidora.partners.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.httpService.partners.PartnersServices;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.db.models.MyRouterSerialized;
import br.com.stilldistribuidora.stillrtc.db.models.RouterApiDirection;
import br.com.stilldistribuidora.stillrtc.db.models.RouterApiDirectionContainer;
import br.com.stilldistribuidora.stillrtc.interfac.OnApiResponse;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;

public class whereAmTheMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Components componentsStatics = new Components();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_where_am_the_map);
        componentsStatics.self=this;
        Intent i = getIntent();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        componentsStatics.selectedRoute = (MyRouterSerialized) i.getSerializableExtra(Constants.ROUTER_OPERATION_LOCAL_MAP_DISTANCE);
        componentsStatics.apiConfig = new ApiConfig(componentsStatics.self);
        componentsStatics.configModel = new ConfigDataAccess(componentsStatics.self);

        componentsStatics.btnVoltarPosition=(Button)findViewById(R.id.btnVoltarPosition);
        componentsStatics.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        componentsStatics.userLogged = (Config) componentsStatics.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED));
        componentsStatics.token = (Config) componentsStatics.configModel.getById(String.format("%s='%s'", "descricao", Constants.API_TOKEN_JWT));




      new Thread(new Runnable() {
          @Override
          public void run() {
              componentsStatics.partnersServices=new PartnersServices(
                      new ConfigDataAccess(componentsStatics.self),componentsStatics.userLogged ,
                      componentsStatics.apiConfig.Api.getEndPointAccess() ,componentsStatics.token,
                      componentsStatics.apiConfig.Api.getEndPointAccessToken());

          }
      }).start();


        componentsStatics.onClick=new OnClick() {
            @Override
            public void onClick(Object obj) {
              new Thread(new Runnable() {
                  @Override
                  public void run() {

                      String info = (String) obj;
                      System.out.println("#4 "+info);
                      String[] startService = info.split("\\|");
                      String[] startServicePosition = startService[0].split("\\,");
                      String[] startServiceOperation = componentsStatics.selectedRoute.getPointStart().split("\\,");
                      componentsStatics.startService= new LatLng(Double.parseDouble(startServicePosition[0]), Double.parseDouble(startServicePosition[1]));
                      componentsStatics.startServiceOperation= new LatLng(Double.parseDouble(startServiceOperation[0]), Double.parseDouble(startServiceOperation[1]));
                      componentsStatics.routerInformation=(TextView)findViewById(R.id.routerInformation);
                      componentsStatics.btnIr=(Button)findViewById(R.id.btnIr);
                      OnApiResponse onClickHttp=new OnApiResponse() {
                          @Override
                          public void onSucess(Object responseObject) {

                              componentsStatics.routerApiDirectionContainer=(RouterApiDirectionContainer)responseObject;
                              drawMapInLines();
                              componentsStatics.map.addMarker(new MarkerOptions()
                                      .position(componentsStatics.startService).title("Estou Aqui no Momento..")
                                      .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                              componentsStatics.map.addMarker(new MarkerOptions()
                                      .position(componentsStatics.startServiceOperation).title("Operação Inicia Aqui.."));

                              componentsStatics.routerInformation.setText(
                                      String.format("%s - (%s)",componentsStatics.routerApiDirectionContainer.getDuracaoText(),
                                              componentsStatics.routerApiDirectionContainer.getDistanciaText())

                              );

                          }

                          @Override
                          public void onError(Object responseObject) {
                              String mensage=(String)responseObject;
                              //Cria o gerador do AlertDialog
                              AlertDialog.Builder builder = new AlertDialog.Builder(componentsStatics.self);
                              //define o titulo
                              builder.setTitle("Informação");
                              //define a mensagem
                              builder.setMessage(mensage.toString());
                              //define um botão como positivo
                              builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface arg0, int arg1) {
                                      finish();
                                  }
                              });

                              //cria o AlertDialog
                              componentsStatics.alerta = builder.create();
                              //Exibe
                              componentsStatics.alerta.show();


                          }
                      };
                      componentsStatics.btnVoltarPosition.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {

                              finish();
                          }
                      });
                      componentsStatics.btnIr.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              double latitude = componentsStatics.startServiceOperation.latitude;
                              double longitude = componentsStatics.startServiceOperation.longitude;


                              Uri mapAppUri = Uri.parse("geo:" + latitude + ","
                                      + longitude + "?q="
                                      + latitude + ","
                                      + longitude
                                      + "(" + Uri.encode("nome do local") + ")");
                              Intent intent = new Intent(Intent.ACTION_VIEW, mapAppUri);
                              Toast.makeText(componentsStatics.self, "Processando...", Toast.LENGTH_SHORT).show();
                              startActivity(intent);
                          }
                      });
                      getDirectionRouteGooleApi(startServicePosition,startServiceOperation,onClickHttp);

                 }
              }).start();

            }
        };



        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        geoPosition(componentsStatics.onClick);
                    }
                });



            }
        }).start();








        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        componentsStatics.fusedLocationClient.getLastLocation().addOnSuccessListener(whereAmTheMapActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    String[] startService = componentsStatics.selectedRoute.getPointStart().split("\\,");
                    LatLng latLng = new LatLng(Double.parseDouble(startService[0]), Double.parseDouble(startService[1]));

                    componentsStatics.map.addMarker(new MarkerOptions()
                            .position(latLng).title("Inicio da Rota"));
                    componentsStatics.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    componentsStatics.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));


                    System.out.println(location.getLatitude());
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




    }

    private void startGps() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Verifica se o GPS está ativo
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // Caso não esteja ativo abre um novo diálogo com as configurações para
        // realizar se ativamento
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private void getDirectionRouteGooleApi(String[] startServicePosition, String[] startServiceOperation, OnApiResponse onClickHttp) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                componentsStatics.partnersServices.
                        directionGoogleApi(
                                String.format("%s,%s",startServicePosition[0],startServicePosition[1]),
                                String.format("%s,%s",startServiceOperation[0],startServiceOperation[1]),
                                onClickHttp
                        );
            }
        }).start();

    }

    private void geoPosition(OnClick onClick) {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {


            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

            }

            @Override
            public void onProviderEnabled(String arg0) {

            }

            @Override
            public void onProviderDisabled(String arg0) {

            }

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                   onClick.onClick(location.getLatitude() + ","+location.getLongitude()+"|"+location.getAccuracy()+"|"+location.getProvider());

                }

            }
        }, null);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        componentsStatics.map = googleMap;
        componentsStatics.map.setIndoorEnabled(true);
        componentsStatics.map.setBuildingsEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_available, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.system_last:
                this.onBackPressed();
                return true;
            case R.id.system_close:
                Intent i = new Intent(getApplicationContext(), LoginAppActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void obtainLastKnowLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        componentsStatics.fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            //obtém a última localização conhecida
                            componentsStatics.mLastKnowLocation = task.getResult();
                            //Se o mapa está disponível
                            //coloca uma marca e faz zoom
                            if (componentsStatics.map != null) {
                                //    zoomMapToMyLocation();
                            }

                        } else {

                            //Não há localização conhecida ou houve uma excepção
                            //A excepção pode ser obtida com task.getException()

                        }
                    }
                });

    }


    private void drawMapInLines() {
        componentsStatics.map.clear();
        int size = componentsStatics.routerApiDirectionContainer.getRouterApiDirection().size();
        List<RouterApiDirection> routers = componentsStatics.routerApiDirectionContainer.getRouterApiDirection();
        LatLng p1 = null;
        List<LatLng> lts = new ArrayList<>();


        lts.add(new LatLng(componentsStatics.startService.latitude,
                     componentsStatics.startService.longitude));


        for(RouterApiDirection router : routers){
            p1 = new LatLng(
                    Double.parseDouble(router.getEnd_locationlat()),
                    Double.parseDouble(router.getEnd_locationlng()));
            lts.add(p1);
        }

        PolylineOptions gg = new PolylineOptions();
        for (LatLng df : lts) {
            gg.add(df);
        }
        Polyline line = componentsStatics.map.addPolyline(gg);


    }




    public static class ApiConfig {
        private final AppCompatActivityBase Api;
        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }

    
    public static class Components{
        public ApiConfig apiConfig;

        public Context self;
        public Config userLogged;
        public Config token;
        public ConfigDataAccess configModel;
        public RouterApiDirectionContainer routerApiDirectionContainer;
        public LatLng startService;
        public LatLng startServiceOperation;
        public TextView routerInformation;
        public Dialog alerta;
        private OnClick onClick;
        public Location mLastKnowLocation;
        private GoogleMap map;
        private FusedLocationProviderClient fusedLocationClient;
        private  MyRouterSerialized selectedRoute;
        private PartnersServices partnersServices;
        private ProgressDialog pd;
        private Button btnVoltarPosition,btnIr;
        



    }




}