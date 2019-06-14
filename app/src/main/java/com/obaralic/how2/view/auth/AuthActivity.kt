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
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.RequestManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.jakewharton.rxbinding2.view.RxView
import com.obaralic.how2.R
import com.obaralic.how2.base.BaseActivity
import com.obaralic.how2.databinding.AuthActivityBinding
import com.obaralic.how2.util.afterTextChanged
import com.obaralic.how2.util.snack
import com.obaralic.how2.view.auth.firebase.FirebaseAuthViewModel
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
    lateinit var googleSignInIntent: Intent

    @Inject
    lateinit var glide: RequestManager

    @field:[Inject Named("drawable.auth")]
    lateinit var logoDrawable: Drawable

    private lateinit var dataBinding: AuthActivityBinding

    private lateinit var fireAuthViewModel: FirebaseAuthViewModel

    override fun initViewModel() {
        fireAuthViewModel = ViewModelProviders.of(this, factory).get(FirebaseAuthViewModel::class.java)
    }

    override fun initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.auth_activity)
        dataBinding.fireAuthViewModel = fireAuthViewModel
    }

    override fun initLayout() {
        glide.load(logoDrawable).into(login_logo)

        user_email_input.afterTextChanged {
            fireAuthViewModel.formDataChanged(getFormInput())
        }

        user_password_input.apply {
            afterTextChanged { fireAuthViewModel.formDataChanged(getFormInput()) }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> attemptLogin()
                }
                false
            }
        }
    }

    override fun initRx() {
        subscribeObservers()

        addDisposable(RxView
            .clicks(login_button)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { attemptLogin() },
                onError = { Timber.e(it) }
            )
        )

        addDisposable(RxView
            .clicks(signup_button)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { attemptSignUp() },
                onError = { Timber.e(it) }
            )
        )

        addDisposable(RxView
            .clicks(google_button)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { attemptGoogleLogin() },
                onError = { Timber.e(it) }
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                fireAuthViewModel.authenticate(account!!)
            } catch (e: ApiException) {
                Timber.e(e, "Google sign in failed")
            }
        }
    }

    private fun subscribeObservers() {
        fireAuthViewModel.observeFormState()
            .observe(this, Observer {
                var enable = false

                when (it.status) {
                    FormResource.FormStatus.INVALID_EMAIL -> user_email_input.error = it.emailFormError
                    FormResource.FormStatus.INVALID_PASSWORD -> user_password_input.error = it.passFormError
                    FormResource.FormStatus.LOADING -> enable = false
                    FormResource.FormStatus.VALID -> enable = true
                }

                enableFormButtons(enable)
            })

        fireAuthViewModel.observeAuthState()
            .observe(this, Observer { authState ->
                Timber.d("Auth State: ${authState.status}")
                when (authState.status) {
                    AuthResource.AuthStatus.LOADING -> {
                        showProgressBar(true)
                    }

                    AuthResource.AuthStatus.AUTHENTICATED -> {
                        Timber.d("Auth Success: ${authState.data!!.email}")
                        showProgressBar(false)
                        onLoginSuccess()
                    }

                    AuthResource.AuthStatus.ERROR -> {
                        Timber.e("Auth Error: ${authState.message}")
                        snack(constraint, authState.message!!)
                        showProgressBar(false)
                        enableFormButtons(true)
                    }

                    AuthResource.AuthStatus.NOT_AUTHENTICATED -> {
                        showProgressBar(false)
                    }
                }
            })
    }

    private fun attemptGoogleLogin() {
        val signInIntent = googleSignInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun attemptLogin() {
        fireAuthViewModel.authenticate(getFormInput())
    }

    private fun attemptSignUp() {
        fireAuthViewModel.create(getFormInput())
    }

    private fun onLoginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun enableFormButtons(enable: Boolean) {
        login_button.isEnabled = enable
        signup_button.isEnabled = enable
    }

    private fun showProgressBar(visible: Boolean) =
        if (visible) progress_bar.visibility = View.VISIBLE
        else progress_bar.visibility = View.GONE


    private fun getFormInput() =
        Pair(user_email_input.text.toString(), user_password_input.text.toString())

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}