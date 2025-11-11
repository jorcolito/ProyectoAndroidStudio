package com.example.myapplication;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;
import android.util.TypedValue;

import java.util.ArrayList;

public class DynamicTable {
    private TableLayout tabla;
    private Context contexto;
    private String[] cabecera;
    private ArrayList<String[]> datos;
    private TableRow fila;
    private TextView celda;

    public DynamicTable(TableLayout tabla, Context contexto) {
        this.tabla = tabla;
        this.contexto = contexto;
    }

    public void setCabecera(String[] cabecera) {
        this.cabecera = cabecera;
    }

    public void setDatos(ArrayList<String[]> datos) {
        this.datos = datos;
    }

    private void nuevaFila() {
        fila = new TableRow(contexto);
    }

    private void nuevaCelda() {
        celda = new TextView(contexto);
        celda.setGravity(Gravity.CENTER);
        celda.setTextSize(14);
    }

    public void crearCabecera() {
        nuevaFila();
        for (String titulo : cabecera) {
            nuevaCelda();
            celda.setText(titulo);
            celda.setTextSize(20);
            celda.setBackgroundColor(Color.parseColor("#CCCCCC"));
            fila.addView(celda, parametrosCelda());
        }
        tabla.addView(fila);
    }

    public void crearFilas() {
        int index = 1;
        for (String[] datosFila : datos) {
            nuevaFila();
            fila.setBackgroundColor(
                    (index % 2 == 0) ? Color.parseColor("#E0E0E0") : Color.WHITE
            );

            for (String dato : datosFila) {
                nuevaCelda();
                celda.setText(dato);
                celda.setTextSize(14);
                fila.addView(celda, parametrosCelda());
            }
            tabla.addView(fila);
            index++;
        }
    }
    private TableRow.LayoutParams parametrosCelda () {
        TableRow.LayoutParams parametros = new TableRow.LayoutParams();
        parametros.setMargins(1, 1, 1, 1);
        parametros.weight = 1;
        return parametros;
    }
}