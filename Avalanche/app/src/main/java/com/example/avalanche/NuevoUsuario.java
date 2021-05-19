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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NuevoUsuario extends AppCompatActivity {
    public static final int CODIGO=1;
    private EditText editUsuario, editContrasinal, editnombre, editrepetircontrasinal;
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
        String email = editUsuario.getText().toString();
        String password = editContrasinal.getText().toString();
        if (!email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(NuevoUsuario.this, "Usuario Creado.",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(NuevoUsuario.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Uno o ambos campos vacíos", Toast.LENGTH_LONG).show();
        }
    }

}