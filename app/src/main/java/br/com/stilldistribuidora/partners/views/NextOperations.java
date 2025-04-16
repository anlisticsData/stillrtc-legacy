package br.com.stilldistribuidora.partners.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import br.com.stilldistribuidora.pco.ui.BaseActivity;
import br.com.stilldistribuidora.stillrtc.R;

public class NextOperations extends BaseActivity {
    private static final Components components = new Components();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_opcoming);
        components.contextActivity=this;
        components.toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
    }

    private static class Components {
        public Context contextActivity;
        public Toolbar toolbar;
    }
}
