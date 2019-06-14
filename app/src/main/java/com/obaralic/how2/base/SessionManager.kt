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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.obaralic.how2.model.User
import com.obaralic.how2.util.livedata.FirebaseAuthLiveData
import com.obaralic.how2.view.auth.AuthResource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionManager @Inject constructor(private var auth: FirebaseAuth) {

    private val user: FirebaseAuthLiveData by lazy { FirebaseAuthLiveData(auth) }

    private val onFailure: (Exception) -> Unit = {
        user.value = AuthResource.error(null, it.message)
    }

    fun getUser(): LiveData<AuthResource<FirebaseUser?>> = user

    fun authenticate(email: String, pass: String) {
        user.value = AuthResource.loading(null)
        auth.signInWithEmailAndPassword(email, pass)
            .addOnFailureListener { onFailure(it) }
    }

    fun create(email: String, pass: String) {
        user.value = AuthResource.loading(null)
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnFailureListener { onFailure(it) }
    }

    fun authenticate(credential: AuthCredential) {
        user.value = AuthResource.loading(null)
        auth.signInWithCredential(credential)
            .addOnFailureListener { onFailure(it) }
    }

    fun signOut() = auth.signOut()

    @Deprecated("JsonPlaceholder")
    private val cachedRetroUser: MediatorLiveData<AuthResource<out User>>
            by lazy { MediatorLiveData<AuthResource<out User>>() }

    @Deprecated("JsonPlaceholder")
    fun getAuthUser(): LiveData<AuthResource<out User>> = cachedRetroUser

    @Deprecated("JsonPlaceholder")
    fun authenticate(source: LiveData<AuthResource<out User>>) {
        cachedRetroUser.value = AuthResource.loading(null)
        cachedRetroUser.addSource(source) {
            cachedRetroUser.value = it
            cachedRetroUser.removeSource(source)
        }
    }
}