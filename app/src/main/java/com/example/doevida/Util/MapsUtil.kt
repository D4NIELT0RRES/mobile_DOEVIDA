package com.example.doevida.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.doevida.model.HospitaisCards

fun abrirNoMaps(context: Context, hospital: HospitaisCards) {
    val query = "${hospital.nomeHospital} ${hospital.endereco}"
    val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://maps.google.com/maps?q=${Uri.encode(query)}")
        )
        context.startActivity(browserIntent)
    }
}

fun ligarPara(context: Context, telefone: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$telefone")
    }
    context.startActivity(intent)
}

fun abrirNoMapsPorCEP(context: Context, hospital: HospitaisCards) {
    val query = if (hospital.cep != null) {
        "${hospital.nomeHospital} ${hospital.cep}"
    } else {
        "${hospital.nomeHospital} ${hospital.endereco}"
    }
    val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://maps.google.com/maps?q=${Uri.encode(query)}")
        )
        context.startActivity(browserIntent)
    }
}
