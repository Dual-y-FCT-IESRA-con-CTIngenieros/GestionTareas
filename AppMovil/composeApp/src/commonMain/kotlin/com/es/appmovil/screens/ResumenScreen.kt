package com.es.appmovil.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.es.appmovil.viewmodel.CalendarViewModel
import com.es.appmovil.viewmodel.DataViewModel.getHours
import com.es.appmovil.viewmodel.DataViewModel.today
import com.es.appmovil.viewmodel.DayMenuViewModel
import com.es.appmovil.viewmodel.ResumeViewmodel
import com.es.appmovil.widgets.ActionButton
import com.es.appmovil.widgets.BarItem
import com.es.appmovil.widgets.BottomNavigationBar
import com.es.appmovil.widgets.DayDialog
import com.es.appmovil.widgets.LegendButton
import com.es.appmovil.widgets.ResumenHorasAnual
import com.es.appmovil.widgets.ResumenHorasMensual
import com.es.appmovil.widgets.SimpleBarChart
import com.es.appmovil.widgets.TodaySummaryCard
import com.es.appmovil.widgets.ProgressThermometer
import com.es.appmovil.widgets.RecentRecordsList
import com.es.appmovil.widgets.RecordUi
import com.es.appmovil.widgets.TopBar

/**
 * Pantalla que fusiona el resumen mensual y anual en una sola vista.
 */
class ResumenScreen : Screen {
    @Composable
    override fun Content() {
        val resumeViewmodel = ResumeViewmodel()
        val navigator = LocalNavigator.currentOrThrow
        val calendarViewmodel = CalendarViewModel()
        val showDialog by calendarViewmodel.showDialog.collectAsState()
        val dayMenuViewModel = DayMenuViewModel()

        MaterialTheme {
            Scaffold(
                topBar = { TopBar(navigator, title = "Resumen", rightContent = { LegendButton(resumeViewmodel) }) },
                bottomBar = { BottomNavigationBar(navigator) },
                floatingActionButton = { ActionButton { calendarViewmodel.changeDialog(true) } },
                floatingActionButtonPosition = FabPosition.Center,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                getHours()
                Column(Modifier.padding(16.dp).fillMaxWidth()) {
                    Text("Resumen", fontWeight = FontWeight.Black, fontSize = 25.sp)
                    Spacer(Modifier.size(10.dp))

                    // Conteo de horas y resumenes
                    // Las horas realizadas se muestran en Home; aquí sólo mostramos los resúmenes
                    Spacer(Modifier.size(8.dp))

                    Text("Resumen mensual", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.size(10.dp))

                    // Reemplazamos visualmente parte de la sección por los nuevos widgets (UI-only)
                    TodaySummaryCard(todayHours = 8f, weekHours = 40f)
                    Spacer(Modifier.size(8.dp))
                    ProgressThermometer(currentHours = 32f, targetHours = 160f, onRegisterClick = { calendarViewmodel.changeDialog(true) })

                    Spacer(Modifier.size(16.dp))

                    // Añadimos gráfica simple y lista de registros recientes (UI-only, datos de ejemplo)
                    SimpleBarChart(items = listOf(
                        BarItem("Lun", 8f),
                        BarItem("Mar", 6f),
                        BarItem("Mie", 7.5f),
                        BarItem("Jue", 4f),
                        BarItem("Vie", 7f)
                    ))

                    Spacer(Modifier.size(12.dp))

                    RecentRecordsList(records = listOf(
                        RecordUi("1", "Entrada salida", "08:00 - 17:00"),
                        RecordUi("2", "Salida temprana", "08:00 - 13:00")
                    ))

                    Spacer(Modifier.size(24.dp))

                    Text("Resumen anual", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.size(10.dp))
                    ResumenHorasAnual(resumeViewmodel)
                }
                DayDialog(showDialog, today.value, dayMenuViewModel, calendarViewmodel) { calendarViewmodel.changeDialog(it) }
            }
        }
    }
}
