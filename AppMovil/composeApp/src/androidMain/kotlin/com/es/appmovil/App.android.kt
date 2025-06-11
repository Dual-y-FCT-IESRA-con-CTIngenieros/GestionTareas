package com.es.appmovil

import android.annotation.SuppressLint
import android.content.ContentValues
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Guarda un archivo de texto (formato CSV) en la carpeta de Descargas del dispositivo Android.
 *
 * Esta función utiliza la API de `MediaStore` para insertar el archivo en el almacenamiento externo
 * de manera moderna y segura (recomendado desde Android Q en adelante).
 *
 * @param data Contenido del archivo en formato `String`.
 * @param filename Nombre que tendrá el archivo (por ejemplo: "reporte.csv").
 * @return `true` si el archivo fue guardado correctamente, `false` si ocurrió un error.
 *
 * @throws Exception Captura y reporta cualquier excepción lanzada durante la escritura del archivo.
 */
@SuppressLint("InlinedApi")
actual suspend fun saveToDownloads(data: String, filename: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val context = AppContextHolder.context
            val resolver = context.contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, filename)
                put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val itemUri = resolver.insert(collection, contentValues) ?: return@withContext false

            resolver.openOutputStream(itemUri)?.use { outputStream ->
                outputStream.write(data.toByteArray())
            }

            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(itemUri, contentValues, null, null)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}