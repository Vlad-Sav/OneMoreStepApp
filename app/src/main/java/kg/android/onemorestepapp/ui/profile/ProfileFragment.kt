package kg.android.onemorestepapp.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentProfileBinding
import kg.android.onemorestepapp.databinding.FragmentStatisticsBinding
import kg.android.onemorestepapp.service.foregroundservice.StepCounterService
import kg.android.onemorestepapp.viewmodels.ProfileViewModel
import kg.android.onemorestepapp.viewmodels.StepCounterViewModel
import java.lang.Byte.decode
import java.util.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import kg.android.onemorestepapp.ui.auth.LoginFragmentDirections
import okhttp3.internal.wait
import java.io.ByteArrayInputStream

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.addRouteBtn.setOnClickListener{
            val directions =
                ProfileFragmentDirections.actionProfileFragmentToRouteRecordFragment()
            findNavController().navigate(directions)
        }
        binding.logoutBtn.setOnClickListener{
            viewModel.logout()
            val directions =
                ProfileFragmentDirections.actionProfileFragmentToRegisterFragment()
            findNavController().navigate(directions)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            binding.userFirstName.text = userProfile?.username
            binding.userName.text = userProfile?.username
        }
        viewModel.usersPinnedSticker.observe(viewLifecycleOwner){ usersPinnedSticker ->
            if(usersPinnedSticker != null){
                val imageAsBytes: ByteArray = Base64.decode(usersPinnedSticker?.sticker, Base64.DEFAULT)
                binding.profileStickerImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}