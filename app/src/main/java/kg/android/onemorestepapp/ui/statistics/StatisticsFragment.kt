package kg.android.onemorestepapp.ui.statistics


import android.Manifest
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.android.onemorestepapp.databinding.FragmentStatisticsBinding
import kg.android.onemorestepapp.service.foregroundservice.StepCounterService

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.ui.profile.ProfileFragmentDirections
import kg.android.onemorestepapp.ui.routes.RoutesRecyclerViewAdapter
import kg.android.onemorestepapp.utils.ExtensionFunctions.disable
import kg.android.onemorestepapp.utils.ExtensionFunctions.enable
import kg.android.onemorestepapp.utils.apiwrappers.API26Wrapper
import kg.android.onemorestepapp.viewmodels.StepCounterViewModel
@AndroidEntryPoint
class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: StepCounterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 26) {
            API26Wrapper.startService(
                requireContext(),
                Intent(activity, StepCounterService::class.java)
            )
        } else {
            requireContext().startService(Intent(activity, StepCounterService::class.java))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        // Инициализируем ViewModel с помощью ViewModelProvider
        viewModel = ViewModelProvider(this).get(StepCounterViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1);
        }/*
        val intent = Intent(context, StepCounterService::class.java)
        context?.startService(intent)*/
        binding.levelBtn.disable()
        // Наблюдаем за LiveData количества шагов
        viewModel.stepCount.observe(viewLifecycleOwner) { stepCount ->
            binding.currentStepsCount.text = "$stepCount"
            var progress = ((stepCount.toFloat() / 10000f) * 100f).toInt()
            // Animate the progress indicator
            val animator = ObjectAnimator.ofInt(binding.circularProgress, "progress", 0, progress)
            animator.duration = (progress * 10).toLong()
            animator.interpolator = LinearInterpolator()
            animator.start()
        }

        viewModel.topUsersResponse.observe(viewLifecycleOwner){ topUsers ->
            if(topUsers != null && topUsers.isNotEmpty()){
                binding.usersTopRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = TopUsersRecyclerView(topUsers)
                }
            }
        }

        viewModel.level.observe(viewLifecycleOwner){ level ->
            if(level != null){
                binding.levelBtn.text = level.toString()
                binding.levelBtn.enable()
            }
        }

        binding.chestOpenBtn.setOnClickListener{
            onChestOpenBtnClicked()
        }

        binding.levelBtn.setOnClickListener{
            onLevelBtnClicked()
        }

        binding.rewardsBtn.setOnClickListener{
            onRewardsBtnClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onChestOpenBtnClicked(){
        val directions =
            StatisticsFragmentDirections.actionStatisticsFragmentToChestOpeningFragment()
        findNavController().navigate(directions)
    }

    private fun onLevelBtnClicked(){
        val directions =
            StatisticsFragmentDirections.actionStatisticsFragmentToLevelsFragment(viewModel.level.value ?: 1)
        findNavController().navigate(directions)
    }

    private fun onRewardsBtnClicked(){
        val directions =
            StatisticsFragmentDirections.actionStatisticsFragmentToRewardsFragment()
        findNavController().navigate(directions)
    }
}