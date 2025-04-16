package br.com.stilldistribuidora.partners.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import br.com.stilldistribuidora.stillrtc.R;

public class lock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout unlock = (LinearLayout)findViewById(R.id.unlock);

        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}