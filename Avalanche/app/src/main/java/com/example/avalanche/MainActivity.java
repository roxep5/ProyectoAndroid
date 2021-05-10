package com.example.avalanche;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private Button btnEmpezar;
    private LinearLayout lnInscribirse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NuevaBD bd= new NuevaBD(this,"fruteria",null, 1);

        btnEmpezar=findViewById(R.id.btnEmpezar);
        lnInscribirse=findViewById(R.id.linearIrAPantallas);

        btnEmpezar.setOnLongClickListener(irALogin);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.opnoche:

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                return true;

            case R.id.opdia:

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                return true;
            case R.id.opsalir:

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);//false
        }
    }

    private View.OnLongClickListener irALogin = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            lnInscribirse.setVisibility(View.VISIBLE);
            btnEmpezar.setVisibility(View.GONE);
            return false;
        }
    };

}