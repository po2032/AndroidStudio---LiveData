package com.example.livedataejer_mt
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class LibroViewModel : ViewModel() {
    // Variable para gestionar alta o modificación
    private var nuevoLibro: Boolean = false

    // LiveData para el listado de libros
    private var _listaLibros: MutableLiveData<MutableList<Libro>> = MutableLiveData(mutableListOf())
    val listaLibros: LiveData<MutableList<Libro>> get() = _listaLibros

    // LiveData para el libro seleccionado
    private var _libroSeleccionado = MutableLiveData<Libro>(Libro("", "", "", false))
    var libroSeleccionado = MutableLiveData<Libro>(Libro("", "", "", false))

    val actionbutton: LiveData<String> = libroSeleccionado.map { libro ->
        when (libro.titulo) {
            "" -> "Publicar" // Si no hay ningún libro seleccionado
            else -> "Modificar" // Si hay un libro seleccionado con un título
        }
    }

    init {
        this._listaLibros.value?.add(
            Libro(
                "The Lord of the Rings",
                "J.R. Tolkien",
                "1989",
                true
            )
        )
        this._listaLibros.value?.add(
            Libro(
                "Don Quijote de la Mancha",
                "Miguel de Cervantes",
                "1500 A.C",
                true
            )
        )
        this._listaLibros.value?.add(
            Libro(
                "Pinocho",
                "Alguien",
                "1976",
                true
            )
        )
    }

    // Método para actualizar el listado de libros
    private fun actualizarListaLibros() {
        // Obtener el valor actual de la lista de libros
        var values = this._listaLibros.value
        // Actualizar la lista de libros
        this._listaLibros.value = values
    }

    // Asignar un nuevo valor al libro seleccionado, notificar cambios
    private fun actualizarLibroSeleccionado() {
        this._libroSeleccionado.value = this._libroSeleccionado.value?.copy()
    }

    // Obtener libro seleccionado actual
    fun settSelected(item: Libro) {
        this._libroSeleccionado.value = item
        this.libroSeleccionado.value = item.copy()
        this.nuevoLibro = false
    }

    // Método para obtener la lista actual de libros
    fun obtenerListaActual(index: Int) {
        this._listaLibros.value?.let {
            this._libroSeleccionado.value = it.get(index)
            this.libroSeleccionado.value = it.get(index).copy()
            this.nuevoLibro = false
        }
    }

    // Método para gestionar alta o modificación
    fun setNuevoLibro() {
        this._libroSeleccionado.value = Libro("", "", "", false)
        this.libroSeleccionado.value = this._libroSeleccionado.value
        this.nuevoLibro = true
    }

    // Método principal para añadir o modificar libros
    // Método principal para añadir o modificar libros
    fun Update() {
        if (nuevoLibro) {
            // Si es un nuevo libro, agregarlo a la lista y notificar cambios
            this.nuevoLibro = false
            this.libroSeleccionado.value?.let {
                this._listaLibros.value?.add(it)
                actualizarListaLibros()
                Log.d("PRUEBAS","Se Añadio un libro")

                // Notificar al observador de listaLibros sobre el cambio
                _listaLibros.postValue(_listaLibros.value)
            }
        } else {
            // Si es una modificación, actualizar el libro seleccionado en la lista y notificar cambios
            this._libroSeleccionado.value?.let {
                it.titulo = libroSeleccionado.value?.let { it.titulo } ?: it.titulo
                it.autor = libroSeleccionado.value?.let { it.autor } ?: it.autor
                it.publicado = libroSeleccionado.value?.let { it.publicado } ?: it.publicado
                it.activo = libroSeleccionado.value?.let { it.activo } ?: it.activo

                this.actualizarListaLibros()
                Log.d("PRUEBAS","Se notifico al observador de actualizar lista")
                this.actualizarLibroSeleccionado()
                Log.d("PRUEBAS","Se notifico al observador de modificar libro: $it")

            }
        }
    }


    fun removerLibro(l: Libro) {
        this.libroSeleccionado.value?.let {
            this._listaLibros.value?.remove(it)
            actualizarListaLibros()
            Log.d("PRUEBAS", "Se elimino un libro de la lista $l")
        }
        // Notificar al observador de listaLibros sobre el cambio
        _listaLibros.postValue(_listaLibros.value)
        Log.d("PRUEBAS", "Se notifico al observador de la eliminacion")
    }
}