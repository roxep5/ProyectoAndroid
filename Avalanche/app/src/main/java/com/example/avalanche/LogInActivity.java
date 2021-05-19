package com.example.avalanche;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayInputStream;

public class LogInActivity extends AppCompatActivity {
    private int bandera;
    private Button btnEntrar,btnnuevo;
    private TextView txtCabecera;
    private CheckBox chkverc;
    private EditText editUsuario, editContrasinal;
    public static final int CODIGO=1;
    //private NuevaBD fruteriabd= new NuevaBD(this,"fruteria",null, 1);
    private SQLiteDatabase bd;
    private ImageView prueba;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //bd=fruteriabd.getReadableDatabase();

        txtCabecera=findViewById(R.id.txtCabeceraLogin);
        prueba=findViewById(R.id.prueba);

        Intent intent=getIntent();
        bandera=intent.getExtras().getInt("bandera");


        btnEntrar=findViewById(R.id.btnEntrar);
        editUsuario=findViewById(R.id.editUsuario);
        editContrasinal=findViewById(R.id.editContrasinal);
        chkverc=findViewById(R.id.chkverc);
        btnnuevo=findViewById(R.id.btnNuevo);

        FirebaseAnalytics firebaseAnalytics=FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();


        if(bandera==1){
            btnnuevo.setEnabled(true);
        }

        btnnuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,NuevoUsuario.class);
                startActivityForResult(intent,CODIGO);
            }
        });






        /*if(bandera==1){
            txtCabecera.setText("Cliente");
        }else if(bandera==2){
            txtCabecera.setText("Empresa");
        }*/

        /*Cursor c=bd.rawQuery("SELECT imagen FROM frutas WHERE codigo=1",null);
        //Cursor c=bd.rawQuery("SELECT * FROM Usuario",null);
        if (c.moveToFirst()) {
            byte[] blob = c.getBlob(0);
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            Bitmap bitmap = BitmapFactory.decodeStream(bais);
            prueba.setImageBitmap(bitmap);
        }else{

            Toast.makeText(this, "Usuario inexistente", Toast.LENGTH_LONG).show();
        }

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(LogInActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usuario","admin");
        editor.putString("contrasenha","admin");*/




        chkverc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkverc.isChecked()){

                    editContrasinal.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else if(!chkverc.isChecked()){
                    editContrasinal.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ;
                }
            }
        });


    }





    public void entrarGo(View v){
        String email=editUsuario.getText().toString();
        String password=editContrasinal.getText().toString();
        if(!email.equals("")&&!password.equals("")){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent=new Intent(LogInActivity.this,PantallaInicioCliente.class);
                                startActivityForResult(intent,CODIGO);
                            }else{
                                Toast.makeText(LogInActivity.this, "Usuario inexistente2", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Uno o ambos campos vacíos", Toast.LENGTH_LONG).show();
        }
        /*String usuario=editUsuario.getText().toString();
        String contrasinal=editContrasinal.getText().toString();
        if(bandera==1){

            Cursor c=bd.rawQuery("SELECT * FROM USUARIO WHERE nombreUsuario='"+usuario+"' and contrasenha='"+contrasinal+"'",null);
            //Cursor c=bd.rawQuery("SELECT * FROM Usuario",null);
            if (c.moveToFirst()) {
                Intent intent=new Intent(LogInActivity.this,PantallaInicioCliente.class);
                startActivityForResult(intent,CODIGO);

            }else{

                Toast.makeText(this, "Usuario inexistente", Toast.LENGTH_LONG).show();
            }
        }else if(bandera==2){

            Cursor c=bd.rawQuery("SELECT * FROM TIENDA WHERE nombre='"+usuario+"' and contrasenha='"+contrasinal+"'",null);
            //Cursor c=bd.rawQuery("SELECT * FROM Usuario",null);
            if (c.moveToFirst()) {
                Intent intent=new Intent(LogInActivity.this,PantallaInicioCliente.class);
                startActivityForResult(intent,CODIGO);

            }else{
                Toast.makeText(this, "Usuario inexistente", Toast.LENGTH_LONG).show();
            }
        }*/
    }



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
                Bundle bundle=new Bundle();
                bundle.putInt("finalizar",1);

                Intent intent=new Intent();
                intent.putExtras(bundle);

                setResult(RESULT_OK, intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);//false
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int datoRecibido;
        if(requestCode==CODIGO){
            if(resultCode==RESULT_OK){
                datoRecibido=data.getExtras().getInt("finalizar");
                if(datoRecibido==1){
                    Bundle bundle=new Bundle();
                    bundle.putInt("finalizar",1);

                    Intent intent=new Intent();
                    intent.putExtras(bundle);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    }
}