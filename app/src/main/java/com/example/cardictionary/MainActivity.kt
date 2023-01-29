package com.example.cardictionary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cardictionary.databinding.ActivityMainBinding
import com.example.cardictionary.tool.BaseUiListener
import com.example.cardictionary.tool.loadThemeId
import com.example.cardictionary.tool.loadUserFromLocal
import com.example.skin.SkinManager
import com.google.android.material.navigation.NavigationView
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import kotlinx.coroutines.runBlocking


open class MainActivity : AppCompatActivity() {
    private val mainViewModel = MainViewModel.getInstance()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val skinPackageName = "/skinpkg-debug.apk"

    override fun onCreate(savedInstanceState: Bundle?) {
        runBlocking {
            val id = loadThemeId(this@MainActivity)
            if (id == 1) {
                setTheme(R.style.Theme_CarDictionary)
                mainViewModel.setThemeId(1)
            } else if(id == 2) {
                mainViewModel.setThemeId(2)
                setTheme(R.style.Theme_CarDictionary2)
            }
        }

        if (mainViewModel.getThemeId() == 2) {
            supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifeCycleCallback(), true)
        }



        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarMain.toolbar)




        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerLayout = binding.navView.inflateHeaderView(R.layout.nav_header_main)
        val avatar = headerLayout.findViewById<ImageView>(R.id.mainAvatar)
        avatar.setOnClickListener {
            if (mainViewModel.getCurrentUser() == null) {
                drawerLayout.closeDrawer(Gravity.LEFT)
                navController.navigate(R.id.loginFragment)
            } else {
                drawerLayout.closeDrawer(Gravity.LEFT)
                navController.navigate(R.id.nav_slideshow)
            }

        }
        mainViewModel.isLogin.observe(this) {

            mainViewModel.bindUserInfo(this, headerLayout)
        }


        loadUserFromLocal(this) {
            mainViewModel.setCurrentUser(it)
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(applicationContext, "驾考通已切换到后台运行！", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.mTencent?.logout(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Log.d("chy", "onActivityResult: ")
            Tencent.onActivityResultData(requestCode, resultCode, data, BaseUiListener());
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    inner class FragmentLifeCycleCallback: FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            Log.d("MainActivity", "111111111111")
            val skinPath = f.requireActivity().filesDir.path.toString() + skinPackageName
            SkinManager.getInstance().loadSkin(skinPath)
        }

    }


}
