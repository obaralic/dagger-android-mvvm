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

import com.obaralic.how2.di.annotation.ActivityScope
import com.obaralic.how2.di.module.auth.AuthModule
import com.obaralic.how2.di.module.auth.AuthViewModelModule
import com.obaralic.how2.di.module.main.MainFragmentBuilderModule
import com.obaralic.how2.di.module.main.MainModule
import com.obaralic.how2.di.module.main.MainViewModelModule
import com.obaralic.how2.view.auth.AuthActivity
import com.obaralic.how2.view.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            AuthViewModelModule::class,
            AuthModule::class
        ]
    )
    internal abstract fun contributeAuthActivity(): AuthActivity

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentBuilderModule::class,
            MainViewModelModule::class,
            MainModule::class
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}
