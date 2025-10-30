package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.FeedReaderContract.FeedEntry
import android.provider.BaseColumns
import androidx.navigation.fragment.findNavController

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: FeedReaderDbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        dbHelper = FeedReaderDbHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGuardar.setOnClickListener {
            guardarRegistro()
        }

        binding.buttonActualizar.setOnClickListener {
            actualizarRegistro()
        }

        binding.buttonBuscar.setOnClickListener {
            buscarRegistro()
        }

        binding.buttonEliminar.setOnClickListener {
            eliminarRegistro()
        }

        // CONEXIÓN DEL BOTÓN LISTAR (READ)
        binding.buttonListar.setOnClickListener {
            // Asume que tienes una acción de navegación definida en nav_graph.xml
            // para ir de FirstFragment a SecondFragment (donde listaremos los datos)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun guardarRegistro() {
        val db = dbHelper.writableDatabase

        val nombre = binding.editTextNombre.text.toString()
        val apellido = binding.editTextApellido.text.toString()

        if (nombre.isBlank() || apellido.isBlank()) {
            Toast.makeText(context, "Los campos no pueden estar vacíos.", Toast.LENGTH_SHORT).show()
            return
        }

        val values = ContentValues().apply {
            put(FeedEntry.column1, nombre)
            put(FeedEntry.column2, apellido)
        }

        val newRowId = db.insert(FeedEntry.nameTable, null, values)

        if (newRowId > 0) {
            Toast.makeText(context, "Registro guardado con ID: $newRowId", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(context, "Error al guardar el registro", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buscarRegistro() {
        val db = dbHelper.readableDatabase
        val id = binding.editTextId.text.toString()

        if (id.isBlank()) {
            Toast.makeText(context, "Ingrese un ID para buscar.", Toast.LENGTH_SHORT).show()
            return
        }

        val projection = arrayOf(FeedEntry.column1, FeedEntry.column2)
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id)

        val cursor = db.query(
            FeedEntry.nameTable,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        with(cursor) {
            if (moveToFirst()) {
                val nombre = getString(getColumnIndexOrThrow(FeedEntry.column1))
                val apellido = getString(getColumnIndexOrThrow(FeedEntry.column2))

                binding.editTextNombre.setText(nombre)
                binding.editTextApellido.setText(apellido)
                Toast.makeText(context, "Registro encontrado.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "ID $id no encontrado.", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            }
        }
        cursor.close()
    }

    private fun actualizarRegistro() {
        val db = dbHelper.writableDatabase

        val id = binding.editTextId.text.toString()
        val nombre = binding.editTextNombre.text.toString()
        val apellido = binding.editTextApellido.text.toString()

        if (id.isBlank() || nombre.isBlank() || apellido.isBlank()) {
            Toast.makeText(context, "Llene ID, Nombre y Apellido para actualizar.", Toast.LENGTH_SHORT).show()
            return
        }

        val values = ContentValues().apply {
            put(FeedEntry.column1, nombre)
            put(FeedEntry.column2, apellido)
        }

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id)

        val count = db.update(
            FeedEntry.nameTable,
            values,
            selection,
            selectionArgs
        )

        if (count > 0) {
            Toast.makeText(context, "Registro con ID $id actualizado.", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(context, "ID $id no encontrado para actualizar.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarRegistro() {
        val db = dbHelper.writableDatabase
        val id = binding.editTextId.text.toString()

        if (id.isBlank()) {
            Toast.makeText(context, "Ingrese un ID para eliminar.", Toast.LENGTH_SHORT).show()
            return
        }

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id)

        val deletedRows = db.delete(FeedEntry.nameTable, selection, selectionArgs)

        if (deletedRows > 0) {
            Toast.makeText(context, "Registro con ID $id eliminado.", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(context, "No se encontró el registro con ID $id.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        binding.editTextId.setText("")
        binding.editTextNombre.setText("")
        binding.editTextApellido.setText("")
        binding.editTextId.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbHelper.close()
        _binding = null
    }
}