package com.es.appmovil

import kotlinx.cinterop.BetaInteropApi
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UINavigationController
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.UIModalPresentationFullScreen
import platform.darwin.NSObject
import platform.Foundation.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Guarda un archivo de texto (formato CSV) temporalmente y presenta un diálogo
 * para que el usuario elija una ubicación donde exportarlo (versión iOS).
 * Utiliza UIDocumentPickerViewController para ofrecer al usuario una UI nativa
 * de exportación, permitiendo guardar el archivo en Archivos o compartirlo.
 *
 * @param data Contenido del archivo como texto plano.
 * @param filename Nombre del archivo que se desea exportar (ej. "reporte.csv").
 * @return true si el usuario seleccionó una ubicación y se exportó el archivo; false si canceló o ocurrió un error.
 * @note Crea primero el archivo en un directorio temporal y lo pasa al UIDocumentPickerViewController.
 */
@OptIn(BetaInteropApi::class)
actual suspend fun saveToDownloads(data: String, filename: String): Boolean {
    return suspendCoroutine { continuation ->
        val tempDir = NSTemporaryDirectory()
        val tempPath = tempDir + filename
        val fileUrl = NSURL.fileURLWithPath(tempPath)

        val nsString = NSString.create(string = data)
        val nsData = nsString.dataUsingEncoding(NSUTF8StringEncoding)
        val success = nsData?.writeToURL(fileUrl, atomically = true)

        if (success != null) {
            continuation.resume(false)
            return@suspendCoroutine
        }

        val picker = UIDocumentPickerViewController(
            forExportingURLs = listOf(fileUrl),
            asCopy = true
        )
        picker.shouldShowFileExtensions = true
        picker.modalPresentationStyle = UIModalPresentationFullScreen
        picker.delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentsAtURLs: List<*>) {
                continuation.resume(true)
            }

            override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
                continuation.resume(false)
            }
        }

        // Present the picker on the current visible view controller
        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
        val topVC = findTopViewController(rootVC)
        topVC?.presentViewController(picker, animated = true, completion = null)
    }
}

/**
 * Encuentra el UIViewController que está actualmente visible para poder presentar
 * otro controlador sobre él (por ejemplo, un UIDocumentPickerViewController).
 *
 * Recorre jerárquicamente UINavigationController, UITabBarController y presentedViewController.
 *
 * @param controller Controlador raíz desde el cual iniciar la búsqueda.
 * @return El controlador visible más arriba en la jerarquía, o null si no se encuentra.
 */
private fun findTopViewController(controller: UIViewController?): UIViewController? {
    return when (controller) {
        is UINavigationController -> findTopViewController(controller.visibleViewController)
        is UITabBarController -> findTopViewController(controller.selectedViewController)
        else -> controller?.presentedViewController?.let { findTopViewController(it) } ?: controller
    }
}