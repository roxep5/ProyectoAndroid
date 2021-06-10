package com.example.avalanche;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import fragment.FragmentClienteComprar;
import fragment.FragmentClientePerfil;

public class PantallaInicioCliente extends AppCompatActivity {

    public static final int CODIGO=1;
    private TabLayout tlCliente;
    ViewPager viewPager;

    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio_cliente);

        tlCliente=findViewById(R.id.tlCliente);


        sectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());


        viewPager=findViewById(R.id.viewpager);
        viewPager.setAdapter(sectionsPagerAdapter);
        //TabLayout.OnTabSelectedListener


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlCliente));
        tlCliente.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

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
                AlertDialog.Builder ventana = new AlertDialog.Builder(this);
                ventana.setTitle("¿Esta seguro que quieres salir?");
                ventana.setMessage("Esto cerrará la aplicación y no mantendrá nada guardado");

                ventana.setPositiveButton("Si", (dialog, which) -> {

                    Bundle bundle=new Bundle();
                    bundle.putInt("finalizar",1);

                    Intent intent=new Intent();
                    intent.putExtras(bundle);

                    setResult(RESULT_OK, intent);
                    finish();
                });
                ventana.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                ventana.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);//false
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int datoRecibido;
        if(requestCode==CODIGO){
            if(resultCode==RESULT_OK){
                datoRecibido=data.getExtras().getInt("finalizar");
                if(datoRecibido==1){
                    finish();
                }
            }
        }
    }
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

 
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment =null;
            switch (position) {
                case 0:
                    fragment = new FragmentClienteComprar();
                    break;
                case 1:
                    fragment = new FragmentClientePerfil();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
