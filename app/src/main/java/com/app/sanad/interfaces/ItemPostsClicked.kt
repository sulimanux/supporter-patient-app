package com.app.sanad.interfaces

import android.view.View
import com.app.sanad.posts.data.entity.Post
import com.app.sanad.model.Supplication

interface ItemPostsClicked {

    fun onItemClicked(post: Post)
    fun updateSeenBySupporter(post: Post)
}