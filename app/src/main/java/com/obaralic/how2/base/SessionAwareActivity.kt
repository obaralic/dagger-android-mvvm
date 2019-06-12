/*
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

package com.obaralic.how2.base



import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.obaralic.how2.model.User
import com.obaralic.how2.view.auth.AuthActivity
import com.obaralic.how2.view.auth.AuthResource
import timber.log.Timber
import javax.inject.Inject


internal abstract class SessionAwareActivity : BaseActivity() {

    @Inject
    protected lateinit var session: SessionManager

    @Inject
    protected lateinit var fireAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        session.getAuthUser().observe(this, Observer<AuthResource<out User>> { auth ->

            auth?.let {
                when (auth.status) {
                    AuthResource.AuthStatus.LOADING -> {
                        Timber.d("onChanged: LOADING...")
                    }

                    AuthResource.AuthStatus.AUTHENTICATED -> {
                        Timber.d("onChanged: AUTHENTICATED as: + ${auth.data!!.email}");
                    }

                    AuthResource.AuthStatus.NOT_AUTHENTICATED -> {
                        Timber.d("onChanged: NOT AUTHENTICATED. Navigating to Login screen.")
                        navigateToLoginScreen()
                    }

                    AuthResource.AuthStatus.ERROR -> {
                        Timber.d("onChanged: ERROR")
                    }
                }

            }
        })
    }

    private fun navigateToLoginScreen() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}