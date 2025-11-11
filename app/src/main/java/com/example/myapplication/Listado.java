package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Listado extends AppCompatActivity {
    private TableLayout tblistado;
    private DynamicTable creaTabla;
    private FeedReaderDbHelper dbHelper;
    private final String[] cabecera = {"ID", "NOMBRE", "APELLIDO"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        dbHelper = new FeedReaderDbHelper(this);
        tblistado = findViewById(R.id.tblistado);

        creaTabla = new DynamicTable(tblistado, this);
        creaTabla.setCabecera(cabecera);

        ArrayList<String[]> datos = traerDatos();
        creaTabla.setDatos(datos);

        creaTabla.crearCabecera();
        creaTabla.crearFilas();
    }

    private ArrayList<String[]> traerDatos() {
        ArrayList<String[]> registros = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            String[] fila = new String[3];
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));

            fila[0] = String.valueOf(itemId);
            fila[1] = nombre;
            fila[2] = apellido;
            registros.add(fila);
        }
        cursor.close();
        db.close();

        if (registros.isEmpty()) {
            Toast.makeText(this, "No hay registros guardados.", Toast.LENGTH_LONG).show();
        }
        return registros;
    }

    public void Regresar(View vista){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}