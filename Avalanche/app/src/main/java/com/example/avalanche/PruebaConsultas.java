package com.example.avalanche;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PruebaConsultas extends AppCompatActivity {
    private ListView lvFruterias, lvProductos;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String cif;
    private ArrayList<FrutasVerduras> productoA;
    private int i = 0;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prueba_consultas);
        View vista;

        lvFruterias = findViewById(R.id.lvFruterias);
        lvProductos = findViewById(R.id.lvProductos);

        LeerFruteria();
        List<String> list = new ArrayList<>();

        db.collection("Fruteria").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<String> list = new ArrayList<>();


                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("nombre"));
                    }
                    String[] Fruteria = new String[list.size()];

                    for (int i = 0; i < Fruteria.length; i++) {
                        Fruteria[i] = list.get(i);
                    }

                    Toast.makeText(PruebaConsultas.this, "1", Toast.LENGTH_LONG).show();
                    ArrayAdapter<String> adaptador;
                    adaptador = new ArrayAdapter<String>(PruebaConsultas.this, android.R.layout.simple_list_item_1, Fruteria);
                    lvFruterias.setAdapter(adaptador);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        lvFruterias.setOnItemClickListener(prueba);

        Toast.makeText(PruebaConsultas.this, "2", Toast.LENGTH_LONG).show();
    }

    private AdapterView.OnItemClickListener prueba = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String fruteriaSeleccionada = parent.getItemAtPosition(position).toString();
            final Query docRef = db.collection("Fruteria")
                    .whereEqualTo("nombre", fruteriaSeleccionada);

            docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    for (QueryDocumentSnapshot doc : value) {
                        verProducto(doc.getId());


                    }
                }

            });


            lvFruterias.setVisibility(View.GONE);
            lvProductos.setVisibility(View.VISIBLE);
        }

    };

    public void verProducto(String cif2) {
        Query docRef2 = db.collection("Mercancia")
                .whereEqualTo("fruteria", cif2);

        docRef2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }
                productoA = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {

                    String producto = doc.getString("producto");
                    crearListViewProductos(producto);


                }
                AdaptadorProductos adaptadorProductos=new AdaptadorProductos(PruebaConsultas.this,productoA);
                lvProductos.setAdapter(adaptadorProductos);
            }

        });
    }


    public void crearListViewProductos(String ProductoID) {
        String[] X = ProductoID.split("/");
        Toast.makeText(PruebaConsultas.this, X[0], Toast.LENGTH_LONG).show();
        Toast.makeText(PruebaConsultas.this, X[1], Toast.LENGTH_LONG).show();
        final DocumentReference docRef = db.collection(X[0]).document(X[1]);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }


                if (value != null && value.exists()) {
                    FrutasVerduras frutasVerduras = value.toObject(FrutasVerduras.class);
                    productoA.add(frutasVerduras);
                    i++;

                } else {
                    Log.d(TAG, "Current data: null");
                }


            }
        });


    }


    private void LeerFruteria() {

        List<String> list = new ArrayList<>();
        db.collection("Fruteria").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<String> list = new ArrayList<>();


                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("nombre"));
                    }
                    String[] Fruteria = new String[list.size()];

                    for (int i = 0; i < Fruteria.length; i++) {
                        Fruteria[i] = list.get(i);
                    }

                    Toast.makeText(PruebaConsultas.this, "1", Toast.LENGTH_LONG).show();
                    ArrayAdapter<String> adaptador;
                    adaptador = new ArrayAdapter<String>(PruebaConsultas.this, android.R.layout.simple_list_item_1, Fruteria);
                    lvFruterias.setAdapter(adaptador);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
}