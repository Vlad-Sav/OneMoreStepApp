package kg.android.onemorestepapp.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentRegisterBinding
import kg.android.onemorestepapp.models.AuthResult
import kg.android.onemorestepapp.viewmodels.RegisterViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.btnRegister.setOnClickListener { registerButtonOnClick() }
        binding.btnHaveAccount.setOnClickListener {
            val directions = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(directions)
        }
        initializeEditText()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory).get(RegisterViewModel::class.java)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.authResults.collect { result ->
                when (result) {
                    is AuthResult.Authorized -> {
                        val directions =
                            RegisterFragmentDirections.actionRegisterFragmentToProfileFragment()
                        findNavController().navigate(directions)
                    }
                    is AuthResult.Unauthorized -> {
                        Toast.makeText(
                            context,
                            "You're not authorized",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is AuthResult.UnknownError -> {
                        Toast.makeText(
                            context,
                            result.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
       /* viewModel.authResults.onEach { result ->
            when(result) {
                is AuthResult.Authorized -> {
                    val directions = RegisterFragmentDirections.actionRegisterFragmentToProfileFragment()
                    findNavController().navigate(directions)
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(
                        context,
                        "You're not authorized",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(
                        context,
                        "An unknown error occurred",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)*/

    }

    private fun registerButtonOnClick(){
       viewModel.onEvent(AuthUiEvent.SignUp)
    }

    private fun initializeEditText(){
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUiEvent.SignUpEmailChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUiEvent.SignUpUsernameChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUiEvent.SignUpConfirmPasswordChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}