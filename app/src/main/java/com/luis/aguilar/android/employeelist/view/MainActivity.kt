package com.luis.aguilar.android.employeelist.view

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.luis.aguilar.android.employeelist.R
import com.luis.aguilar.android.employeelist.databinding.ActivityMainBinding
import com.luis.aguilar.android.employeelist.domain.Response
import com.luis.aguilar.android.employeelist.domain.model.Employee
import com.luis.aguilar.android.employeelist.view.EmployeeUtilStrings.Companion.SAVED_FILTER_STATE
import com.luis.aguilar.android.employeelist.view.viewmodel.EmployeesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EmployeesRecyclerAdapter.EmployeeClickListener, FilterRecyclerAdapter.FilterClickListener {

    private val adapter = EmployeesRecyclerAdapter(arrayListOf(),this)
    private val filterAdapter = FilterRecyclerAdapter(arrayListOf(), this)
    private lateinit var binding: ActivityMainBinding
    private var employeesList = mutableListOf<Employee>()
    private var savedFilter = "All"

    private lateinit var employeesViewModel : EmployeesViewModel

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            savedFilter = savedInstanceState.getString(SAVED_FILTER_STATE,"All")
        }

        employeesViewModel = ViewModelProvider(this)[EmployeesViewModel::class.java]

        progressDialog = ProgressDialog(this)

        with(binding.employeesRecycler) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        with(binding.filtersRecycler) {
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter = this@MainActivity.filterAdapter
        }

        attemptEmployeesRequest()

        binding.reloadSwipeToRefresh.setOnRefreshListener {
            attemptEmployeesRequest(true)
        }

    }

    private fun attemptEmployeesRequest(isRefresh: Boolean = false) {
        if (isRefresh){
            employeesList.clear()
            savedFilter = "All"
            adapter.notifyDataSetChanged()
            filterAdapter.notifyDataSetChanged()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                employeesViewModel.uiState.observe(this@MainActivity) {
                    when(it) {
                        is Response.Success -> {
                            binding.reloadSwipeToRefresh.isRefreshing = false
                            progressDialog.dismiss()
                            if (it.data.employees.isNotEmpty()){
                                it.data.employees.map { employee ->
                                    employeesList.add(employee)
                                }
                                setFilterData(employeesList)
                                adapter.updateAdapter(employeesList)
                                setFilter(savedFilter)
                                binding.reloadBtn.visibility = View.GONE
                            } else {
                                Toast.makeText(this@MainActivity, "No data found", Toast.LENGTH_SHORT).show()
                                binding.reloadBtn.visibility = View.VISIBLE
                                binding.reloadBtn.setOnClickListener {
                                    attemptEmployeesRequest(true)
                                }
                            }
                        }

                        is Response.Loading -> {
                            binding.reloadSwipeToRefresh.isRefreshing = false
                            progressDialog.setTitle(getString(R.string.loading))
                            progressDialog.show()
                        }

                        is Response.Error -> {
                            binding.reloadSwipeToRefresh.isRefreshing = false
                            progressDialog.dismiss()
                        }

                        is Response.NotInitialized -> {}
                    }
                }
            }
        }
    }

    private fun setFilterData(employees: List<Employee>) {
        val filters = mutableListOf<String>()

        filters.add("All")
        employees.map {
            if (!filters.contains(it.team)){
                filters.add(it.team)
            }
        }
        filterAdapter.updateAdapter(filters)
    }

    override fun onEmployeeClick(employeeId: String, position: Int) {}

    override fun onFilterClick(filter: String) {
        savedFilter = filter
        setFilter(filter)
    }

    private fun setFilter(filter: String) {
        if (filter == "All"){
            adapter.updateAdapter(employeesList)
        } else {
            adapter.updateAdapter(employeesList.filter { it.team == filter })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_FILTER_STATE, savedFilter)
    }
}