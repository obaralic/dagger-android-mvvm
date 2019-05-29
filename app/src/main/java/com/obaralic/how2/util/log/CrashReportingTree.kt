package com.obaralic.how2.util.log

import android.util.Log
import timber.log.Timber

internal class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) return

//       Crashlytics.log(priority, tag, message)

        if (t != null) {
            if (priority == Log.ERROR) {
//                Crashlytics.logException(t)

            } else if (priority == Log.WARN) {
//                Crashlytics.logException(t)
            }
        }
    }

}
