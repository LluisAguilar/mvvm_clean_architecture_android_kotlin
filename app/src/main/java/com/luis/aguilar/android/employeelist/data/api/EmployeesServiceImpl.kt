package com.android.code.challenge.justo.data.api

import com.android.code.challenge.justo.data.retrofit.EmployeesService
import com.luis.aguilar.android.employeelist.data.model.EmployeesResult
import javax.inject.Inject

class EmployeesServiceImpl @Inject constructor(
    private val employeesService: EmployeesService
): EmployeesServiceHelper {
    override suspend fun getEmployees(): EmployeesResult = employeesService.getEmployees()

}