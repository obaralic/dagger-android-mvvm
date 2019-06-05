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

package com.obaralic.how2.di.module.main

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.obaralic.how2.base.BaseApp
import com.obaralic.how2.network.main.MainApi
import com.obaralic.how2.util.VerticalSpaceItemDecoration
import com.obaralic.how2.view.main.posts.PostsRecyclerAdapter
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named


@Module
abstract class MainModule {

    @Module
    companion object {

        @JvmStatic
        @MainScope
        @Provides
        fun provideMainApi(retrofit: Retrofit): MainApi = retrofit.create(MainApi::class.java)

        @JvmStatic
        @MainScope
        @Provides
        fun provideAdapter(): PostsRecyclerAdapter = PostsRecyclerAdapter()

        @JvmStatic
        @MainScope
        @Provides
        fun provideLinearLayoutManager(app: BaseApp): LinearLayoutManager = LinearLayoutManager(app.baseContext)

        @JvmStatic
        @MainScope
        @Provides
        fun provideItemDecoration(): VerticalSpaceItemDecoration = VerticalSpaceItemDecoration()

        @JvmStatic
        @MainScope
        @Provides
        @Named("drawable.main")
        fun provideDrawable(context: Context): Drawable = ContextCompat
            .getDrawable(context, android.R.drawable.ic_media_play)!!
    }
}
