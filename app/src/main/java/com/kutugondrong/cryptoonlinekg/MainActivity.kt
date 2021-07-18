package com.kutugondrong.cryptoonlinekg

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kutugondrong.cryptoonlinekg.databinding.ActivityMainBinding
import com.kutugondrong.cryptoonlinekg.preference.LocalPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bindingActivity: ActivityMainBinding
    @Inject lateinit var localPreferences: LocalPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        bindingActivity = binding
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.streamFragment
        ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.apply {
            supportActionBar?.hide()
            supportActionBar?.setBackgroundDrawable(ColorDrawable(0xffffffff.toInt()));
            navBottom.setupWithNavController(navController)
        }
    }

    fun showToolbar(isShow: Boolean) {
        if (isShow) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    fun showNavBottom(visibility: Int) {
        bindingActivity?.navBottom.visibility = visibility
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                localPreferences.isLogin = false
                showToolbar(false)
                showNavBottom(View.GONE)
                navController.navigate(R.id.loginFragment, null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val currentDestId = navController.currentDestination?.id
        if (currentDestId == R.id.welcomeFragment || currentDestId == R.id.loginFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}