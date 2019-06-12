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
package com.obaralic.how2.base

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.obaralic.how2.BuildConfig
import com.obaralic.how2.R
import com.obaralic.how2.di.component.DaggerAppComponent
import com.obaralic.how2.util.Constants.CRASHLYTICS_ENABLED
import com.obaralic.how2.util.CrashReportingTree
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class BaseApp : DaggerApplication() {

    @Inject
    lateinit var config: FirebaseRemoteConfig

    @Inject
    lateinit var auth: FirebaseAuth

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        initFirebase()
        initTimber()
    }

    private fun initFirebase() {
        Crashlytics.setUserName("Obaralic Test")
        config.setDefaults(R.xml.config)
        config.fetchAndActivate().addOnCompleteListener {
            initTimber() }
    }

    private fun initTimber() {
        if (config.getBoolean(CRASHLYTICS_ENABLED)) Timber.plant(CrashReportingTree())
        else Timber.plant(Timber.DebugTree())
    }
}
