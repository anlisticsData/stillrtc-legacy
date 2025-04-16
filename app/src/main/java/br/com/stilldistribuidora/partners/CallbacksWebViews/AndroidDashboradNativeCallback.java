package br.com.stilldistribuidora.partners.CallbacksWebViews;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class AndroidDashboradNativeCallback {

    Context mContext;
    private  ContextStaticsObjects contextStaticsObjects = new ContextStaticsObjects();
    /** Instantiate the interface and set the context */
    public AndroidDashboradNativeCallback(Context context) {
        mContext = context;


    }


    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }



    public static   class ContextStaticsObjects{

    }

}
