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
import androidx.databinding.DataBindingUtil
import com.obaralic.how2.R
import com.obaralic.how2.databinding.MainActivityBinding

internal class MainActivity : SessionAwareActivity() {

    private lateinit var dataBinding: MainActivityBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.logout_item -> {
                session.logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
    }

    override fun initViewModel() {
    }

    override fun initRx() {
    }

    override fun initLayout() {
    }


}