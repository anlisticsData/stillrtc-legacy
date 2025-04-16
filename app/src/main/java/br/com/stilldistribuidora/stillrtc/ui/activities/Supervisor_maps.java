package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;

/**
 * Created by Still Technology and Development Team on 16/08/2018.
 */

public class Supervisor_maps extends AppCompatActivity {
    private WebView mWebView;
    private String name;
    private Context mContext;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_supervisor_maps);
        mWebView = (WebView) findViewById(R.id.webview_supervisor);
        final ProgressDialog barra = new ProgressDialog(mContext);
        barra.setMessage("Carregando Mapa.! Aguarde...");
        barra.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barra.setIndeterminate(false);
        barra.setProgressStyle(1);
        barra.show();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name= bundle.getString("clientLoja");

        try {
            String url = ApiClient.BASE_BACKOFFICE + "pages/maps_supervisor.php?id=" + bundle.getString("idOperation") + "&zones=" + bundle.getString("areasLimitsOperation") + "&colored=false&app=true";
            WebSettings webSettings = mWebView.getSettings();
            mWebView.setWebViewClient(new MyWebViewClient());
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportZoom(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setDomStorageEnabled(true);
            webSettings.setSupportMultipleWindows(true);
            mWebView.addJavascriptInterface(this, "NAVEGAR");
            mWebView.loadUrl(url);

           // webSettings.setSaveFormData(true);

            setToolbar();
            mWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100) {
                    }
                    barra.setProgress(progress);
                    if (progress == 100) {
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

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.supervisor_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(name);
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    }

}
