package com.es.appmovil.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResumeViewmodel {

    private var _currentHours = MutableStateFlow(0)
    val currentHours: StateFlow<Int> = _currentHours

    private var _dailyHours = MutableStateFlow(0)
    val dailyHours: StateFlow<Int> = _dailyHours

    private var _currentDay = MutableStateFlow(0)
    val currentDay: StateFlow<Int> = _currentDay

}