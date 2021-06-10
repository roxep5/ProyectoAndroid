package com.example.avalanche;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pojo.Usuario;

public class LogInActivity extends AppCompatActivity {
    private int bandera;
    private Button btnnuevo;
    private CheckBox chkverc, chkguardar;
    private EditText editUsuario, editContrasinal;
    public static final int CODIGO=1;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;

    //Usuarios fruterias: (llevan true en la base de datos)
    //frutas@frutas.com
    //ll@ll.com
    //pepe@pepe.com
    //contraseñas: abc123. (el punto es importante)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        prefs=PreferenceManager.getDefaultSharedPreferences(LogInActivity.this);



        editUsuario=findViewById(R.id.editUsuario);
        editContrasinal=findViewById(R.id.editContrasinal);
        chkverc=findViewById(R.id.chkverc);
        chkguardar=findViewById(R.id.chkguardar);
        btnnuevo=findViewById(R.id.btnNuevo);


        int guardado=prefs.getInt("Guardado", 0);

        if(guardado==1){
            String email=prefs.getString("email", "");
            String contrasenha=prefs.getString("contrasenha", "");
            chkguardar.setChecked(true);
            editUsuario.setText(email);
            editContrasinal.setText(contrasenha);
        }

        mAuth = FirebaseAuth.getInstance();

        Intent intent=getIntent();
        bandera=intent.getExtras().getInt("bandera");

        if(bandera==1){
            btnnuevo.setEnabled(true);
        }

        btnnuevo.setOnClickListener(v -> {
            Intent intent1 =new Intent(LogInActivity.this, NuevoUsuarioActivity.class);
            startActivityForResult(intent1,CODIGO);
        });


        chkverc.setOnClickListener(v -> {
            if(chkverc.isChecked()){
                editContrasinal.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else if(!chkverc.isChecked()){
                editContrasinal.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });


    }


    public void entrarGo(View v){

        SharedPreferences.Editor editor = prefs.edit();
        if(chkguardar.isChecked()){

            editor.putInt("Guardado", 1);
            editor.putString("email", editUsuario.getText().toString());
            editor.putString("contrasenha", editContrasinal.getText().toString());
            editor.apply();

        }else if(!chkguardar.isChecked()){

            editor.putInt("Guardado", 0);
            editor.putString("email", "");
            editor.putString("contrasenha", "");
            editor.apply();

        }
        String email=editUsuario.getText().toString();
        String password=editContrasinal.getText().toString();

            if (!email.equals("") && !password.equals("")) {

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference docRef = db.collection("Users").document(email);
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Usuario usuario = documentSnapshot.toObject(Usuario.class);
                                        if (!usuario.isFruteria()&&bandera==1) {

                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Intent intent = new Intent(LogInActivity.this, PantallaInicioCliente.class);
                                            intent.putExtra("email",email);
                                            startActivityForResult(intent, CODIGO);

                                        }else if(usuario.isFruteria()&&bandera==2){

                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Intent intent = new Intent(LogInActivity.this, PantallaInicioEmpresa.class);
                                            intent.putExtra("email",email);
                                            startActivityForResult(intent, CODIGO);

                                        }
                                    }
                                });


                            } else {
                                Toast.makeText(LogInActivity.this, "Email o contraseña erróneos", Toast.LENGTH_LONG).show();
                            }
                        });

            } else {
                Toast.makeText(this, "Uno o ambos campos vacíos", Toast.LENGTH_LONG).show();
            }

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
                AlertDialog.Builder ventana = new AlertDialog.Builder(this);
                ventana.setTitle("¿Esta seguro que quieres salir?");
                ventana.setMessage("Esto cerrará la aplicación y no mantendrá nada guardado");

                ventana.setPositiveButton("Si", (dialog, which) -> {

                    Bundle bundle=new Bundle();
                    bundle.putInt("finalizar",1);

                    Intent intent=new Intent();
                    intent.putExtras(bundle);

                    setResult(RESULT_OK, intent);
                    finish();
                });
                ventana.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                ventana.show();
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