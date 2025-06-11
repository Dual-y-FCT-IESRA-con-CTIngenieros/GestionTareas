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

class EmployeesDataViewModel : ViewModel() {
    private val _employees = MutableStateFlow(DataViewModel.employees.value)
    val employees: MutableStateFlow<MutableList<Employee>> = _employees

    private var _actualEmployees = MutableStateFlow<MutableList<Employee>>(mutableListOf())
    val actualEmployees: MutableStateFlow<MutableList<Employee>> = _actualEmployees

    private val _exEmployees = MutableStateFlow<MutableList<Employee>>(mutableListOf())
    val exEmployees: MutableStateFlow<MutableList<Employee>> = _exEmployees

    private var _filter: MutableStateFlow<String> = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

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

    fun onChangeName(newName: String) {
        _name.value = newName
    }

    fun onChangeLastName(newLastName: String) {
        _lastName.value = newLastName
    }

    fun onChangeUser(newUser: String) {
        _user.value = newUser
    }

    fun onChangeEmail(newEmail: String) {
        val regex = ".+@".toRegex()
//        if (!regex.containsMatchIn(_email.value)) {
        _email.value = newEmail + _domain.value
//        }
    }

    fun onChangeIdCT(newIdCT: String) {
        _idCT.value = newIdCT
    }

    fun onChangeIdAirbus(newIdAirbus: String) {
        _idAirbus.value = newIdAirbus
    }

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
                    // Manejo de error si el formato no es válido
                    _exEmployees.value.add(employee)
                }
            }
        }
    }

    fun addEmployee(newEmployee: Employee) {
        viewModelScope.launch {
            Database.addEmployee(
                newEmployee.toInsertDTO()
            )
            Database.register(newEmployee.email, "ct1234")
            DataViewModel.employees.value.add(newEmployee)
            orderEmployees()
        }
    }

    fun removeEmployee(removeEmployee: EmployeeUpdateDTO) {
        viewModelScope.launch {
            FullScreenLoadingManager.showLoader()
            Database.updateEmployee(
                removeEmployee
            )

            val updatedEmployee = removeEmployee.toEntity()

            val employeeToRemove =
                DataViewModel.employees.value.find { it.idEmployee == updatedEmployee.idEmployee }
            DataViewModel.employees.value.remove(employeeToRemove)
            DataViewModel.employees.value.add(removeEmployee.toEntity())

            orderEmployees()

            FullScreenLoadingManager.hideLoader()
        }

    }

    fun changeFilter(value: String) {
        _filter.value = value
    }
}