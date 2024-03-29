package com.onegravity.bloc.posts

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.BlocObservableOwner
import com.onegravity.bloc.blocContext
import com.onegravity.bloc.sample.posts.bloc.Post
import com.onegravity.bloc.sample.posts.bloc.PostState
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.toObservable
import com.onegravity.bloc.sample.posts.domain.repositories.Post as PostData

/**
 * Not used any more but we keep it around for illustration purposes
 */
class PostViewModel(
    repository: PostRepository,
    post: PostData
) : ViewModel(), BlocObservableOwner<PostState, Unit> {

    private val bloc = Post.bloc(blocContext(), repository)
        .also { it.send(Post.Action.Load(post.id)) }

    override val observable = bloc.toObservable()

}
