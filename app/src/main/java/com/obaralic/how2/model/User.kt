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

package com.obaralic.how2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class User constructor(
    @Expose @SerializedName("id") var id: Int = 0,
    @Expose @SerializedName("username") var username: String? = null,
    @Expose @SerializedName("email") var email: String? = null,
    @Expose @SerializedName("website") var website: String? = null
)

