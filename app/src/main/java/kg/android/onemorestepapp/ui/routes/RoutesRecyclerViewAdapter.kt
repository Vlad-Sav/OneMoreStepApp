package kg.android.onemorestepapp.ui.routes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.models.responses.RoutesResponse
import kg.android.onemorestepapp.ui.levels.LevelsRecyclerViewAdapter

class RoutesRecyclerViewAdapter(private val routes: List<RoutesResponse>): RecyclerView.Adapter<RoutesRecyclerViewAdapter.RouteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.route_recycler_view_item, parent, false)
        return RouteViewHolder(v)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(routes[position].title)
        holder.itemView.setOnClickListener{ view ->
            val bundle = bundleOf("routeData" to routes[position])

            view.findNavController().navigate(R.id.action_routesFragment_to_routeDetailsFragment, bundle)
        }

    }

    override fun getItemCount(): Int {
        return routes.count()
    }

    class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTv: TextView = itemView.findViewById(R.id.title_tv)
        fun bind(title: String) {
            titleTv.text = title
        }
    }
}