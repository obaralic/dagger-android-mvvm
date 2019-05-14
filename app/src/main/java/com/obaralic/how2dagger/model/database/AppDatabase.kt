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
package com.obaralic.how2dagger.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.obaralic.how2dagger.util.ioThread

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    internal fun addCallback(callback: Callback) {
        if (mCallbacks == null) {
            mCallbacks = ArrayList()
        }
        mCallbacks!!.add(callback)
    }

    companion object {

        const val NAME = "how2dagger"

        internal fun seedDatabaseCallback(database: AppDatabase): Callback {
            return object : Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    ioThread {
                        val dao = database.userDao()
                        dao.insert(
                            UserEntity(
                                0,
                                "dagger@debug.com",
                                "Dagger Android",
                                "Debug"
                            )
                        )
                    }
                }
            }
        }
    }
}