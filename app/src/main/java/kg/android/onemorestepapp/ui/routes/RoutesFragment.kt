package kg.android.onemorestepapp.ui.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.databinding.FragmentRoutesBinding
import kg.android.onemorestepapp.ui.levels.LevelsRecyclerViewAdapter
import kg.android.onemorestepapp.viewmodels.RoutesViewModel

@AndroidEntryPoint
class RoutesFragment : Fragment() {
    private var _binding: FragmentRoutesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RoutesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoutesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RoutesViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.routes.observe(viewLifecycleOwner) { routes ->
            if(routes != null && routes.isNotEmpty()){
                binding.routesRecyclerView.apply {
                    layoutManager = GridLayoutManager(activity, 2)
                    adapter = RoutesRecyclerViewAdapter(routes)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}