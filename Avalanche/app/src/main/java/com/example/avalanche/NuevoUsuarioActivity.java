package com.example.avalanche;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageException;

import java.util.HashMap;
import java.util.Map;

public class NuevoUsuarioActivity extends AppCompatActivity {

    public static final int CODIGO=1;
    private EditText editUsuario, editContrasinal, editnombre, editrepetircontrasinal, editNumero;
    private Button btnnuevo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        mAuth = FirebaseAuth.getInstance();

        editUsuario=findViewById(R.id.editnuevoEmail);
        editContrasinal=findViewById(R.id.editnuevoContrasinal);
        editnombre=findViewById(R.id.editnuevoNombre);
        editrepetircontrasinal=findViewById(R.id.editrepetirContrasinal);
        editNumero=findViewById(R.id.edittelefono);
        btnnuevo=findViewById(R.id.btnRegistrar);


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
    public void registrar(View v) {
        String nombre=editnombre.getText().toString();
        String email = editUsuario.getText().toString();
        String password = editContrasinal.getText().toString();
        String repetirpassword = editrepetircontrasinal.getText().toString();
        String numero=editNumero.getText().toString();


        if(password.equals(repetirpassword)) {
            if (!email.equals("") && !password.equals("")&& !nombre.equals("")&&!numero.equals("")) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Toast.makeText(NuevoUsuarioActivity.this, "Usuario Creado.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    Usuario usuario= new Usuario(false,nombre,numero);
                                    // Create a new user with a first and last name
                                    /*Map<String, Object> usuario = new HashMap<>();
                                    usuario.put("nombre", nombre);
                                    usuario.put("numero", numero);
                                    usuario.put("fruteria", false);*/

                                    db.collection("Users").document(email).set(usuario);

                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(NuevoUsuarioActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(NuevoUsuarioActivity.this, "Uno o varios campos vacíos", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(NuevoUsuarioActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
        }

    }
}