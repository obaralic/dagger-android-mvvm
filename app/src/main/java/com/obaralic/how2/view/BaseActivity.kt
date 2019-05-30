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

package com.obaralic.how2.view

import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber


internal abstract class BaseActivity : DaggerAppCompatActivity() {

    private val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposables.isDisposed) disposables.dispose()
    }

    @Synchronized
    protected fun addDisposable(disposable: Disposable?) = disposable?.let { disposables.add(it) }

    protected fun init() {
        initViewModel().let { Timber.d("initViewModel") }
        initBinding().let { Timber.d("initBinding") }
        initLayout().let { Timber.d("initLayout") }
        initRx().let { Timber.d("initRx") }
    }

    protected abstract fun initViewModel()

    protected abstract fun initBinding()

    protected abstract fun initRx()

    protected abstract fun initLayout()
}
