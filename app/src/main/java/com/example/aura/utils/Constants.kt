package com.example.aura.utils

object Constants {
    const val APP_NAME = "Entrada Livre"
    const val PACKAGE_NAME = "com.example.aura"

    /** URI de um drawable empacotado no app (carregável pelo Coil). */
    fun drawableUri(name: String): String = "android.resource://$PACKAGE_NAME/drawable/$name"
}
