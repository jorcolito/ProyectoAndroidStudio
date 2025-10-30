package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Buscamos nuestro nuevo botón "Enviar"
        binding.buttonSend.setOnClickListener {
            // 1. Obtenemos el texto de los campos
            val nombre = binding.editTextName.text.toString()
            val correo = binding.editTextEmail.text.toString()

            // 2. Creamos un "paquete" (Bundle) para enviar los datos
            val bundle = Bundle()
            bundle.putString("key_nombre", nombre)
            bundle.putString("key_correo", correo)

            // 3. Navegamos al SegundoFragment, enviando el paquete
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}