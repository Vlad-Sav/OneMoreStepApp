package kg.android.onemorestepapp.ui.rewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentRewardsBinding
import kg.android.onemorestepapp.databinding.FragmentRouteSaveBinding
import kg.android.onemorestepapp.ui.routes.RoutesRecyclerViewAdapter
import kg.android.onemorestepapp.viewmodels.RewardsViewModel
import kg.android.onemorestepapp.viewmodels.RouteRecordViewModel

@AndroidEntryPoint
class RewardsFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: RewardsViewModel

    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RewardsViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.stickersNumber.observe(viewLifecycleOwner){
            number -> viewModel.onEvent(RewardsUiEvent.GetStickers)
        }
        viewModel.stickersLiveData.observe(viewLifecycleOwner) { stickers ->
            if(stickers != null && stickers.isNotEmpty()){
                binding.rewardsRecyclerView.apply {
                    layoutManager = GridLayoutManager(activity, 3)
                    adapter = StickersRecyclerViewAdapter(stickers)
                }
            }
        }
    }
}