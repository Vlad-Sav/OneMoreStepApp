package kg.android.onemorestepapp.ui.routerecording

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentLoginBinding
import kg.android.onemorestepapp.databinding.FragmentRouteRecordBinding
import kg.android.onemorestepapp.databinding.FragmentRouteSaveBinding
import kg.android.onemorestepapp.ui.auth.AuthUiEvent
import kg.android.onemorestepapp.viewmodels.RouteRecordViewModel

@AndroidEntryPoint
class RouteSaveFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: RouteRecordViewModel

    private var _binding: FragmentRouteSaveBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRouteSaveBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(RouteRecordViewModel::class.java)
        binding.btnSaveRoute.setOnClickListener { saveButtonOnClick() }
        initializeEditText()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun saveButtonOnClick(){
        viewModel.onEvent(RouteSaveUiEvent.Save)
    }

    private fun initializeEditText(){
        binding.etRouteTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(RouteSaveUiEvent.RouteTitleChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.etRouteDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(RouteSaveUiEvent.RouteDescriptionChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}