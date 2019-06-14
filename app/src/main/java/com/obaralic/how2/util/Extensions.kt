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

package com.obaralic.how2.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG): Toast {
    val toast: Toast = Toast.makeText(this, message, duration)
    toast.show()
    return toast
}

fun Context.snack(view: View, message: String, duration: Int = Snackbar.LENGTH_LONG): Snackbar {
    val snack = Snackbar.make(view, message, duration)
    snack.show()
    return snack
}

fun String?.isEmailAddress(): Boolean {
    return if (this == null) {
        false
    } else if (this.contains('@')) {
        Patterns.EMAIL_ADDRESS.matcher(this).matches()
    } else {
        this.isNotBlank()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {

    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}