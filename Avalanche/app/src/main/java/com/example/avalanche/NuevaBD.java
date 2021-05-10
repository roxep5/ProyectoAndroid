package com.example.avalanche;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NuevaBD extends SQLiteOpenHelper {
    String sqlCreateFrutas = "CREATE TABLE FRUTAS (codigo INTEGER PRIMARY KEY, nombre TEXT, precio REAL)";
    String sqlCreateVerduras = "CREATE TABLE VERDURAS (codigo INTEGER PRIMARY KEY, nombre TEXT, precio REAL)";
    String sqlTienda = "CREATE TABLE TIENDA (codigo INTEGER PRIMARY KEY, nombre TEXT, telefono TEXT, contrasenha TEXT)";
    String sqlTiendaFrutas = "CREATE TABLE TIENDAFRUTAS (codigoTienda INTEGER, codigoFruta INTEGER ,stock INTEGER," +
            "FOREIGN KEY('codigoTienda') REFERENCES 'tienda'('codigo')," +
            "FOREIGN KEY('codigoFruta') REFERENCES 'frutas'('codigo'))";

    String sqlTiendaVerduras="CREATE TABLE TIENDAVERDURAS (codigoTienda INTEGER, codigoVerdura INTEGER ,stock INTEGER," +
            "FOREIGN KEY('codigoTienda') REFERENCES 'tienda'('codigo')," +
            "FOREIGN KEY('codigoVerdura') REFERENCES 'frutas'('codigo'))";

    public NuevaBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateFrutas);
        db.execSQL(sqlCreateVerduras);
        db.execSQL(sqlTienda);
        db.execSQL(sqlTiendaFrutas);
        db.execSQL(sqlTiendaVerduras);


        db.execSQL("INSERT INTO FRUTAS (codigo,nombre, precio) VALUES (1,'naranjas', 1.15)");
        db.execSQL("INSERT INTO FRUTAS (codigo,nombre, precio) VALUES (2,'fresas', 2.00)");
        db.execSQL("INSERT INTO FRUTAS (codigo,nombre, precio) VALUES (3,'platanos', 1.55)");
        db.execSQL("INSERT INTO FRUTAS (codigo,nombre, precio) VALUES (4,'melones', 4.00)");
        db.execSQL("INSERT INTO FRUTAS (codigo,nombre, precio) VALUES (5,'kiwis', 10.00)");

        db.execSQL("INSERT INTO VERDURAS (codigo,nombre, precio) VALUES (1,'tomates', 3.00)");
        db.execSQL("INSERT INTO VERDURAS (codigo,nombre, precio) VALUES (2,'brecol', 1.00)");
        db.execSQL("INSERT INTO VERDURAS (codigo,nombre, precio) VALUES (3,'zanahoria', 1.86)");
        db.execSQL("INSERT INTO VERDURAS (codigo,nombre, precio) VALUES (4,'patatas', 4)");
        db.execSQL("INSERT INTO VERDURAS (codigo,nombre, precio) VALUES (5,'maiz', 8.00)");

        db.execSQL("INSERT INTO TIENDA (codigo, nombre, telefono, contrasenha) VALUES (1, 'Frutas pepe', '986680209','abc123.')");
        db.execSQL("INSERT INTO TIENDA (codigo, nombre, telefono, contrasenha) VALUES (2, 'Manoli Frutas', '986698542','abc123.')");
        db.execSQL("INSERT INTO TIENDA (codigo, nombre, telefono, contrasenha) VALUES (3, 'Paco frutos', '986876215','abc123.')");


        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (1, 1, 10)");
        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (1, 2, 20)");
        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (1, 4, 18)");
        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (1, 5, 13)");

        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (2, 1, 40)");
        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (2, 2, 30)");
        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (2, 3, 43)");
        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (2, 4, 25)");
        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (2, 5, 50)");

        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (3, 1, 24)");
        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (3, 3, 26)");
        db.execSQL("INSERT INTO TIENDAVERDURAS (codigoTienda, codigoFruta, stock) VALUES (2, 5, 50)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
