import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luis.aguilar.android.employeelist.databinding.ItemEmployeeBinding
import com.luis.aguilar.android.employeelist.domain.model.Employee
import com.squareup.picasso.Picasso

class EmployeesRecyclerAdapter(
    var employeesList: MutableList<Employee>,
    val onItemClicked: (Int) -> Unit,
    val onItemDeleted: (Int) -> Unit,
    ) :
    RecyclerView.Adapter<EmployeesRecyclerAdapter.EmployeesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeesViewHolder {
        val binding =
            ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: EmployeesViewHolder,
        position: Int
    ) {
        holder.bind(employeesList[position])
    }

    override fun getItemCount(): Int {
        return employeesList.size
    }

    fun updateEmployees(employees: MutableList<Employee>) {
        this.employeesList = employees
        notifyDataSetChanged()
    }

    inner class EmployeesViewHolder(val binding: ItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(employee: Employee) {
            binding.apply {
                employeeName.text = employee.full_name
                Picasso.get().load(employee.photo_url_small).into(employeeImage)
                itemEmployeeTeam.text = employee.team
                itemEmployeeBiography.text = employee.biography

                employeeImage.setOnClickListener {
                    onItemClicked(adapterPosition)
                }

                employeeDelete.setOnClickListener {
                    onItemDeleted(adapterPosition)
                }
            }
        }
    }
}