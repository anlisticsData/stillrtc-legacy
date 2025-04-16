package br.com.stilldistribuidora.partners.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import br.com.stilldistribuidora.stillrtc.R;

public class HomeParceiro extends AppCompatActivity   {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_partens_app);
        setContentView(R.layout.activity_home_partens_app);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, HomeParterns.class, null)
                .addToBackStack("stack")
                .commit();
    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_partens, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.active_stop_rtc) {

            Intent i = new Intent(HomeParceiro.this, LoginAppActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return(true);
        }
        if (item.getItemId() == R.id.perfil) {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container_view, PerfilActivity.class, null)
                    .commit();
            return(true);
        }
        if (item.getItemId() == R.id.home) {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container_view, HomeParterns.class, null)
                    .commit();



            return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
