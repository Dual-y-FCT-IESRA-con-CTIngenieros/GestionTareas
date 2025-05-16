package com.es.appmovil.viewmodel

import com.es.appmovil.viewmodel.DataViewModel.today
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class CalendarManageViewModel {

    private var _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private var _weekIndex = MutableStateFlow(0)
    val weekIndex: StateFlow<Int> = _weekIndex

    private val _weeksInMonth = MutableStateFlow(generateWeeksForMonth(today.value.year, today.value.month))
    val weeksInMonth:StateFlow<List<Pair<LocalDate, LocalDate>>> = _weeksInMonth

    private val _locked = MutableStateFlow<List<Pair<LocalDate, LocalDate>>>(emptyList())
    val locked: StateFlow<List<Pair<LocalDate, LocalDate>>> = _locked

    fun changeDialog(bool:Boolean) {
        _showDialog.value = bool
    }

    fun changeWeekIndex(index:Int) {
        _weekIndex.value = index
    }

    fun lockWeek(week: Pair<LocalDate, LocalDate>) {
        _locked.value += week
    }

    fun unlockWeek(week: Pair<LocalDate, LocalDate>) {
        _locked.value -= week
    }

    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia atras
     */
    fun onMonthChangePrevious(month: DatePeriod) {
        today.value = today.value.minus(month)
        _weeksInMonth.value = generateWeeksForMonth(today.value.year, today.value.month)
    }

    /**
     * Función para cambiar el mes que se muestra en el calendario
     * @param month Numero de meses que se van a cambiar hacia delante
     */
    fun onMonthChangeFordward(month: DatePeriod) {
        today.value = today.value.plus(month)
        _weeksInMonth.value = generateWeeksForMonth(today.value.year, today.value.month)
    }

    private fun generateWeeksForMonth(year: Int, month: Month): List<Pair<LocalDate, LocalDate>> {
        val firstDayOfMonth = LocalDate(year, month, 1)
        val lastDayOfMonth = LocalDate(year, month, getDaysInMonth(year, month))

        // Buscar el lunes anterior (o el mismo día si ya es lunes)
        var current = firstDayOfMonth
        while (current.dayOfWeek != DayOfWeek.MONDAY) {
            current = current.minus(DatePeriod(days = 1))
        }

        val weeks = mutableListOf<Pair<LocalDate, LocalDate>>()

        while (current <= lastDayOfMonth) {
            val startOfWeek = current
            val endOfWeek = current.plus(DatePeriod(days = 6))

            // Solo agregamos si algún día de la semana está dentro del mes
            if (startOfWeek <= lastDayOfMonth && endOfWeek >= firstDayOfMonth) {
                weeks.add(startOfWeek to endOfWeek)
            }

            // Avanzamos a la siguiente semana
            current = current.plus(DatePeriod(days = 7))
        }

        return weeks
    }

    private fun getDaysInMonth(year: Int, month: Month): Int {
        return when (month) {
            Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY,
            Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31

            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30

            Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
            else -> -1
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }



}