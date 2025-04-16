package br.com.stilldistribuidora.partners.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import br.com.stilldistribuidora.partners.CallbacksWebViews.CallbackWebView;
import br.com.stilldistribuidora.stillrtc.R;

public class HomeWebView extends AppCompatActivity {

    private int REQUEST_IMAGE_CAPTURE=10002;
    private  static WebView webView;
    private static WebSettings webViewSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_web_view);
        InitPermisionGeral();
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.addJavascriptInterface(new AndroidDashboradNativeCallback(HomeWebView.this), "AndroidNativeCallback");
        webViewSetting = myWebView.getSettings();
        webViewSetting.setUseWideViewPort(true);
        webViewSetting.setDisplayZoomControls(false);
        webViewSetting.setJavaScriptEnabled(true);
        webViewSetting.setDomStorageEnabled(true);
        webViewSetting.setLoadWithOverviewMode(true);
        webViewSetting.setAllowUniversalAccessFromFileURLs(true);
        myWebView.setWebViewClient(new CallbackWebView());
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("DT", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        myWebView.loadUrl("file:///android_asset/home/home.html");
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
                ActivityCompat.requestPermissions(HomeWebView.this,
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            try{
                this.saveImage(imageBitmap,"TESTE");
            }catch (Exception e){}
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        if(webView !=null && webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }


    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;
        String IMAGES_FOLDER_NAME="IMAGENS";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = HomeWebView.this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + IMAGES_FOLDER_NAME);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + IMAGES_FOLDER_NAME;

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }







    public class AndroidDashboradNativeCallback {

        Context mContext;
         /** Instantiate the interface and set the context */
        public AndroidDashboradNativeCallback(Context context) {
            mContext = context;


        }


        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }



        public   class ContextStaticsObjects{

        }

    }




}