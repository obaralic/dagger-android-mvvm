/**
 *  All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.obaralic.how2.view.main

import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.obaralic.how2.R
import com.obaralic.how2.base.SessionAwareActivity
import com.obaralic.how2.databinding.MainActivityBinding
import kotlinx.android.synthetic.main.main_activity.*

@Suppress("SameParameterValue")
internal class MainActivity : SessionAwareActivity() {

    private lateinit var dataBinding: MainActivityBinding

    private lateinit var navController: NavController

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.logout_item -> {
                session.logout()
                fireAuth.signOut()
                return true
            }
            android.R.id.home -> {
                return if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    true
                } else false

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViewModel() {
    }

    override fun initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
    }

    override fun initLayout() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        NavigationUI.setupWithNavController(nav_view, navController)
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_profile -> {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.main, true)
                        .build()
                    navController.navigate(R.id.profileScreen, null, navOptions)
                }
                R.id.nav_posts -> {
                    if (isValidDestination(R.id.postsScreen)) {
                        navController.navigate(R.id.postsScreen)
                    }
                }
                else -> {
                    return@setNavigationItemSelectedListener false
                }
            }
            it.isChecked = true
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun initRx() {
    }

    private fun isValidDestination(destination: Int) = destination != navController.currentDestination!!.id

    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(navController, drawer_layout)

}