package com.luis.aguilar.android.employeelist.view

import EmployeesRecyclerAdapter
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.luis.aguilar.android.employeelist.R
import com.luis.aguilar.android.employeelist.databinding.ActivityMainBinding
import com.luis.aguilar.android.employeelist.domain.Response
import com.luis.aguilar.android.employeelist.domain.model.Employee
import com.luis.aguilar.android.employeelist.view.EmployeeUtilStrings.Companion.SAVED_FILTER_STATE
import com.luis.aguilar.android.employeelist.view.viewmodel.EmployeesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FilterRecyclerAdapter.FilterClickListener {

    private lateinit var adapter : EmployeesRecyclerAdapter
    private lateinit var filterAdapter : FilterRecyclerAdapter
    private lateinit var binding: ActivityMainBinding

    private var employeesList = mutableListOf<Employee>()
    private var filters = mutableListOf<String>()

    private var savedFilter = "All"

    private lateinit var employeesViewModel : EmployeesViewModel

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addEmployee.setOnClickListener {
            this.employeesList.add(0,
                Employee(
                    biography = "Android developer Java|Kotlin",
                    email_address = "luis@gmail.com",
                    "Contractor",
                    "Luis Aguilar Garcia",
                    phone_number = "1234567890",
                    photo_url_large = "https://miro.medium.com/v2/resize:fit:1400/format:webp/1*aUT1pcBqSFsq-cNvKK3UHg.jpeg",
                    photo_url_small = "https://miro.medium.com/v2/resize:fit:1400/format:webp/1*aUT1pcBqSFsq-cNvKK3UHg.jpeg",
                    team = "Android",
                    uuid = "1234567"
                )
            )
            this.adapter.notifyItemInserted(0)
            binding.employeesRecycler.scrollToPosition(0)
        }

        adapter = EmployeesRecyclerAdapter(
            employeesList,
            { position -> onEmployeeClick(position) },
            { position -> onItemDeleted(position) }
        )

        filterAdapter = FilterRecyclerAdapter(filters, this@MainActivity)

        if (savedInstanceState != null) {
            savedFilter = savedInstanceState.getString(SAVED_FILTER_STATE,"All")
        }

        employeesViewModel = ViewModelProvider(this)[EmployeesViewModel::class.java]

        progressDialog = ProgressDialog(this)

        with(binding.employeesRecycler) {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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
                                applyFilter(savedFilter)
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

                        is Response.NotInitialized -> Unit
                    }
                }
            }
        }
    }

    private fun setFilterData(employees: List<Employee>) {
        filters.add("All")
        employees.forEach {
            if (!filters.contains(it.team)){
                filters.add(it.team)
            }
        }
        filterAdapter.notifyDataSetChanged()
    }

    fun onEmployeeClick(position: Int) {
        Toast.makeText(this, this.employeesList[position].full_name.toString(), Toast.LENGTH_SHORT).show()
    }

    fun onItemDeleted(position: Int) {
        this.employeesList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    override fun onFilterClick(filter: String) {
        savedFilter = filter
        applyFilter(filter)
    }

    private fun applyFilter(filter: String) {
        if (filter == "All"){
            adapter.updateEmployees(this.employeesList)
        } else {
            val filteredList = this.employeesList.filter { employee -> employee.team == filter }
            adapter.updateEmployees(filteredList.toMutableList())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_FILTER_STATE, savedFilter)
    }
}