package com.luis.aguilar.android.employeelist.data.repository

import com.android.code.challenge.justo.data.api.EmployeesServiceHelper
import com.luis.aguilar.android.employeelist.domain.Response
import com.luis.aguilar.android.employeelist.domain.model.Employees
import com.luis.aguilar.android.employeelist.data.helpers.EmployeesResultToEmployees
import com.luis.aguilar.android.employeelist.data.repository.EmployeesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EmployeesRepositoryImp @Inject constructor(
    private val employeesServiceHelper: EmployeesServiceHelper,
    private val dispatcher: CoroutineDispatcher
    ) : EmployeesRepository {

    private val employeesResultToEmployees = EmployeesResultToEmployees()

    override fun getEmployees(): Flow<Response<Employees>> = flow {
        emit(Response.Loading)

        emit(Response.Success(employeesResultToEmployees.map(employeesServiceHelper.getEmployees())))
    }.catch { exception ->
        emit(Response.Error(exception))
    }.flowOn(dispatcher)

}
