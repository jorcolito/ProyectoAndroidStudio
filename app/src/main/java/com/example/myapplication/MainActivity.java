package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText txtid;
    private EditText txtnombre;
    private EditText txtapellido;

    FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new FeedReaderDbHelper(this);

        txtid = findViewById(R.id.editText_id);
        txtnombre = findViewById(R.id.editText_nombre);
        txtapellido = findViewById(R.id.editText_apellido);
    }

    public void Listar(View vista){
        Intent listar = new Intent(this,Listado.class);
        startActivity(listar);
    }

    public void Guardar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (txtnombre.getText().toString().isEmpty() || txtapellido.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe llenar Nombre y Apellido", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, txtnombre.getText().toString());
        values.put(FeedReaderContract.FeedEntry.column2, txtapellido.getText().toString());

        long newRowId = db.insert(FeedReaderContract.FeedEntry.nameTable, null, values);

        Toast.makeText(getApplicationContext(), "Se guardó el registro con clave " +
                newRowId,Toast.LENGTH_LONG).show();
        db.close();
    }

    public void Buscar(View vista){
        if (txtid.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese un ID para buscar", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {txtid.getText().toString()};

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()){
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));

            txtnombre.setText(nombre);
            txtapellido.setText(apellido);
            Toast.makeText(getApplicationContext(), "Registro encontrado.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "ID no encontrado.", Toast.LENGTH_LONG).show();
            txtnombre.setText("");
            txtapellido.setText("");
        }
        cursor.close();
        db.close();
    }

    public void Eliminar(View vista) {
        if (txtid.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese un ID para eliminar", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {txtid.getText().toString()};

        int deletedRows = db.delete(FeedReaderContract.FeedEntry.nameTable, selection, selectionArgs);

        if (deletedRows > 0) {
            Toast.makeText(getApplicationContext(), "Se eliminó " + deletedRows + " registro(s)", Toast.LENGTH_LONG).show();
            txtid.setText("");
            txtnombre.setText("");
            txtapellido.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "ID no encontrado para eliminar", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void Actualizar(View vista){
        if (txtid.getText().toString().isEmpty() || txtnombre.getText().toString().isEmpty() || txtapellido.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Llene ID, Nombre y Apellido para actualizar", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String nombre = txtnombre.getText().toString();
        String apellido = txtapellido.getText().toString();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString()};

        int count = db.update(FeedReaderContract.FeedEntry.nameTable,
                values,
                selection,
                selectionArgs);

        Toast.makeText(getApplicationContext(), "Se actualizó "+
                count+" registro(s)", Toast.LENGTH_LONG).show();
        db.close();
    }
}