package com.example.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practico1nuevo.R
import com.example.practico1nuevo.fragment.TopNoteFragment
import kotlin.random.Random

class NotaAdapter(
    private val noteList: ArrayList<String>,
    private val fragment: TopNoteFragment
    ): RecyclerView.Adapter<NotaAdapter.NoteViewHolder>(), OnContactClickListener {

    // Lista de colores para cada nota del recuadro de mi list item
    private val noteColors = ArrayList<Int>()
    private var noteToEdit: String? = null
    init {
        // Inicializamos todos los colores de las notas con un color predeterminado y no cambie a la hora de agregar una nueva nota o editar
        noteList.forEach { note ->
            noteColors.add(Color.WHITE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return NoteViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        val color = noteColors[position]
        holder.bind(note, color, this)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun addNote(note: String) {
        noteList.add(0, note)
        noteColors.add(0, Color.WHITE)
        notifyItemInserted(0)
    }
    fun deleteNote(note: String) {
        val index = noteList.indexOf(note)
        if (index != -1) {
            noteList.removeAt(index)
            noteColors.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    fun updateNote(newNote: String) {
        noteToEdit?.let { note ->
            val index = noteList.indexOf(note)
            if (index != -1) {
                noteList[index] = newNote
                notifyItemChanged(index)
            }
        }
    }

    fun updateNoteColor(position: Int, newColor: Int) {
        noteColors[position] = newColor
        notifyItemChanged(position)
    }

    class NoteViewHolder(itemView: View, private val adapter: NotaAdapter) : RecyclerView.ViewHolder(itemView) {
        private val lblNoteItem: TextView = itemView.findViewById(R.id.lblNoteItem)
        private val btnEditNoteItem: ImageButton = itemView.findViewById(R.id.btnEditNote)
        private val btnDeleteNoteItem: ImageButton = itemView.findViewById(R.id.btnDeleteNote)
        private val btnColor: Button = itemView.findViewById(R.id.btnColor)


        fun bind(note: String, color: Int, listener: NotaAdapter) {
            lblNoteItem.text = note
            lblNoteItem.setBackgroundColor(color)

            btnEditNoteItem.setOnClickListener {
                listener.onNoteEditClickListener(note)
            }
            btnDeleteNoteItem.setOnClickListener {
                listener.onNoteDeleteClickListener(note)
            }
            btnColor.setOnClickListener {
                val newColor = getRandomColor()
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    adapter.updateNoteColor(position, newColor)
                    lblNoteItem.setBackgroundColor(newColor)
                }
            }
        }

        private fun getRandomColor(): Int {
            val colors = listOf(
                Color.YELLOW, Color.BLUE, Color.GREEN, Color.RED, Color.CYAN,
                Color.MAGENTA, Color.DKGRAY, Color.LTGRAY, Color.BLACK, Color.WHITE
            )
            return colors[Random.nextInt(colors.size)]
        }
    }

    override fun onNoteEditClickListener(note: String) {
        // Asigna la nota a editar a la variable global
        noteToEdit = note
        fragment.loadNoteToEdit(note) // Llamar al fragmento para cargar la nota a editar
    }

    override fun onNoteDeleteClickListener(note: String) {
        deleteNote(note)
    }


}
public interface OnContactClickListener {
    fun onNoteEditClickListener(note: String)
    fun onNoteDeleteClickListener(note: String)

}