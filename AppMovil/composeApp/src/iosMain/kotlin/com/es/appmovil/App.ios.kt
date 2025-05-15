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

private fun findTopViewController(controller: UIViewController?): UIViewController? {
    return when (controller) {
        is UINavigationController -> findTopViewController(controller.visibleViewController)
        is UITabBarController -> findTopViewController(controller.selectedViewController)
        else -> controller?.presentedViewController?.let { findTopViewController(it) } ?: controller
    }
}