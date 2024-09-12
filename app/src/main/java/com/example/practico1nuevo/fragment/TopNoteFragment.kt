package com.example.practico1nuevo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practico1nuevo.R
import com.example.ui.adapters.NotaAdapter
import com.example.ui.adapters.OnContactClickListener


/**
 * A simple [Fragment] subclass.
 * Use the [TopNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopNoteFragment : Fragment(), OnContactClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NotaAdapter
    private lateinit var btnGuardar: Button
    private lateinit var txtNewNote: EditText
    private var noteToEdit: String? = null
    private var isEditing = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_top_note, container, false)
        recyclerView = view.findViewById(R.id.rvListNote)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        txtNewNote = view.findViewById(R.id.txtNewNote)


        noteAdapter = NotaAdapter(arrayListOf(),this)
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context).apply {
            orientation = LinearLayoutManager.VERTICAL }

        btnGuardar.setOnClickListener {
            if (isEditing) {
                // Actualizar la nota si estamos editando
                val updatedNote = txtNewNote.text.toString()
                if (updatedNote.isNotEmpty()) {
                    noteToEdit?.let { note ->
                        noteAdapter.updateNote(updatedNote)  // Actualizar la nota en el adaptador
                    }
                    txtNewNote.text.clear()  // Limpiar el campo de texto
                    resetGuardarBoton()  // Resetear el botón a su comportamiento original
                }
            } else {
                // Crear una nueva nota si no estamos editando
                val newNote = txtNewNote.text.toString()
                if (newNote.isNotEmpty()) {
                    noteAdapter.addNote(newNote)
                    txtNewNote.text.clear()
                }
            }
        }

        return view
    }
    // Restablecer el comportamiento del botón "Guardar" después de editar
    private fun resetGuardarBoton() {
        isEditing = false
        noteToEdit = null
        btnGuardar.text = "Guardar"  // Cambia el texto si lo deseas
    }

    // Este es el método que se encargará de cargar la nota en el campo EditText para editarla
    fun loadNoteToEdit(note: String) {
        isEditing = true
        noteToEdit = note
        txtNewNote.setText(note)  // Cargar el texto de la nota en el EditText
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TopNoteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = TopNoteFragment()
        fun loadNoteToEdit(topNoteFragment: TopNoteFragment, note: String) {
            topNoteFragment.txtNewNote.setText(note)
            topNoteFragment.btnGuardar.setOnClickListener {
                val updatedNote = topNoteFragment.txtNewNote.text.toString()
                if (updatedNote.isNotEmpty()) {
                    topNoteFragment.noteAdapter.updateNote(updatedNote)  // Actualizar la nota en el adaptador
                    topNoteFragment.txtNewNote.text.clear()  // Limpiar el campo de texto
                }
            }
        }
    }

    override fun onNoteEditClickListener(note: String) {
        loadNoteToEdit(note)  // Llamar a la función para cargar la nota en el EditText
        btnGuardar.text = "Actualizar"  // Cambia el texto del botón para indicar que es una actualización
    }

    override fun onNoteDeleteClickListener(note: String) {
        val adapter = recyclerView.adapter as NotaAdapter
        adapter.deleteNote(note)
    }
}