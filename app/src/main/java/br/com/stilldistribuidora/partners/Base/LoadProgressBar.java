package br.com.stilldistribuidora.partners.Base;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadProgressBar {
    public ProgressDialog progressDialog;

    public LoadProgressBar(Context contextActivity) {
        this.progressDialog=new ProgressDialog(contextActivity);
    }

    public void setTitle(String s) {
        this.progressDialog.setTitle(s);
    }

    public void setMessage(String s) {
        this.progressDialog.setMessage(s);
    }

    public void setCancelable(boolean b) {
        this.progressDialog.setCancelable(b);
    }

    public void show() {

        try {
            this.progressDialog.show();
        }catch (Exception e){}

    }

    public void dismiss() {
        try {
            this.progressDialog.dismiss();
        }catch (Exception e){}
    }
}
