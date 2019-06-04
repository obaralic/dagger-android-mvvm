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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.obaralic.how2.R
import com.obaralic.how2.base.BaseFragment
import com.obaralic.how2.databinding.PostsFragmentBinding
import com.obaralic.how2.model.Post
import com.obaralic.how2.util.VerticalSpaceItemDecoration
import com.obaralic.how2.view.main.Resource
import kotlinx.android.synthetic.main.posts_fragment.*
import timber.log.Timber
import javax.inject.Inject

class PostsFragment : BaseFragment() {

    @Inject
    lateinit var app: Context

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: PostsRecyclerAdapter

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var itemDecoration: VerticalSpaceItemDecoration

    lateinit var dataBinding: PostsFragmentBinding

    lateinit var viewModel: PostsViewModel

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.posts_fragment, container, false)
        return dataBinding.root
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(PostsViewModel::class.java)
    }

    override fun initLayout() {
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(itemDecoration)
        recycler_view.adapter = adapter
    }

    override fun initRx() {
        subscribeObservers()
    }

    private fun subscribeObservers() {
        // Fragment can be recreated independent from activity,
        // so we need to make sure that there are no loitering observers.
        viewModel.observePosts().removeObservers(viewLifecycleOwner)
        viewModel.observePosts().observe(this, Observer<Resource<out List<Post>>>{ listResource ->
            listResource?.let {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        Timber.d("Loading...")}
                    Resource.Status.SUCCESS -> {
                        Timber.d("Got posts...")
                            .also { adapter.setPosts(listResource.data!!) }
                    }
                    Resource.Status.ERROR -> {
                        Timber.e(Throwable(it.message))
                    }
                }
            }
        })


    }
}