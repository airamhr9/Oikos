package com.example.oikos.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.OkHttpResponseListener
import com.example.oikos.MainActivity
import com.example.oikos.R
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import objects.DatosInmueble
import objects.Preferencia
import okhttp3.Response
import org.json.JSONArray

class preferences : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var cancelB : AppCompatButton
    lateinit var filterCard : CardView
    lateinit var aceptarB: AppCompatButton
    lateinit var tipoText : TextView
    lateinit var myPreferences : Preferencia

    lateinit var tCiudad : TextInputEditText
    lateinit var  tTipo : TextView
    lateinit var tPrecioMax : TextInputEditText
    lateinit var tPrecioMin : TextInputEditText
    lateinit var tHabs : TextInputEditText
    lateinit var tBaño : TextInputEditText
    lateinit var tSupMin : TextInputEditText
    lateinit var tSupMax : TextInputEditText
    lateinit var tGaraje : CheckBox







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        tCiudad = findViewById(R.id.filtro_ciudad)
        tTipo = findViewById(R.id.filter_tipo_text)
        tPrecioMax =  findViewById(R.id.filtro_precio_max)
        tPrecioMin =  findViewById(R.id.filtro_precio_min)
        tHabs =  findViewById(R.id.filtro_habitaciones)
        tBaño =  findViewById(R.id.filtro_baños)
        tSupMin = findViewById(R.id.filtro_superficie_min)
        tSupMax = findViewById(R.id.filtro_superficie_max)
         tGaraje =  findViewById(R.id.filtro_garaje)



        filterCard = findViewById(R.id.filter_search_card)
        myPreferences = intent.getSerializableExtra("preferencias") as Preferencia
        printFilters(myPreferences.toJson())


        aceptarB = findViewById(R.id.bAcpetar)
        aceptarB.setOnClickListener{
            putPreferences()

        }







        val tipoSpinner : AppCompatSpinner = findViewById(R.id.filtro_tipo)
        ArrayAdapter.createFromResource(
                applicationContext,
                R.array.spinner_values,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tipoSpinner.adapter = adapter
        }
        tipoText = findViewById(R.id.filter_tipo_text)
        tipoSpinner.onItemSelectedListener = this




        cancelB = findViewById(R.id.bCancelar)
        cancelB.setOnClickListener{
            finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tipoText.text = parent?.getItemAtPosition(position) as String
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun editarPreferencias(){


        val cityText = findViewById<TextInputEditText>(R.id.filtro_ciudad).text.toString()
        val precioMin = findViewById<TextInputEditText>(R.id.filtro_precio_min).text.toString()
        val precioMax = findViewById<TextInputEditText>(R.id.filtro_precio_max).text.toString()
        val habs = findViewById<TextInputEditText>(R.id.filtro_habitaciones).text.toString()
        val baños = findViewById<TextInputEditText>(R.id.filtro_baños).text.toString()
        val supMin = findViewById<TextInputEditText>(R.id.filtro_superficie_min).text.toString()
        val supMax = findViewById<TextInputEditText>(R.id.filtro_superficie_max).text.toString()
        val garaje = findViewById<CheckBox>(R.id.filtro_garaje).isChecked
        val tipo = findViewById<TextView>(R.id.filtro_tipo).text.toString()

        myPreferences.superficie_min = if( supMin == "" ) 0 else supMin.toInt()
        myPreferences.superficie_max = if(supMax == "") Int.MAX_VALUE else supMax.toInt()
        myPreferences.precio_max =  if(precioMax == "") Double.MAX_VALUE else precioMax.toDouble()
        myPreferences.precio_min =  if( precioMin == "" ) 0.0 else precioMin.toDouble()
        myPreferences.habitaciones = if( habs == "") null else habs.toInt()
        myPreferences.baños = if( habs == "") null else baños.toInt()
        myPreferences.garaje = garaje
        myPreferences.ciudad = cityText
        myPreferences.t

    }



    private fun putPreferences(){
        editarPreferencias(getWindow().getDecorView().findViewById(android.R.id.content))
        if ((this as MainActivity).isNetworkConnected()) {
            val query = AndroidNetworking.put("http://10.0.2.2:9000/api/preferencias/")
                    .addBodyParameter(myPreferences.toJson())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsOkHttpResponse(object: OkHttpResponseListener{
                        override fun onResponse(response: Response?) {
                            Log.d("EO",myPreferences.toString())
                            finish()
                        }

                        override fun onError(anError: ANError?) {
                            AlertDialog.Builder(this@preferences)
                                    .setIcon(android.R.drawable.ic_menu_search)
                                    .setTitle("Sin inmuebles en esta zona")
                                    .setMessage("Pruebe en otro lugar")
                                    .setPositiveButton("Ok"
                                    ) { _, _ ->}
                                    .show()
                        }


                    })



        }
    }


    fun printFilters(preferences: JsonObject){



        for (key in preferences.keySet()) {
            when (key) {
                "ciudad" -> tCiudad.setText("${preferences[key].asString}")
                "tipo" -> tTipo.setText("${preferences[key].asString}")
                "precioMin" -> tPrecioMin.setText("${preferences[key].asString}€")
                "precioMax" -> if(preferences[key] as Int != Int.MAX_VALUE) tPrecioMax.setText( " ${preferences[key].asString}€")
                "habitaciones" -> tHabs.setText("${preferences[key].asString}")
                "baños" -> tBaño.setText( "${preferences[key].asString}")
                "supMin" ->  tSupMin.setText("${preferences[key].asString}")
                "supMax" ->if(preferences[key] as Double != Double.MAX_VALUE) tSupMax.setText ("${preferences[key].asString}")
                "garaje" -> tGaraje.isChecked = (preferences[key].asBoolean)
            }
        }

    }


}
