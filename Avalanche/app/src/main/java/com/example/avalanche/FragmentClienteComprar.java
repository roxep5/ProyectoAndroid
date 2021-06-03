package com.example.avalanche;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FragmentClienteComprar extends Fragment {

    private ListView lvFruterias, lvProductos;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String cif;
    private List<FrutasVerduras> productoA;
    private int i=0;
    public FragmentClienteComprar() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);
        View vista;
        vista= inflater.inflate(R.layout.fragment_cliente_comprar, container, false);

        lvFruterias=vista.findViewById(R.id.lvFruterias);
        lvProductos=vista.findViewById(R.id.lvProductos);

        LeerFruteria();


        lvFruterias.setOnItemClickListener(prueba);




        return vista;
    }

    private AdapterView.OnItemClickListener prueba=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fruteriaSeleccionada= parent.getItemAtPosition(position).toString();
                    final Query docRef=db.collection("Fruteria")
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

    public void verProducto(String cif2){
        Query docRef2=db.collection("Mercancia")
                .whereEqualTo("fruteria", cif2);

        docRef2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }
                productoA=new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {

                    String producto=doc.getString("producto");
                    crearListViewProductos(producto);


                }
            }

        });
    }



    public void crearListViewProductos(String ProductoID){
        String[] X=ProductoID.split("/");
        Toast.makeText(getActivity(), X[0], Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), X[1], Toast.LENGTH_LONG).show();
        final DocumentReference docRef= db.collection(X[0]).document(X[1]);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }


                if (value != null && value.exists()) {
                FrutasVerduras frutasVerduras=value.toObject(FrutasVerduras.class);
                    productoA.add(frutasVerduras);
                    i++;

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });



    }



    private void LeerFruteria(){

        List<String> list = new ArrayList<>();
        db.collection("Fruteria").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<String> list = new ArrayList<>();


                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("nombre"));
                    }
                    String[] Fruteria=new String[list.size()];

                    for(int i=0;i<Fruteria.length;i++){
                        Fruteria[i]= list.get(i);
                    }

                    ArrayAdapter<String> adaptador;
                    adaptador= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Fruteria);
                    lvFruterias.setAdapter(adaptador);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
}