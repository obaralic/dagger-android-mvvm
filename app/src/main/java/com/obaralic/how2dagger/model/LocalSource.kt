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
package com.obaralic.how2dagger.model

import com.obaralic.how2dagger.model.database.UserDao
import com.obaralic.how2dagger.model.database.UserEntity
import javax.inject.Inject

class LocalSource @Inject constructor(val dao: UserDao) {

    fun load(): User {
        val entity: UserEntity = dao.findById(1)
        return User(entity.name, entity.username)
    }
}
