/**
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.obaralic.how2dagger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.obaralic.how2dagger.model.Repository
import com.obaralic.how2dagger.model.User
import com.obaralic.how2dagger.util.ioThread
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repository: Repository

    private val loadUser: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    val loadLiveUser: LiveData<User> by lazy { loadUser }

    fun load() {
        ioThread {
            loadUser.postValue(repository.load())
        }
    }
}
