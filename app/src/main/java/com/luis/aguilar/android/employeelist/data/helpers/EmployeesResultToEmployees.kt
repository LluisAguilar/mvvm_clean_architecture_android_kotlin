package com.luis.aguilar.android.employeelist.data.helpers

import com.luis.aguilar.android.employeelist.core.mapper.Mapper
import com.luis.aguilar.android.employeelist.data.model.EmployeesResult
import com.luis.aguilar.android.employeelist.domain.model.Employee
import com.luis.aguilar.android.employeelist.domain.model.Employees

class EmployeesResultToEmployees : Mapper<EmployeesResult, Employees>() {
    override fun map(value: EmployeesResult): Employees {
        val employeesList = mutableListOf<Employee>()
        value.employees.map {
            employeesList.add(Employee(
                it.biography,
                it.email_address,
                it.employee_type,
                it.full_name,
                it.phone_number,
                it.photo_url_large,
                it.photo_url_small,
                it.team,
                it.uuid
                ))
        }
        return Employees(employeesList)
    }

    override fun reverseMap(value: Employees): EmployeesResult {
        val employeesList = mutableListOf<com.luis.aguilar.android.employeelist.data.model.Employee>()
        value.employees.map {
            employeesList.add(com.luis.aguilar.android.employeelist.data.model.Employee(
                it.biography,
                it.email_address,
                it.employee_type,
                it.full_name,
                it.phone_number,
                it.photo_url_large,
                it.photo_url_small,
                it.team,
                it.uuid
            ))
        }
        return EmployeesResult(employeesList)
    }


}