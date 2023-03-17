package com.luis.aguilar.android.employeelist.data.repository

import com.luis.aguilar.android.employeelist.domain.Response
import com.luis.aguilar.android.employeelist.domain.model.Employees
import kotlinx.coroutines.flow.Flow

interface EmployeesRepository {

    fun getEmployees(): Flow<Response<Employees>>

}