package com.onegravity.bloc.posts

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.blocContext
import com.onegravity.bloc.sample.posts.bloc.Post
import com.onegravity.bloc.sample.posts.bloc.PostState
import com.onegravity.bloc.toObservable
import com.onegravity.bloc.utils.BlocObservableOwner
import org.koin.core.component.KoinComponent
import com.onegravity.bloc.sample.posts.domain.repositories.Post as PostData

class PostViewModel(context: ActivityBlocContext, post: PostData) :
    ViewModel(),
    BlocObservableOwner<PostState, Unit>,
    KoinComponent {

    private val bloc = Post.bloc(blocContext(context))
        .also { it.send(Post.Action.Load(post.id)) }

    override val observable = bloc.toObservable()

}
