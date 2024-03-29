package org.wit.dogadoptioncentre.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import org.wit.dogadoptioncentre.R
import org.wit.dogadoptioncentre.databinding.HomePageBinding
import org.wit.dogadoptioncentre.ui.auth.LoggedInViewModel
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseUser
import org.wit.dogadoptioncentre.databinding.NavHeaderBinding
import org.wit.dogadoptioncentre.ui.auth.Login



class AdoptionHome : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomePageBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var loggedInViewModel : LoggedInViewModel


    /**
     * The function onCreate() is called when the activity is created
     *
     * @param savedInstanceState The saved state of the activity, if it is being re-created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
   /*     setContentView(R.layout.content_adoption_home)*/

        homeBinding = HomePageBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        val navHostFragment = supportFragmentManager.
        findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

//       val navController = findNavController(R.id.nav_host_fragment)






        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.adoptionHomeFragment, R.id.report_listFragment, R.id.aboutUsFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)


        val navView = homeBinding.navView
        navView.setupWithNavController(navController)


    }



    /**
     * The function is called when the activity is started. It creates a new instance of the
     * LoggedInViewModel class and observes the liveFirebaseUser variable. If the user is logged in,
     * the updateNavHeader function is called. If the user is logged out, the user is redirected to the
     * Login activity
     */
    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null) {
                //val currentUser = loggedInViewModel.liveFirebaseUser.value
                /*if (currentUser != null)*/ updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
            }
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

    }


    /**
     * It updates the navigation header with the current user's email.
     *
     * @param currentUser FirebaseUser - The current user that is logged in.
     */
    private fun updateNavHeader(currentUser: FirebaseUser) {
        var headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        navHeaderBinding.navHeaderEmail.text = currentUser.email
    }

    /**
     * It logs the user out and redirects them to the login page.
     *
     * @param item MenuItem - The menu item that was clicked.
     */
    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    /**
     * If the user presses the Up button, navigate up in the navigation hierarchy
     *
     * @return The return value is a boolean.
     */
    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return NavigationUI.navigateUp(navController, drawerLayout)
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }







}