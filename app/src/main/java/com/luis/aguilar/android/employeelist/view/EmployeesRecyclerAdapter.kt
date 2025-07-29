import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luis.aguilar.android.employeelist.databinding.ItemEmployeeBinding
import com.luis.aguilar.android.employeelist.databinding.ItemProgressBinding
import com.luis.aguilar.android.employeelist.domain.model.Employee
import com.squareup.picasso.Picasso

class EmployeesRecyclerAdapter(
    var employeesList: MutableList<Employee>,
    val onItemClicked: (Int) -> Unit,
    val onItemDeleted: (Int) -> Unit,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var isLoading = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            ITEM -> {
                val binding = ItemEmployeeBinding.inflate(layoutInflater, parent, false)
                EmployeesViewHolder(binding)
            }
            LOADING -> {
                val binding = ItemProgressBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
            else -> {
                val binding = ItemEmployeeBinding.inflate(layoutInflater, parent, false)
                EmployeesViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(getItemViewType(position)) {
            ITEM -> {
                val vh = holder as EmployeesViewHolder
                vh.bind(employeesList[position])
            }
            LOADING -> {
                val vh = holder as LoadingViewHolder
                vh.bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return employeesList.size
    }

    fun updateEmployees(employees: MutableList<Employee>) {
        this.employeesList = employees
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == employeesList.size -1 && isLoading)
            LOADING
        else
            ITEM
    }

    fun updateLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        if (isLoading) {
            this.employeesList.add(Employee())
            notifyItemInserted(this.employeesList.size - 1)
        } else {
            val position = this.employeesList.size -1
            val result = this.employeesList[position]

            result.let{
                this.employeesList.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    inner class EmployeesViewHolder(val binding: ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root) {
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

    inner class LoadingViewHolder(val binding: ItemProgressBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.loadMoreProgress.visibility = View.VISIBLE
        }
    }

    companion object {
        const val ITEM = 0
        const val LOADING = 1
    }
}