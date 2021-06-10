package com.example.avalanche;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
    private static final int NOTIF_ALERTA_ID = 1;
    private Button btnEmpezar, btnCliente, btnEmpresa;
    private LinearLayout lnInscribirse, llInicio;

    //en la mayoria de clases usé lambda porque me parece mas bonito y limpio esteticamente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEmpezar=findViewById(R.id.btnEmpezar);
        btnCliente=findViewById(R.id.btnIniciarCliente);
        btnEmpresa=findViewById(R.id.btnIniciarEmpresa);
        lnInscribirse=findViewById(R.id.linearIrAPantallas);
        llInicio=findViewById(R.id.llInicio);




        btnEmpezar.setOnLongClickListener(irALogin);
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.logo_transparent)
                .setContentTitle("La app esta abierta")
                .setContentText("Esto es un aviso de que la app está abierta")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder2.build());
createNotificationChannel();
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.project_id);
            String description = getString(R.string.iniciar);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                ventana.setMessage("Esto cerrará la aplicación y no mantendrá nada guardado");

                ventana.setPositiveButton("Si", (dialog, which) -> finish());
                ventana.setNegativeButton("No", (dialog, which) -> dialog.cancel());
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