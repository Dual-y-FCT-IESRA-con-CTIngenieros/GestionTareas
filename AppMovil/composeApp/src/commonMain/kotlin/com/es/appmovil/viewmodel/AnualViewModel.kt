package com.es.appmovil.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.es.appmovil.model.UserYearData
import com.es.appmovil.model.dto.CalendarYearDTO
import com.es.appmovil.model.dto.TimeCodeDTO
import com.es.appmovil.viewmodel.DataViewModel.employee
import com.es.appmovil.viewmodel.DataViewModel.employeesYearData
import com.es.appmovil.viewmodel.DataViewModel.today
import ir.ehsannarmani.compose_charts.models.Bars
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

/**
 * ViewModel responsable de calcular y preparar los datos anuales del empleado
 * para visualización gráfica y generación de reportes. Incluye manejo de índices
 * semestrales, cálculo de horas trabajadas, horas teóricas y agrupación por meses.
 */
class AnualViewModel {

    // Fuente de actividades de los empleados y time codes
    private val employeeActivities = DataViewModel.employeeActivities
    private val timeCodes: StateFlow<List<TimeCodeDTO>> = DataViewModel.timeCodes

    // Lista de barras agrupadas por mes (semestre 1 o 2)
    private val _bars = MutableStateFlow<List<Bars>>(emptyList())
    val bars: StateFlow<List<Bars>> = _bars

    // Índice del semestre actual (1 o 2)
    private var _index = MutableStateFlow(setIndex())
    val index: StateFlow<Int> = _index

    /**
     * Establece el índice inicial del semestre en función del mes actual.
     * También genera las barras correspondientes a dicho semestre.
     *
     * @return 1 si es de enero a junio, 2 si es de julio a diciembre.
     */
    private fun setIndex(): Int {
        val current = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val month = current.monthNumber
        val i = if (month <= 6) 1 else 2
        generarBarrasPorMes(i)
        return i
    }

    /**
     * Recupera los datos anuales del empleado actual para el año en curso.
     *
     * @param employeeId ID del empleado.
     * @return Datos anuales del usuario si existen, o null si no hay coincidencia.
     */
    fun getEmployeeYearData(employeeId: Int): UserYearData? {
        val currentYear = today.value.year
        return employeesYearData.value.firstOrNull {
            it.idEmployee == employeeId && it.year == currentYear
        }
    }

    /**
     * Cambia el semestre (índice) y regenera las barras correspondientes.
     *
     * @param i Índice del semestre (1 o 2).
     */
    fun changeIndex(i: Int) {
        _index.value = i
        generarBarrasPorMes(_index.value)
    }

    /**
     * Calcula las horas trabajadas por mes del empleado actual, agrupadas de enero a diciembre.
     *
     * @return Lista con 12 elementos correspondientes a cada mes.
     */
    fun calcularHorasPorMes(): List<Double> {
        val hourMonth = employeeActivities.value
            .filter { it.idEmployee == employee.idEmployee }
            .groupBy { it.date.split("-")[1].toInt() - 1 }
            .mapValues { (_, timeMonth) ->
                timeMonth.sumOf { it.time.toDouble() }
            }
        val teorico = calcularHorasTeoricasPorMes(generarCalendar())

        return (0..11).map { mes ->
            hourMonth[mes] ?: teorico[mes]
        }
    }

    /**
     * Calcula las horas teóricas de trabajo por mes basándose en un calendario laboral.
     *
     * @param calendar Calendario laboral que incluye días festivos.
     * @return Lista de 12 valores correspondientes a las horas por mes.
     */
    fun calcularHorasTeoricasPorMes(calendar: CalendarYearDTO): List<Double> {
        val festive = calendar.date.map { LocalDate.parse(it) }.toSet()
        val dayHours = 8.0
        val anio = calendar.date[0].split("-")[0].toIntOrNull() ?: 0

        val firstDay = LocalDate(anio, 1, 1)
        val lastDay = LocalDate(anio, 12, 31)

        val horasPorMes = MutableList(12) { 0.0 }

        var date = firstDay
        while (date <= lastDay) {
            val isWorkDay = date.dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY
            val isFestive = date in festive

            if (isWorkDay && !isFestive) {
                val mesIndex = date.monthNumber - 1
                horasPorMes[mesIndex] += dayHours
            }

            date = date.plus(1, DateTimeUnit.DAY)
        }

        return horasPorMes
    }

    /**
     * Genera un calendario laboral del año 2025 con los días festivos predefinidos.
     *
     * @return Objeto CalendarYearDTO con los días festivos de 2025.
     */
    fun generarCalendar(): CalendarYearDTO {
        val festivos2025 = listOf(
            // Enero
            "2025-01-01", // Año nuevo
            "2025-01-06", // Reyes
            "2025-01-20", // Festivo genérico

            // Febrero
            "2025-02-14", // Festivo opcional
            "2025-02-28", // Andalucía (España)

            // Marzo
            "2025-03-17", // San Patricio
            "2025-03-19", // San José
            "2025-03-31", // Lunes Santo

            // Abril
            "2025-04-01", // Martes Santo
            "2025-04-02", // Miércoles Santo
            "2025-04-03", // Jueves Santo
            "2025-04-04", // Viernes Santo
            "2025-04-25", // San Marcos

            // Mayo
            "2025-05-01", // Día del Trabajador
            "2025-05-15", // San Isidro
            "2025-05-30", // Canarias (España)

            // Junio
            "2025-06-09", // Lunes de Pascua Granada
            "2025-06-24", // San Juan

            // Julio
            "2025-07-25", // Santiago Apóstol

            // Agosto
            "2025-08-15", // Asunción

            // Septiembre
            "2025-09-08", // Día de Extremadura
            "2025-09-11", // Cataluña
            "2025-09-24", // Festivo local

            // Octubre
            "2025-10-09", // Valencia
            "2025-10-12", // Fiesta Nacional de España

            // Noviembre
            "2025-11-01", // Todos los Santos
            "2025-11-09", // Almudena
            "2025-11-24", // Festivo opcional

            // Diciembre
            "2025-12-06", // Constitución
            "2025-12-08", // Inmaculada
            "2025-12-24", // Nochebuena
            "2025-12-25", // Navidad
            "2025-12-26", // San Esteban
            "2025-12-29", // Cierre navideño
            "2025-12-30", // Cierre navideño
            "2025-12-31"  // Fin de año
        )

        return CalendarYearDTO(1, festivos2025)
    }

    /**
     * Genera los datos de barras agrupados por mes, basados en las actividades del semestre indicado.
     * Cada barra contiene una lista de valores por tipo de código de tiempo.
     *
     * @param i Índice del semestre: 1 (enero-junio) o 2 (julio-diciembre).
     */
    private fun generarBarrasPorMes(i: Int) {
        val timeCodeMap = timeCodes.value.associateBy { it.idTimeCode }

        val actividadesPorMes =
            employeeActivities.value.filter { it.idEmployee == employee.idEmployee }.groupBy {
                val fecha = LocalDate.parse(it.date)
                fecha.monthNumber to fecha.year
            }

        val current = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val year = current.year

        val range = if (i == 1) (1..6) else (7..12)

        _bars.value = range.map { mes ->
            val claveMes = mes to year
            val actividadesDelMes = actividadesPorMes[claveMes]

            val dataPorTimeCode = actividadesDelMes?.groupBy { it.idTimeCode }
                ?.mapNotNull { (idTimeCode, listaActividades) ->
                    val timeCode = timeCodeMap[idTimeCode] ?: return@mapNotNull null
                    Bars.Data(
                        label = timeCode.desc,
                        value = listaActividades.sumOf { it.time.toDouble() },
                        color = SolidColor(Color(timeCode.color))
                    )
                } ?: emptyList()

            Bars(
                label = LocalDate(year, mes, 1).month.name.take(3).lowercase()
                    .replaceFirstChar { it.uppercase() },
                values = dataPorTimeCode
            )
        }
    }
}