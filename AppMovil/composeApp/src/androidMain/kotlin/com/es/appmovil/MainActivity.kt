package com.es.appmovil

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContextHolder.context = applicationContext

        setContent {
            App()
        }
    }
}

@SuppressLint("StaticFieldLeak")
object AppContextHolder {
    lateinit var context: Context
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}