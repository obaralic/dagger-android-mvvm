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
package com.obaralic.how2.view.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.obaralic.how2.model.User
import com.obaralic.how2.network.AuthApi
import com.obaralic.how2.view.auth.AuthResource
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class AuthViewModel @Inject constructor(private val authApi: AuthApi) : ViewModel() {

    private val authUser: MediatorLiveData<AuthResource<out User>>
            by lazy { MediatorLiveData<AuthResource<out User>>() }

    internal fun authenticate(userId: Int) {
        authUser.value = AuthResource.loading(null)

        val source = LiveDataReactiveStreams.fromPublisher(
            authApi.getUser(userId)
                .onErrorReturn {
                    val errorUser = User()
                    errorUser.id = -1
                    errorUser
                }
                .map { user ->
                    if (user.id == -1) AuthResource.error(null, "Could not authenticate")
                    else AuthResource.authenticated(user)
                }
                .subscribeOn(Schedulers.io())
        )

        authUser.addSource(source) {
            authUser.value = it
            authUser.removeSource(source)
        }
    }

    internal fun auth(userId: Int) =
        authApi.getUser(userId)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { Timber.d("Auth success: $it") },
                onError = { Timber.e(it) }
            )

    internal fun observeUser(): LiveData<AuthResource<out User>> = authUser

}

