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
package com.obaralic.how2dagger.di.module

import android.content.Context
import androidx.room.Room
import com.obaralic.how2dagger.App
import com.obaralic.how2dagger.model.database.AppDatabase
import com.obaralic.how2dagger.model.database.UserDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule.BindsModule::class])
class AppModule {

    @Module
    interface BindsModule {

        @Binds
        @Singleton
        fun bindContext(app: App): Context

    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(app: App): AppDatabase {
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
