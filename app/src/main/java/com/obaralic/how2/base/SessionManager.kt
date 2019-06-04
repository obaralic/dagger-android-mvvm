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
import com.obaralic.how2.model.User
import com.obaralic.how2.view.auth.AuthResource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionManager @Inject constructor() {

    private val cachedUser: MediatorLiveData<AuthResource<out User>>
            by lazy { MediatorLiveData<AuthResource<out User>>() }

    fun getAuthUser(): LiveData<AuthResource<out User>> = cachedUser

    fun authenticate(source: LiveData<AuthResource<out User>>) {
        if(cachedUser != null){
            cachedUser.value = AuthResource.loading(null)
            cachedUser.addSource(source) {
                cachedUser.value = it
                cachedUser.removeSource(source)
            }
        } else {
            Timber.d("Previous session detected. Returning cached user.")
        }
    }

    fun logout() {
        Timber.d("Logging out...")
        cachedUser.value = AuthResource.logout()
    }
}