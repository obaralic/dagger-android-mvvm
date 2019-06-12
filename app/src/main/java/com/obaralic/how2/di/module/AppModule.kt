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
package com.obaralic.how2.di.module

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.obaralic.how2.base.BaseApp
import com.obaralic.how2.R
import com.obaralic.how2.base.SessionManager
import com.obaralic.how2.model.database.AppDatabase
import com.obaralic.how2.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [AppModule.BindsModule::class])
class AppModule {

    @Module
    interface BindsModule {

        @Singleton
        @Binds
        fun bindContext(app: BaseApp): Context

    }

    @Singleton
    @Provides
    fun provideRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Singleton
    @Provides
    fun provideSessionManager(): SessionManager = SessionManager()

    @Singleton
    @Provides
    @Named("retrofit.base.url")
    fun provideRetrofitBaseUrl(): String = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(@Named("retrofit.base.url") base: String, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(base)
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions = RequestOptions
        .placeholderOf(android.R.drawable.picture_frame)
        .error(android.R.drawable.picture_frame)

    @Singleton
    @Provides
    fun provideHttpInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Singleton
    @Provides
    fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun provideGlide(app: BaseApp, options: RequestOptions): RequestManager = Glide
        .with(app)
        .setDefaultRequestOptions(options)

    @Singleton
    @Provides
    @Named("drawable.app")
    fun provideDrawable(context: Context): Drawable = ContextCompat
        .getDrawable(context, android.R.drawable.presence_online)!!

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideDatabase(app: BaseApp): AppDatabase {
        val database = Room
            .databaseBuilder(
                app.applicationContext,
                AppDatabase::class.java,
                AppDatabase.NAME
            )
            .build()
        database.addCallback(AppDatabase.seedDatabaseCallback(database))
        return database
    }
}
