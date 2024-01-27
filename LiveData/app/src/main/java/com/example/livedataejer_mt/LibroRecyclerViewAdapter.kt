package com.example.livedataejer_mt

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.livedataejer_mt.databinding.FragmentLibroitemBinding

class LibroRecyclerViewAdapter(
    private val valores: MutableList<Libro>,
    private val viewModel: LibroViewModel
): RecyclerView.Adapter<LibroRecyclerViewAdapter.ViewHolder>() {

    // Función de clic para ser establecida desde fuera del adaptador
    var clic: ((Int, Libro) -> Unit)? = null

    // Crea y devuelve un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentLibroitemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // Rellena el ViewHolder con los datos en una posición específica
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = valores[position]

        // Establecer el número de ítem
        holder.numeroItem.text = position.toString()
        // Establecer el contenido del ítem (nombre del libro)
        holder.contenido.text = item.titulo

        // Configurar el clic de botón
        holder.boton.setOnClickListener {
            this.clic?.invoke(position, valores[position])
        }

        // Configurar el clic de botonEliminar
        holder.botonEliminar.setOnClickListener {
            viewModel.removerLibro(valores[position])
            notifyItemRemoved(position)
        }
    }

    // Devuelve el número total de ítems en el conjunto de datos
    override fun getItemCount(): Int = valores.size

    // ViewHolder representa cada ítem en la lista
    inner class ViewHolder(private val binding: FragmentLibroitemBinding) : RecyclerView.ViewHolder(binding.root) {
        // Elementos de la interfaz de usuario para este ítem
        val numeroItem: TextView = binding.itemNumber
        val contenido: TextView = binding.content
        val boton: Button = binding.boton
        val botonEliminar: Button = binding.botonEliminar
    }
}
