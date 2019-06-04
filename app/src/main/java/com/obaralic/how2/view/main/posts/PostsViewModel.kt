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

package com.obaralic.how2.view.main.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.obaralic.how2.base.SessionManager
import com.obaralic.how2.model.Post
import com.obaralic.how2.network.main.MainApi
import com.obaralic.how2.view.main.Resource
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PostsViewModel @Inject constructor(
    private val session: SessionManager,
    private val mainApi: MainApi)
    : ViewModel() {

    private val posts: MediatorLiveData<Resource<out List<Post>>>
            by lazy { MediatorLiveData<Resource<out List<Post>>>() }

    init {
        Timber.d("Posts View Module is injected!")
    }

    fun observePosts(): LiveData<Resource<out List<Post>>> {
        posts.value = Resource.loading(null)

        val source = LiveDataReactiveStreams.fromPublisher(
            mainApi.getPosts(session.getAuthUser().value!!.data!!.id)
                .onErrorReturn {
                    val errorPost = Post()
                    errorPost.id = -1
                    arrayListOf(errorPost)
                }
                .map {posts ->
                    if (posts.isNotEmpty() && posts.first().id == -1) Resource.error("Something went wrong...", null)
                    else Resource.success(posts)
                }
                .subscribeOn(Schedulers.io())
        )

        posts.addSource(source) {
            posts.value = it
            posts.removeSource(source)
        }

        return posts
    }

}