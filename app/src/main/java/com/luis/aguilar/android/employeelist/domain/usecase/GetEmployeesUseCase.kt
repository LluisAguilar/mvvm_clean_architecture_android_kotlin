package com.luis.aguilar.android.employeelist.domain.usecase

import com.luis.aguilar.android.employeelist.domain.Response
import com.luis.aguilar.android.employeelist.domain.model.Employees
import com.luis.aguilar.android.employeelist.data.repository.EmployeesRepositoryImp
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(private val employeesRepositoryImp: EmployeesRepositoryImp) {

    operator fun invoke(): Flow<Response<Employees>> =
        employeesRepositoryImp.getEmployees()


}