package com.example.avalanche;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {
    public static final int CODIGO=1;
    private Button btnEmpezar, btnCliente, btnEmpresa;
    private LinearLayout lnInscribirse, llInicio;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://my-custom-bucket");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NuevaBD bd= new NuevaBD(this,"fruteria",null, 1);

        btnEmpezar=findViewById(R.id.btnEmpezar);
        btnCliente=findViewById(R.id.btnIniciarCliente);
        btnEmpresa=findViewById(R.id.btnIniciarEmpresa);
        lnInscribirse=findViewById(R.id.linearIrAPantallas);
        llInicio=findViewById(R.id.llInicio);


        Notification.Builder builder = new Notification.Builder(this);

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
                AlertDialog.Builder ventana = new AlertDialog.Builder(this);
                ventana.setTitle("¿Esta seguro que quieres salir?");
                ventana.setMessage("Esto cerrará la aplicacion y no mantendra nada guardado");

                ventana.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                ventana.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
                ventana.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);//false
        }
    }

    private View.OnLongClickListener irALogin = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            lnInscribirse.setVisibility(View.VISIBLE);
            llInicio.setVisibility(View.GONE);
            return false;
        }
    };


    public void btnIniciarLogin(View v){
        Intent intent=new Intent(MainActivity.this,LogInActivity.class);
        if(v==btnCliente){
            intent.putExtra("bandera",1);
        }else if(v==btnEmpresa){
            intent.putExtra("bandera",2);
        }
        startActivityForResult(intent,CODIGO);
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int datoRecibido;
        if(requestCode==CODIGO){
            if(resultCode==RESULT_OK){
                datoRecibido=data.getExtras().getInt("finalizar");
                if(datoRecibido==1){
                    finish();
                }
            }
        }
    }

}