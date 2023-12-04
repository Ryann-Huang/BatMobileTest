package com.example.batmobiletest

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.batmobiletest.databinding.ActivityMainBinding
import com.example.batmobiletest.databinding.FragmentOptionsBinding
import com.example.batmobiletest.ui.home.HomeFragment
import com.example.batmobiletest.ui.home.HomeFragmentDirections
import com.example.batmobiletest.ui.home.HomeViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val titleText = findViewById<TextView>(R.id.tvStopInfo)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_menu) {
            val navController = findNavController(R.id.nav_host_fragment_content_main)

            if (navController.graph.startDestinationId == navController.currentDestination?.id) {
                item.setIcon(R.drawable.baseline_close_black_18)
                val action = HomeFragmentDirections.actionNavHomeToOptionsFragment()
                navController.navigate(action)
            } else {
                item.setIcon(R.drawable.baseline_menu_black_18)
                navController.navigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}