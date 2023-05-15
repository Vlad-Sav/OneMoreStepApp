package kg.android.onemorestepapp.ui.permission

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentPermissionBinding
import kg.android.onemorestepapp.utils.Permissions.hasLocationPermission
import kg.android.onemorestepapp.utils.Permissions.requestLocationPermission


class PermissionFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
        binding.continueButton.setOnClickListener {
            if(hasLocationPermission(requireContext())){
                findNavController().navigate(R.id.action_permissionFragment_to_registerFragment)
            }else{
                requestLocationPermission(this)
            }
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionDenied(this, perms[0])){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        findNavController().navigate(R.id.action_permissionFragment_to_registerFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
}