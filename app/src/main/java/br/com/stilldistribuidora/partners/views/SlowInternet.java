package br.com.stilldistribuidora.partners.views;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import br.com.stilldistribuidora.stillrtc.R;

public class SlowInternet extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slow_activity);
    }

    public void goBack(View view){
        finish();
    }
}
