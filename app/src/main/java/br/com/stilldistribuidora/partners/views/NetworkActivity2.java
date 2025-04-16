package br.com.stilldistribuidora.partners.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import br.com.stilldistribuidora.stillrtc.R;

public class NetworkActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network2);
    }
    public void onClose(View v){
        finish();
    }


}