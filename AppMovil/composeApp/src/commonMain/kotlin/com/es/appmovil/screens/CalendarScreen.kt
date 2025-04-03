package com.es.appmovil.screens

import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

class CalendarScreen:Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Button(onClick = {
            navigator?.push(ResumeScreen())
        }){}
    }
}