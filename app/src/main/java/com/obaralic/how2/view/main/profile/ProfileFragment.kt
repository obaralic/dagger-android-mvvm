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

package com.obaralic.how2.view.main.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseUser
import com.obaralic.how2.R
import com.obaralic.how2.databinding.ProfileFragmentBinding
import com.obaralic.how2.model.User
import com.obaralic.how2.base.BaseFragment
import com.obaralic.how2.view.auth.AuthResource
import javax.inject.Inject

class ProfileFragment : BaseFragment() {

    @Inject
    lateinit var app: Context

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var dataBinding: ProfileFragmentBinding

    lateinit var viewModel: ProfileViewModel

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false)
        return dataBinding.root
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
    }

    override fun initLayout() {
    }

    override fun initRx() {
        subscribeObservers()
    }

    private fun subscribeObservers() {
        // Fragment can be recreated independent from activity,
        // so we need to make sure that there are no loitering observers.
        viewModel.getFireAuth().removeObservers(viewLifecycleOwner)

        viewModel.getFireAuth().observe(this, Observer<AuthResource<FirebaseUser?>> {
            when (it.status) {
                AuthResource.AuthStatus.AUTHENTICATED -> {
                    setUserDetails(it.data!!)
                }
                AuthResource.AuthStatus.ERROR -> {
                    setErrorDetails(it.message!!)
                }
                else -> {
                }
            }

        })
    }

    private fun setErrorDetails(message: String) {
        dataBinding.user = User(email = message, username = "Error", website = "Error")
    }

    private fun setUserDetails(data: FirebaseUser) {
        dataBinding.fireUser = data
    }

}