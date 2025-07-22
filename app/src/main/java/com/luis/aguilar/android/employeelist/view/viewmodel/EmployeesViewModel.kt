package com.luis.aguilar.android.employeelist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.aguilar.android.employeelist.domain.Response
import com.luis.aguilar.android.employeelist.domain.model.Employees
import com.luis.aguilar.android.employeelist.domain.usecase.GetEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(private val getEmployeesUseCase: GetEmployeesUseCase) :
    ViewModel() {

    private val _uiState = MutableLiveData<Response<Employees>>(Response.Loading)
    val uiState: LiveData<Response<Employees>> = _uiState

    init {
        getEmployees()
    }

    private fun getEmployees() {
        viewModelScope.launch {
            getEmployeesUseCase()
                .collect { response ->
                    when(response){

                        is Response.Loading -> {
                            _uiState.value = Response.Loading
                        }

                        is Response.Success -> {
                            _uiState.value = Response.Success(response.data)
                        }

                        is Response.Error -> {
                            _uiState.value = Response.Error(response.exception)
                        }

                        is Response.NotInitialized -> {
                            _uiState.value = Response.NotInitialized
                        }
                    }

                }
        }
    }

}