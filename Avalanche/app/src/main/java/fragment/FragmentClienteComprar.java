package fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import adaptador.AdaptadorProductos;
import pojo.FrutasVerduras;
import com.example.avalanche.R;
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

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FragmentClienteComprar extends Fragment {

    private static final int LLAMADA_TELEFONO = 1 ;
    private ListView lvFruterias, lvProductos;
    private Button btnSiguiente;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String telefono;
    private String[] fruteria;
    private ArrayList<FrutasVerduras> productoA;
    private int i=0;
    public FragmentClienteComprar() {
        // Required empty public constructor
    }

//Los hilos tienen que ir interrelacionados para no sobreponerse, de esta manera se logra que las fruterias muestren las frutas que hemos dado de alta en la coleccion de mercancia
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista;
        vista= inflater.inflate(R.layout.fragment_cliente_comprar, container, false);

        lvFruterias=vista.findViewById(R.id.lvFruterias);
        lvProductos=vista.findViewById(R.id.lvProductos);
        btnSiguiente=vista.findViewById(R.id.btnsiguiente);

        LeerFruteria();


        lvFruterias.setOnItemClickListener(prueba);
        Task1 task1=new Task1();
        task1.execute("dos");
//        Toast.makeText(getActivity(),"2", Toast.LENGTH_LONG).show();



        return vista;
    }

    private AdapterView.OnItemClickListener prueba = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /**/
            String fruteriaSeleccionada = parent.getItemAtPosition(position).toString();
            PopupMenu popup = new PopupMenu(getActivity(), view);//al inflarlo de manera asincronica decidi usar esto ya que no encontre la respuesta para el menu contextual
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_contextual,popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.comprar:

                        final Query docRef = db.collection("Fruteria")
                                .whereEqualTo("nombre", fruteriaSeleccionada);

                        docRef.addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            for (QueryDocumentSnapshot doc : value) {
                                Cargar2 cargar1=new Cargar2();
                                cargar1.execute(doc.getId());

                            }
                        });


                        lvFruterias.setVisibility(View.GONE);

                        btnSiguiente.setVisibility(View.VISIBLE);
                        lvProductos.setVisibility(View.VISIBLE);
                        break;
                    case R.id.llamar:
                        seleccionaridUser(fruteriaSeleccionada);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},LLAMADA_TELEFONO);
                        } else{
                            Leer leer=new Leer();
                            leer.execute("");
                        }



                        break;
                }
                return true;
            });
            popup.show();
        }

    };
    public void seleccionaridUser(String nombrefruteria){
        Query docRef2 = db.collection("Fruteria")
                .whereEqualTo("nombre", nombrefruteria);

        docRef2.addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }
            for (QueryDocumentSnapshot doc : value) {
                String usuario=doc.get("usuario").toString();
                SeleccionarNumero(usuario);

            }


        });
    }

    public void SeleccionarNumero(String nombreusuario){

        String[] X = nombreusuario.split("/");
        DocumentReference docRef2 = db.collection(X[0]).document(X[1]);

        docRef2.addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }
            if (value != null && value.exists()) {
                telefono="tel:"+value.get("numero").toString();

                Toast.makeText(getActivity(),telefono, Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, value + " data: null");
            }

            });
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //vemos si el código de respuesta coincide con el identificador de nuestra solicitud
        if (requestCode==LLAMADA_TELEFONO){

            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            LLAMADA_TELEFONO);
                } else {

                    startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse(telefono)));
                }
            }
            else {

                Toast.makeText(getActivity(),"No se permite realizar la llamada por falta de permisos", Toast.LENGTH_LONG).show();

            }
        }
    }



    private void LeerFruteria() {
        Task<QuerySnapshot> task=FirebaseFirestore.getInstance().collection("Fruteria").get();
        List<String> list = new ArrayList<>();
        task.addOnSuccessListener(getActivity(), queryDocumentSnapshots -> {
            {
                if (task.isSuccessful()) {

                    List<String> list1 = new ArrayList<>();


                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list1.add(document.getString("nombre"));
                    }
                    fruteria = new String[list1.size()];

                    for (int i = 0; i < fruteria.length; i++) {
                        fruteria[i] = list1.get(i);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }


    class Task1 extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            try{
                Thread.sleep(400);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayAdapter<String> adaptador;
            adaptador = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, fruteria);
            lvFruterias.setAdapter(adaptador);
        }
    }

    class Cargar1 extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                Thread.sleep(50);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String frutasVerduras2) {
            String[] X = frutasVerduras2.split("/");
            super.onPostExecute(frutasVerduras2);
            final DocumentReference docRef = db.collection(X[0]).document(X[1]);
            docRef.addSnapshotListener((value, error) -> {
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


            });
        }

    }
    class Cargar2 extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                Thread.sleep(50);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String cif2) {
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
                        Cargar1 cargar1=new Cargar1();
                        cargar1.execute(producto);


                    }
                    Cargar3 cargar3=new Cargar3();
                    cargar3.execute("");

                }

            });
        }

    }
    class Cargar3 extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                Thread.sleep(50);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String cif2) {

                    AdaptadorProductos adaptadorProductos=new AdaptadorProductos(getActivity(),productoA);
                    lvProductos.setAdapter(adaptadorProductos);

                }

        }
        class Leer extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                Thread.sleep(500);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String cif2) {


            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse(telefono)));

                }

        }


    }








