package com.es.appmovil.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.es.appmovil.database.Database
import com.es.appmovil.model.Employee
import com.es.appmovil.model.dto.EmployeeUpdateDTO
import com.es.appmovil.utils.DTOConverter.toEntity
import com.es.appmovil.utils.DTOConverter.toInsertDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * ViewModel para gestionar empleados: altas, bajas, edición y filtrado.
 */
class EmployeesDataViewModel : ViewModel() {

    // Lista de todos los empleados (se carga inicialmente desde DataViewModel)
    private val _employees = MutableStateFlow(DataViewModel.employees.value)
    val employees: MutableStateFlow<MutableList<Employee>> = _employees

    // Lista de empleados activos (sin fecha de baja o fecha de baja futura)
    private var _actualEmployees = MutableStateFlow<MutableList<Employee>>(mutableListOf())
    val actualEmployees: MutableStateFlow<MutableList<Employee>> = _actualEmployees

    // Lista de exempleados (fecha de baja pasada o incorrecta)
    private val _exEmployees = MutableStateFlow<MutableList<Employee>>(mutableListOf())
    val exEmployees: MutableStateFlow<MutableList<Employee>> = _exEmployees

    // Texto actual del filtro de búsqueda
    private var _filter: MutableStateFlow<String> = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    // Variables de edición de formulario
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName
    private val _idRol = MutableStateFlow("")
    val idRol: StateFlow<String> = _idRol
    private val _user = MutableStateFlow("")
    val user: StateFlow<String> = _user
    private val _domain = MutableStateFlow(DataViewModel.currentEmail.value)
    val domain: StateFlow<String> = _domain
    private var _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    private val _dateFrom = mutableStateOf("")
    val dateFrom = _dateFrom
    private val _dateTo = MutableStateFlow("")
    val dateTo: StateFlow<String> = _dateTo
    private val _idEmployee = MutableStateFlow("")
    val idEmployee: StateFlow<String> = _idEmployee
    private val _idCT = MutableStateFlow("")
    val idCT: StateFlow<String> = _idCT
    private val _idAirbus = MutableStateFlow("")
    val idAirbus: StateFlow<String> = _idAirbus

    /** Setters de los campos de formulario **/
    fun onChangeName(newName: String) {
        _name.value = newName
    }

    fun onChangeLastName(newLastName: String) {
        _lastName.value = newLastName
    }

    fun onChangeUser(newUser: String) {
        _user.value = newUser
    }

    // Al cambiar el email concatenamos el dominio
    fun onChangeEmail(newEmail: String) {
        _email.value = newEmail + _domain.value
    }

    fun onChangeIdCT(newIdCT: String) {
        _idCT.value = newIdCT
    }

    fun onChangeIdAirbus(newIdAirbus: String) {
        _idAirbus.value = newIdAirbus
    }

    /**
     * Ordena los empleados en activos y exempleados según su fecha de baja.
     */
    fun orderEmployees() {
        _actualEmployees.value.clear()
        _exEmployees.value.clear()

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        _employees.value.forEach { employee ->
            val dateTo = employee.dateTo

            if (dateTo.isNullOrEmpty()) {
                _actualEmployees.value.add(employee)
            } else {
                try {
                    val dateToParsed = LocalDate.parse(dateTo)
                    if (dateToParsed >= today) {
                        _actualEmployees.value.add(employee)
                    } else {
                        _exEmployees.value.add(employee)
                    }
                } catch (e: Exception) {
                    // Si el formato es inválido lo metemos como exempleado
                    _exEmployees.value.add(employee)
                }
            }
        }
    }

    /**
     * Añade un nuevo empleado a la base de datos y lo registra.
     */
    fun addEmployee(newEmployee: Employee) {
        viewModelScope.launch {
            Database.addEmployee(newEmployee.toInsertDTO())
            Database.register(newEmployee.email, "ct1234") // registro inicial con password por defecto

            // Lo añadimos a DataViewModel global
            DataViewModel.employees.value.add(newEmployee)
            orderEmployees()
        }
    }

    /**
     * Marca un empleado como baja (update de datos).
     */
    fun removeEmployee(removeEmployee: EmployeeUpdateDTO) {
        viewModelScope.launch {
            FullScreenLoadingManager.showLoader()

            Database.updateEmployee(removeEmployee)

            val updatedEmployee = removeEmployee.toEntity()

            val employeeToRemove = DataViewModel.employees.value
                .find { it.idEmployee == updatedEmployee.idEmployee }

            DataViewModel.employees.value.remove(employeeToRemove)
            DataViewModel.employees.value.add(updatedEmployee)

            orderEmployees()

            FullScreenLoadingManager.hideLoader()
        }
    }

    /**
     * Cambia el valor del filtro de búsqueda de empleados.
     */
    fun changeFilter(value: String) {
        _filter.value = value
    }
}
