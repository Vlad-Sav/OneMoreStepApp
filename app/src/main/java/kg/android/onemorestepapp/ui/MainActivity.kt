package kg.android.onemorestepapp.ui

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.utils.ExtensionFunctions.hide
import kg.android.onemorestepapp.utils.ExtensionFunctions.show
import kg.android.onemorestepapp.utils.Permissions

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.blue_mid)))

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.permissionFragment -> bottomNavigationView.hide()
                R.id.routeRecordFragment -> bottomNavigationView.hide()
                R.id.routeSaveFragment -> bottomNavigationView.hide()
                R.id.loginFragment -> bottomNavigationView.hide()
                R.id.registerFragment -> bottomNavigationView.hide()
                R.id.profileFragment -> bottomNavigationView.show()
                R.id.statisticsFragment -> bottomNavigationView.show()
                R.id.chestOpeningFragment -> bottomNavigationView.hide()
            }
        }
        bottomNavigationView.setupWithNavController(navController)
        if(!Permissions.hasLocationPermission(this)){
            navController.navigate(R.id.action_registerFragment_to_permissionFragment)
        }
    }
}