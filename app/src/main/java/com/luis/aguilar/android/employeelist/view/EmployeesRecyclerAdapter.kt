package com.luis.aguilar.android.employeelist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luis.aguilar.android.employeelist.databinding.ItemEmployeeBinding
import com.luis.aguilar.android.employeelist.domain.model.Employee
import com.squareup.picasso.Picasso

class EmployeesRecyclerAdapter(private var employeeDataList: List<Employee>, listener: EmployeeClickListener):
    RecyclerView.Adapter<EmployeesRecyclerAdapter.EmployeesViewHolder>() {

    private var itemClickListener: EmployeeClickListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): EmployeesViewHolder {
        val itemBinding = ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(employeeViewHolder: EmployeesViewHolder, position: Int) {
        employeeViewHolder.bind(employeeDataList, itemClickListener)
    }

    override fun getItemCount(): Int {
        return employeeDataList.size
    }

    fun updateAdapter(employeeList:List<Employee>){
        this.employeeDataList = employeeList
        notifyDataSetChanged()
    }

    class EmployeesViewHolder(private val itemBinding: ItemEmployeeBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(employeeDataList: List<Employee>, listener: EmployeeClickListener) {
            with(itemBinding.itemEmployeeName) {
                text = employeeDataList[adapterPosition].full_name
                setOnClickListener {
                    listener.onEmployeeClick(employeeDataList[adapterPosition].full_name, adapterPosition)
                }
            }
            Picasso.get().load(employeeDataList[adapterPosition].photo_url_small).into(itemBinding.itemEmployeeImage)

            itemBinding.itemEmployeeTeam.text = employeeDataList[adapterPosition].team
            itemBinding.itemEmployeeBiography.text = employeeDataList[adapterPosition].biography
        }
    }

    interface EmployeeClickListener {
        fun onEmployeeClick(employeeId:String, position: Int)
    }
}