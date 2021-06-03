package com.example.avalanche;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LogInActivity extends AppCompatActivity {
    private int bandera;
    private Button btnEntrar,btnnuevo;
    private TextView txtCabecera;
    private CheckBox chkverc;
    private EditText editUsuario, editContrasinal;
    public static final int CODIGO=1;
    private SQLiteDatabase bd;
    private ImageView prueba;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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
                Intent intent=new Intent(LogInActivity.this, NuevoUsuarioActivity.class);
                startActivityForResult(intent,CODIGO);
            }
        });


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

            if (!email.equals("") && !password.equals("")) {

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

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
                                                Intent intent = new Intent(LogInActivity.this, PruebaConsultas.class);
                                                startActivityForResult(intent, CODIGO);

                                            }else if(usuario.isFruteria()&&bandera==2){

                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Intent intent = new Intent(LogInActivity.this, PantallaInicioEmpresa.class);
                                                startActivityForResult(intent, CODIGO);

                                            }
                                        }
                                    });


                                } else {
                                    Toast.makeText(LogInActivity.this, "Email o contraseña erróneos", Toast.LENGTH_LONG).show();
                                }
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