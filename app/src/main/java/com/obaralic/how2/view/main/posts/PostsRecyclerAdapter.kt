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

package com.obaralic.how2.view.main.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.obaralic.how2.R
import com.obaralic.how2.model.Post


class PostsRecyclerAdapter constructor(
    private var posts: List<Post> = arrayListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class PostViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var title: TextView = itemView.findViewById(R.id.title)

        fun bind(post: Post) {
            title.text = post.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.post_list_item_layout, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as PostViewHolder).bind(posts[position])

    internal fun setPosts(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }


}