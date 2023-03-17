package com.android.code.challenge.justo.data.retrofit

import com.luis.aguilar.android.employeelist.data.model.EmployeesResult
import retrofit2.http.GET

interface EmployeesService {

    @GET("/sq-mobile-interview/employees.json")
    suspend fun getEmployees(): EmployeesResult
}