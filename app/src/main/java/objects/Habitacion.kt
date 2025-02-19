package objects

import com.google.gson.JsonObject
import java.io.Serializable

class Habitacion(id: Int,
                 disponible: Boolean,
                 tipo: String,
                 superficie: Int,
                 precio: Double,
                 propietario: Usuario,
                 descripcion: String,
                 direccion: String,
                 ciudad: String,
                 latitud: Double,
                 longitud: Double,
                 fecha : String,
                 contadorVisitas : Int,
                 imagenes: ArrayList<String>,
                 habitaciones: Int,
                 baños: Int,
                 garaje: Boolean,
                 var numCompañeros: Int,
) : Serializable, Piso(id, disponible, tipo, superficie, precio, propietario, descripcion,
        direccion, ciudad, latitud, longitud, fecha, contadorVisitas, imagenes, habitaciones, baños, garaje) {

    override fun toJson(): JsonObject {
        val result = super.toJson()
        result.addProperty("numCompañeros", numCompañeros)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Habitacion

        if (numCompañeros != other.numCompañeros) return false

        return true
    }

    companion object {
        fun fromJson(jsonObject: JsonObject): Habitacion {
            val id = jsonObject.get("id").asInt
            val disponible = jsonObject.get("disponible").asBoolean
            val tipo = jsonObject.get("tipo").asString
            val superficie = jsonObject.get("superficie").asInt
            val precio = jsonObject.get("precio").asDouble
            val propietario = Usuario.fromJson(jsonObject.getAsJsonObject("propietario"))
            val descripcion = jsonObject.get("descripcion").asString.toString()
            val direccion = jsonObject.get("direccion").asString
            val ciudad = jsonObject.get("ciudad").asString
            val latitud = jsonObject.get("latitud").asDouble
            val longitud = jsonObject.get("longitud").asDouble

            val contadorVisitas = jsonObject.get("contadorVisitas").asInt
            val imagenes = jsonObject.get("imagenes").asJsonArray
            val imageArray = ArrayList<String>()
            imagenes.forEach {
                imageArray.add(it.asString)
            }
            val fecha = jsonObject.get("fecha").asString
            val habitaciones = jsonObject.get("habitaciones").asInt
            val baños = jsonObject.get("baños").asInt
            val garaje = jsonObject.get("garaje").asBoolean
            val numCompañeros = jsonObject.get("numCompañeros").asInt

            return Habitacion(id, disponible, tipo, superficie, precio, propietario, descripcion,
                    direccion, ciudad, latitud, longitud, fecha, contadorVisitas, imageArray, habitaciones, baños, garaje, numCompañeros)
        }
    }
}