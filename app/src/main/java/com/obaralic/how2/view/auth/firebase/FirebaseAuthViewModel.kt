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

package com.obaralic.how2.view.auth.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.obaralic.how2.base.SessionManager
import com.obaralic.how2.util.isEmailAddress
import com.obaralic.how2.view.auth.FormResource
import com.obaralic.how2.view.auth.AuthResource
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthViewModel @Inject constructor(private val session: SessionManager) : ViewModel() {

    private val authForm: MutableLiveData<FormResource> by lazy { MutableLiveData<FormResource>() }

    fun observeFormState(): LiveData<FormResource> = authForm

    fun observeAuthState(): LiveData<AuthResource<FirebaseUser?>> = session.getUser()

    fun authenticate(data: Pair<String, String>) {
        Timber.d("Authenticate user attempt")
        authForm.value = FormResource.freeze()
        session.authenticate(data.first, data.second)
    }

    fun create(data: Pair<String, String>) {
        Timber.d("Create user attempt")
        authForm.value = FormResource.freeze()
        session.create(data.first, data.second)
    }

    fun authenticate(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        session.authenticate(credential)
    }

    fun formDataChanged(data: Pair<String, String>) {
        if (!data.first.isEmailAddress()) authForm.value = FormResource.invalidEmail("Email field is not valid")
        else if (data.second.isEmpty()) authForm.value = FormResource.invalidPassword("Password field is not valid")
        else authForm.value = FormResource.validForm()
    }

}