package kg.android.onemorestepapp.ui.levels

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import kg.android.onemorestepapp.R

class LevelsRecyclerViewAdapter(val currentLevel: Int): RecyclerView.Adapter<LevelsRecyclerViewAdapter.LevelViewHolder>() {
    private val numberOfLevels = 5
    private var startLevel: Int = currentLevel / 5 * 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.level_recycler_view_item, parent, false)
        return LevelViewHolder(v)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(startLevel + position + 1, currentLevel)
    }

    override fun getItemCount(): Int {
        return numberOfLevels
    }

    class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val levelNumber: TextView = itemView.findViewById(R.id.tv_level_number)
        private val verticalLine: View = itemView.findViewById(R.id.verticalLine)
        fun bind(number: Int, currentLevel: Int) {
            levelNumber.text = number.toString()
            if(number == currentLevel) levelNumber.setBackground(ContextCompat.getDrawable(itemView.context,R.drawable.circle_background_orange))
            verticalLine.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.blue_light))
            verticalLine.layoutParams.width = 10
        }
    }
}