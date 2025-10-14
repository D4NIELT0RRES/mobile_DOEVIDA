package com.example.doevida.Util


import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.doevida.data.Hospital

fun abrirNoMaps(context: Context, h: Hospital) {
    // Se você tiver latitude/longitude no seu modelo:
    val lat = (h as? HasLatLng)?.latitude
    val lng = (h as? HasLatLng)?.longitude

    val uri: Uri = if (lat != null && lng != null) {
        // Com coordenadas e rótulo
        Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(h.nome)})")
    } else {
        // Sem coordenadas: consulta por nome + CEP (ou logradouro, se houver)
        val query = listOfNotNull(h.nome, h.cep).joinToString(" ")
        Uri.parse("geo:0,0?q=${Uri.encode(query)}")
    }

    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        // Se tiver Google Maps instalado, abre nele; senão, em qualquer app de mapas
        setPackage("com.google.android.apps.maps")
    }

    // fallback se o Maps não estiver instalado
    if (intent.resolveActivity(context.packageManager) == null) {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    } else {
        context.startActivity(intent)
    }
}

// Interface opcional caso você adicione lat/lng ao Hospital futuramente
interface HasLatLng {
    val latitude: Double?
    val longitude: Double?
}
