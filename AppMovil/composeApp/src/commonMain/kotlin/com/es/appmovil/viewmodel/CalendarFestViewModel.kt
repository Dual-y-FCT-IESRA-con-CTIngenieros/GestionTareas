package com.es.appmovil.viewmodel

import com.es.appmovil.viewmodel.DataViewModel.today
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class CalendarFestViewModel {


    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia atras
     */
    fun onMonthChangePrevious(month: DatePeriod) {
        today.value = today.value.minus(month)
    }

    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia delante
     */
    fun onMonthChangeFordward(month: DatePeriod) {
        today.value = today.value.plus(month)
    }

}