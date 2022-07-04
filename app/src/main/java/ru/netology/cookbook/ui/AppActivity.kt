package ru.netology.cookbook.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.netology.cookbook.R
import ru.netology.cookbook.databinding.AppActivityBinding

class AppActivity : AppCompatActivity() {
    private lateinit var binding: AppActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AppActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    fun showBottomNav(visible: Boolean) {
        if (visible) {
            binding.bottomNavigation.visibility = View.VISIBLE
        } else {
            binding.bottomNavigation.visibility = View.GONE
        }
    }

}