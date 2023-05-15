package kg.android.onemorestepapp.ui.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.models.responses.TopUsersResponse

class TopUsersRecyclerView (private val topUsers: List<TopUsersResponse>): RecyclerView.Adapter<TopUsersRecyclerView.TopUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopUserViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_users_recycler_view_item, parent, false)
        return TopUserViewHolder(v)
    }

    override fun onBindViewHolder(holder: TopUserViewHolder, position: Int) {
        holder.bind(topUsers[position])
        /*holder.itemView.setOnClickListener{ view ->
            val bundle = bundleOf("routeData" to topUsers[position])

            view.findNavController().navigate(R.id.action_routesFragment_to_routeDetailsFragment, bundle)
        }
*/
    }

    override fun getItemCount(): Int {
        return topUsers.count()
    }

    class TopUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTv: TextView = itemView.findViewById(R.id.user_name_top_tv)
        private val stepCountTv: TextView = itemView.findViewById(R.id.user_step_count_tv)
        fun bind(user: TopUsersResponse) {
            userNameTv.text = user.userName
            stepCountTv.text = user.stepsCount.toString()
        }
    }
}