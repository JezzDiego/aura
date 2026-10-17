package com.example.aura.data.local.datasource

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

/**
 * Guarda as fotos comprovatórias dos relatos no armazenamento interno do app,
 * para que continuem disponíveis offline (mesmo que a imagem original seja apagada).
 */
class PhotoLocalDataSource(private val context: Context) {

    private val directory: File
        get() = File(context.filesDir, "review_photos").apply { if (!exists()) mkdirs() }

    /** Copia o conteúdo de [sourceUri] e retorna a URI `file://` da cópia. */
    fun save(sourceUri: String): String {
        val target = File(directory, "${UUID.randomUUID()}.jpg")
        val input = context.contentResolver.openInputStream(Uri.parse(sourceUri))
            ?: throw IllegalStateException("Não foi possível abrir a imagem selecionada.")
        input.use { source -> target.outputStream().use { source.copyTo(it) } }
        return Uri.fromFile(target).toString()
    }

    fun delete(uri: String) {
        val path = Uri.parse(uri).path ?: return
        val file = File(path)
        if (file.parentFile == directory) file.delete()
    }
}
