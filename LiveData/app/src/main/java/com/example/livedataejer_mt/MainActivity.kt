package com.example.livedataejer_mt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.livedataejer_mt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: LibroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        setContentView(binding.root)

        // Cambiamos el nombre del adaptador y el ViewModel
        binding.listado.layoutManager = GridLayoutManager(this, 2)
        binding.listado.adapter = viewModel.listaLibros.value?.let {
            LibroRecyclerViewAdapter(it,viewModel)
        }
        binding.libroViewModel = viewModel

        if (binding.listado.adapter is LibroRecyclerViewAdapter) {
            // Cambiamos el nombre de las funciones y variables
            (binding.listado.adapter as LibroRecyclerViewAdapter).clic = { position, libro ->
                run {
                    this.viewModel.settSelected(libro)
                }
            }

            viewModel.listaLibros.observe(this, Observer { listado ->
                binding.listado.adapter?.notifyDataSetChanged()
                Log.d("PRUEBAS MAIN","Se notifico de Cambios $listado")

            })
        }

        binding.actualizar.setOnClickListener() {
            this.viewModel.Update()
            Log.d("PRUEBAS MAIN","click en boton modificar o añadir")
        }

        binding.floatingActionButton.setOnClickListener {
            this.viewModel.setNuevoLibro()
            Log.d("PRUEBAS MAIN","click en boton publicar nuevo libro")

        }
    }
}
