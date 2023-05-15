package kg.android.onemorestepapp.ui.levels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentLevelsBinding
import kg.android.onemorestepapp.databinding.FragmentLoginBinding
import kg.android.onemorestepapp.ui.routedetails.RouteDetailsFragmentArgs


class LevelsFragment : Fragment() {
    private var _binding: FragmentLevelsBinding? = null
    private val binding get() = _binding!!
    private var level: Int = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLevelsBinding.inflate(inflater, container, false)
        val bundle = arguments ?: return binding.root
        val args = LevelsFragmentArgs.fromBundle(bundle)
        level = args.level
        binding.levelsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = LevelsRecyclerViewAdapter(level)
        }
        return binding.root
    }
}