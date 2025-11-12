package com.example.aura.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatarDataParaBR(dataIso: String?, formatoExtenso: Boolean = false): String {
    if (dataIso.isNullOrEmpty()) {
        return "Data indisponível"
    }

    return try {
        val instant = Instant.parse(dataIso)
        val dataLocal = instant.atZone(ZoneId.systemDefault()).toLocalDate()

        if (formatoExtenso) {
            val formatoExtensoBR = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
            dataLocal.format(formatoExtensoBR)
        } else {
            val formatoPadraoBR = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("pt", "BR"))
            dataLocal.format(formatoPadraoBR)
        }
    } catch (e: Exception) {
        "Data inválida"
    }
}