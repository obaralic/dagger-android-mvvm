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

package com.obaralic.how2.view.auth

data class FormResource(
    val status: FormStatus,
    val emailFormError: String? = null,
    val passFormError: String? = null
) {

    enum class FormStatus {
        VALID,
        LOADING,
        INVALID_EMAIL,
        INVALID_PASSWORD
    }

    companion object {
        fun invalidEmail(message: String): FormResource =
            FormResource(FormStatus.INVALID_EMAIL, emailFormError = message)

        fun invalidPassword(message: String): FormResource =
            FormResource(FormStatus.INVALID_PASSWORD, passFormError = message)

        fun validForm(): FormResource =
            FormResource(status = FormStatus.VALID)

        fun freeze(): FormResource =
            FormResource(status = FormStatus.LOADING)
    }
}