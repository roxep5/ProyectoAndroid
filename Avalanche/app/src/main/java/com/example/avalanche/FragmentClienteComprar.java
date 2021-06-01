package com.example.avalanche;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FragmentClienteComprar extends Fragment {

    private ListView lvFruterias, lvProductos;
    private Fruteria fruteria;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FragmentClienteComprar() {
        // Required empty public constructor
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista;
        vista= inflater.inflate(R.layout.fragment_cliente_comprar, container, false);
        lvFruterias=vista.findViewById(R.id.lvFruterias);
        lvProductos=vista.findViewById(R.id.lvProductos);
        LeerFruteria();
        lvFruterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fruteriaSeleccionada= parent.getItemAtPosition(position).toString();
                db.collection("Mercancia")
                        .whereEqualTo("fruteria", "/Fruteria/"+fruteriaSeleccionada)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    List<String> list = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Toast.makeText(getActivity(), " "+document.getLong("peso"), Toast.LENGTH_LONG).show();
                                    }
                                    /*ArrayAdapter<String> adaptador;
                                    adaptador= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Fruteria);
                                    lvProductos.setAdapter(adaptador);*/
                                } else {
                                    Toast.makeText(getActivity(), " Error ", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        );
                lvFruterias.setVisibility(View.GONE);
                lvProductos.setVisibility(View.VISIBLE);
            }
        }
         );
        return vista;
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
                        fruteria=document.toObject(Fruteria.class);
                    }Toast.makeText(getActivity(), list.toString(), Toast.LENGTH_LONG).show();
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