package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.ArrayList;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;


/**
 * Created by Still Technology and Development Team on 23/04/2017.
 */

public class MapWebViewActivity extends AppCompatActivity {

    private static final String TAG = MapWebViewActivity.class.getSimpleName();

    private int position;
    private String url;
    private String name;

    ArrayList<Operation.KMLS> kmls = new ArrayList<>();
    private Operation.Result phraseReult;

    private String queryKmls = "";
    private Context mContext;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_maps_webview);
          try {
            Bundle extras = getIntent().getExtras();
            phraseReult = (Operation.Result) getIntent().getSerializableExtra("object");
            position = extras.getInt("position");
            name = phraseReult.ar.get(position).getStoreName();
            kmls = phraseReult.ar.get(position).ids;

            for (Operation.KMLS kml: kmls) {
                queryKmls +=kml.getId()+",";
            }

            Log.wtf(TAG,queryKmls);

            url = ApiClient.BASE_BACKOFFICE + "pages/maps.php?id="+phraseReult.ar.get(position).getDeliveryId()+"&zones="+queryKmls+"&colored=false&app=true";

            //url = "file:///android_asset/map.html";// ApiClient.BASE_BACKOFFICE + "pages/maps.php?id="+phraseReult.ar.get(position).getDeliveryId()+"&zones="+queryKmls+"&colored=false";
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }

        setToolbar();
        initUI();
        // listenerUI();
    }


    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(name);
    }

    public void initUI() {

        final ProgressDialog barra = new ProgressDialog(mContext);
        barra.setMessage("Carregando Mapa.! Aguarde...");
        barra.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barra.setIndeterminate(false);
        barra.setProgressStyle(1);
        barra.show();

        try {
            // [START]
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.clearCache(true);
            myWebView.getSettings().setBuiltInZoomControls(true);
            myWebView.getSettings().setUseWideViewPort(true);
            myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            myWebView.getSettings().setLoadWithOverviewMode(true);
            myWebView.getSettings().setBuiltInZoomControls(true);
            myWebView.getSettings().setDisplayZoomControls(false);
            myWebView.getSettings().setDomStorageEnabled(true);
            myWebView.getSettings().setSupportMultipleWindows(true);
            myWebView.addJavascriptInterface(this, "NAVEGAR");
            myWebView.loadUrl(url);
           // myWebView.getSettings().setSaveFormData(true);
            myWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if(progress < 100 ){
                    }
                    barra.setProgress(progress);
                    if(progress == 100) {
                        barra.dismiss();

                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            barra.dismiss();
        }



    }

    @JavascriptInterface
    public void navegarViaGps(String Coordenadas,String TipoRecurso){

        Log.i("NET_",Coordenadas+"   "+TipoRecurso);
        String uri = "";
        Intent intent;
        try {
            if(TipoRecurso.equals("1")){
                try {
                    Uri mapAppUri = Uri.parse("geo:" + Coordenadas + "?q=" + Coordenadas + "(" + Uri.encode("Ir até...") + ")");
                    String url = "geo:" + Coordenadas;
                    intent = new Intent(Intent.ACTION_VIEW, mapAppUri);
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                }catch ( ActivityNotFoundException ex  )
                {
                    // If Google is not installed, open it in Google Play:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=apps.maps"));
                    startActivity(intent);
                }

            }else{

                try {
                    String url = "waze://?ll=" + Coordenadas + "&navigate=yes";
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);

                }catch ( ActivityNotFoundException ex  )
                {
                    // If Waze is not installed, open it in Google Play:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                    startActivity(intent);
                }
            }

        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                 Log.i("G",innerEx.getMessage());
            }
        }


    }

    /**
     * Handles a click on the menu option to get a place.
     *
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //showCurrentPlace();
            onBackPressed();

        }
        return true;
    }
}