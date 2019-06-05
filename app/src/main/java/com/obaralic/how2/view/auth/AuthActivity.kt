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

package com.obaralic.how2.view.auth

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.obaralic.how2.R
import com.obaralic.how2.databinding.AuthActivityBinding
import com.obaralic.how2.model.User
import com.obaralic.how2.base.BaseActivity
import com.obaralic.how2.view.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.auth_activity.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named


internal class AuthActivity : BaseActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var glide: RequestManager

    @field:[Inject Named("drawable.auth")]
    lateinit var logoDrawable: Drawable

    private lateinit var dataBinding: AuthActivityBinding

    private lateinit var viewModel: AuthViewModel

    override fun initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.auth_activity)
        dataBinding.viewModel = viewModel
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
    }

    override fun initLayout() {
        glide.load(logoDrawable).into(login_logo)
    }

    override fun initRx() {
        subscribeObservers()
        addDisposable(RxView
            .clicks(login_button)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { Timber.d("Login...").apply { attemptLogin() } },
                onError = { Timber.e(it) }
            )
        )
    }

    private fun attemptLogin() {
        if (user_id_input.text!!.isNotEmpty()) {
            viewModel.authenticate(Integer.parseInt(user_id_input.text.toString()))
        }
    }

    private fun onLoginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun subscribeObservers() {
        viewModel.observeAuthState().observe(this, Observer<AuthResource<out User>> { authResource ->
            authResource?.apply {
                when (authResource.status) {
                    AuthResource.AuthStatus.LOADING -> {
                        showProgressBar(true)
                    }

                    AuthResource.AuthStatus.AUTHENTICATED -> {
                        showProgressBar(false)
                        Timber.d("LOGIN SUCCESS: ${authResource.data}")
                        onLoginSuccess()
                    }

                    AuthResource.AuthStatus.ERROR -> {
                        showProgressBar(false)
                        Timber.e("LOGIN ERROR: ${authResource.message}")
                    }

                    AuthResource.AuthStatus.NOT_AUTHENTICATED -> {
                        showProgressBar(false)
                    }
                }
            }
        })
    }

    private fun showProgressBar(visible: Boolean) {
        if (visible) progress_bar.visibility = View.VISIBLE
        else progress_bar.visibility = View.GONE
    }

}