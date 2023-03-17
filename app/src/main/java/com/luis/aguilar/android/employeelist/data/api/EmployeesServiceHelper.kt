package com.android.code.challenge.justo.data.api

import com.luis.aguilar.android.employeelist.data.model.EmployeesResult

interface EmployeesServiceHelper {

    suspend fun getEmployees(): EmployeesResult

}