package br.com.stilldistribuidora.stillrtc;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.text.TextUtilsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by Still Technology and Development Team on 25/05/2017.
 */

public class MainTest extends AppCompatActivity {

    private Toolbar toolbar;

    private WebView wb;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    AlertDialog.Builder alert;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView textView = (TextView) findViewById(R.id.teste);

        String message = "hello my friend Por nessAbge";
       // message = Character.toUpperCase(message.charAt(0)) + message.substring(1);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.option_get_place:

                break;
        }
        return true;
    }

}

