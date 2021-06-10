package fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avalanche.LogInActivity;
import com.example.avalanche.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentClientePerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentClientePerfil extends Fragment {

    private TextView nombre, numero, txtemail;
    private String nombreS, numeroS, emailid, email;
    private Intent i;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentClientePerfil() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentClientePerfil newInstance(String param1, String param2) {
        FragmentClientePerfil fragment = new FragmentClientePerfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista;
        vista= inflater.inflate(R.layout.fragment_cliente_perfil, container, false);

        Intent i = getActivity().getIntent();
        email = i.getExtras().getString("email");
        nombre = vista.findViewById(R.id.txtnombre);
        numero = vista.findViewById(R.id.txtnumero);
        txtemail = vista.findViewById(R.id.txtemail);
        Ver ver=new Ver();
        ver.execute(email);
        return vista;
    }

    class Ver extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(75);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String email) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Users").document(email);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nombreS = "Nombre: " + document.getString("nombre");
                        numeroS = "Numero: " + document.getString("numero");
                        emailid = "Email: " + document.getId();

                        PonerSet ponerSet = new PonerSet();
                        ponerSet.execute("");

                        Toast.makeText(getActivity(), "nombre:" + document.getString("nombre"), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "numero:" + document.getString("numero"), Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });


        }



    }
    class PonerSet extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String email) {

            nombre.setText(nombreS);
            numero.setText(numeroS);
            txtemail.setText(emailid);


        }

    }
}