package com.example.avalanche;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorProductos extends ArrayAdapter<FrutasVerduras> {

    public AdaptadorProductos(@NonNull Context context, ArrayList<FrutasVerduras> frutasVerdurasArrayList) {
        super(context, 0,frutasVerdurasArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView=convertView;
        if(listView==null){
            listView= LayoutInflater.from(getContext()).inflate(R.layout.linea,parent,false);
        }
        FrutasVerduras frutasVerduras=getItem(position);
        TextView nombre=listView.findViewById(R.id.txtNombreProd);
        TextView precio=listView.findViewById(R.id.txtPrecio);
        ImageView img=listView.findViewById(R.id.imgprod);

        nombre.setText(frutasVerduras.getNombre());
        precio.setText(String.valueOf(frutasVerduras.getPrecio()));
        Picasso.with(getContext()).load(frutasVerduras.getImagen()).into(img);
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on the item click on our list view.
                // we are displaying a toast message.
                Toast.makeText(getContext(), "Item clicked is : " + frutasVerduras.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
        return listView;
    }
}
