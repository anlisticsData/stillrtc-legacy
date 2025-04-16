package br.com.stilldistribuidora.partners.Base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import br.com.stilldistribuidora.stillrtc.R;

public class GeneralAccessPermissions extends Dialog implements android.view.View.OnClickListener  {
    private final OnResponse onResponse;
    public Activity c;
    public Dialog d;
    public Button ok,moreInfoButton;


    public GeneralAccessPermissions(@NonNull Activity context,OnResponse onResponse) {
        super(context);
        this.c = context;
        this.onResponse=onResponse;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gerenal_layout_information);




        

        /*
        ok = (Button) findViewById(R.id.agreenButton);
        ok.setOnClickListener(this);

        moreInfoButton=(Button) findViewById(R.id.moreInfoButton);
        moreInfoButton.setOnClickListener(this);*/

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.moreInfoButton:
                String url=c.getString(R.string.privacy);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                c.startActivity(i);
                break;

            default:
                break;
        }

        onResponse.OnResponseType("OK",null);
        dismiss();
    }
}
