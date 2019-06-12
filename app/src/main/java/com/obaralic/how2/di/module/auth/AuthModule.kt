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

package com.obaralic.how2.di.module.auth

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.obaralic.how2.R
import com.obaralic.how2.network.auth.AuthApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named


@Module
abstract class AuthModule {

    @Module
    companion object {

        @JvmStatic
        @AuthScope
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @JvmStatic
        @AuthScope
        @Provides
        fun provideSessionApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

        @JvmStatic
        @AuthScope
        @Provides
        @Named("drawable.auth")
        fun provideDrawable(context: Context): Drawable = ContextCompat
            .getDrawable(context, R.drawable.ic_shade)!!
    }
}

