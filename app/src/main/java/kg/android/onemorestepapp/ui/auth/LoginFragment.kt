package kg.android.onemorestepapp.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentLoginBinding
import kg.android.onemorestepapp.models.AuthResult
import kg.android.onemorestepapp.viewmodels.RegisterViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.btnLogin.setOnClickListener { loginButtonOnClick() }
        initializeEditText()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory).get(RegisterViewModel::class.java)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.authResults.collect { result ->
                when (result) {
                    is AuthResult.Authorized -> {
                        val directions =
                            LoginFragmentDirections.actionLoginFragmentToProfileFragment()
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

    }

    private fun loginButtonOnClick(){
        viewModel.onEvent(AuthUiEvent.SignIn)
    }

    private fun initializeEditText(){
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUiEvent.SignInEmailChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(s.toString()))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

}