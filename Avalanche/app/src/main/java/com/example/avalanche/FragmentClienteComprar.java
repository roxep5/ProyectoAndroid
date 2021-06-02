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
import com.google.firebase.firestore.FirebaseFirestore;
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
    private String[] referenciaFrutas;
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
        Toast.makeText(getActivity(), "prueba", Toast.LENGTH_LONG).show();


        lvFruterias.setOnItemClickListener(prueba);




        return vista;
    }
    private AdapterView.OnItemClickListener prueba=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fruteriaSeleccionada= parent.getItemAtPosition(position).toString();
                    db.collection("Fruteria")
                            .whereEqualTo("nombre", fruteriaSeleccionada)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                           if (task.isSuccessful()) {

                                                               List<String> list = new ArrayList<>();
                                                               for (QueryDocumentSnapshot document : task.getResult()) {

                                                                   cif=document.getId();
                                                               }
                                                               ArrayAdapter<String> adaptador;
                                                               /*adaptador= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Fruteria);
                                                               lvProductos.setAdapter(adaptador)*/

                                                           } else {
                                                               Toast.makeText(getActivity(), " Error ", Toast.LENGTH_LONG).show();
                                                           }
                                                       }
                                                   }
                            );
                    db.collection("Mercancia")
                            .whereEqualTo("fruteria", "Fruteria/"+cif)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                           if (task.isSuccessful()) {

                                                               List<String> list = new ArrayList<>();
                                                               for (QueryDocumentSnapshot document : task.getResult()) {

                                                                   list.add(document.getString("FrutasVerduras"));
                                                               }
                                                                referenciaFrutas=new String[list.size()];
                                                               for(int i=0;i<referenciaFrutas.length;i++){
                                                                   referenciaFrutas[i]=list.get(i);
                                                               }
                                                           } else {
                                                               Toast.makeText(getActivity(), " Error ", Toast.LENGTH_LONG).show();
                                                           }
                                                       }
                                                   }
                            );
                    for(int i=0;i<referenciaFrutas.length;i++){
                        db.document(referenciaFrutas[i]).get().addOnSuccessListener(
                                new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
 
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }

                        };
                    }

                    lvFruterias.setVisibility(View.GONE);
                    lvProductos.setVisibility(View.VISIBLE);
                }

        };
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.comprar:

                return true;
            //otros casos…
            case R.id.llamar:

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

   /* @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1,1,0,"comprar");
        menu.add(2,2,0,"comprar");

    }
*/
   @Override
   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
           menuInfo) {
       super.onCreateContextMenu(menu, v, menuInfo);
       MenuInflater inflater=getActivity().getMenuInflater();
       AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
       String elemento=lvFruterias.getAdapter().getItem(info.position).toString();
       menu.setHeaderTitle(elemento);
       inflater.inflate(R.menu.menu_contextual, menu);
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
                    }Toast.makeText(getActivity(), list.toString(), Toast.LENGTH_LONG).show();

                    String[] Fruteria=new String[list.size()];

                    for(int i=0;i<Fruteria.length;i++){
                        Fruteria[i]= list.get(i);
                    }

                    ArrayAdapter<String> adaptador;
                    adaptador= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Fruteria);
                    lvFruterias.setAdapter(adaptador);
                    registerForContextMenu(lvFruterias);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
}