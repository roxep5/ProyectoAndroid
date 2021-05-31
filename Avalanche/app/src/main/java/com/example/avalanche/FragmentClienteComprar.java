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

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class FragmentClienteComprar extends Fragment {
    private String[] fruterias;
    //private ArrayList<Fruteria> fruterias;
    private ListView lvFruterias;
    ArrayAdapter<String> adaptador;
    private int i;
    public FragmentClienteComprar() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lvFruterias=getActivity().findViewById(R.id.lvFruterias);
        LeerFruteria();
        //adaptador= new ArrayAdapter<>(getActivity(), R.layout.linea, R.id.txtPrueba, fruterias);
        //lvFruterias.setAdapter(adaptador);

    }

    private void LeerFruteria(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*DocumentReference docRef = db.collection("Fruteria");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        })*/
        db.collection("Fruteria")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                i++;
                            }
                            fruterias=new String[i];
                            i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                fruterias[i]=document.getString("nombre");

                                i++;
                            }
                        } else {
                            Toast.makeText(getActivity(), "Algo va mal.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        for(int i=0;i<fruterias.length;i++){
            Toast.makeText(getActivity(),fruterias[i]+" "+i ,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cliente_comprar, container, false);
    }
}