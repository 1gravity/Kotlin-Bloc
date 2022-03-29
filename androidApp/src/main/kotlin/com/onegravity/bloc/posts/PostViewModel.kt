package com.onegravity.bloc.posts

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.blocContext
import com.onegravity.bloc.sample.posts.bloc.Post
import com.onegravity.bloc.sample.posts.bloc.PostState
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import com.onegravity.bloc.utils.BlocObservableOwner
import com.onegravity.bloc.toObservable
import org.koin.core.component.KoinComponent

class PostViewModel(postOverview: PostOverview) :
    ViewModel(),
    BlocObservableOwner<PostState, Unit>,
    KoinComponent {

    private val bloc = Post.bloc(blocContext())
        .also { it.send(Post.Action.Load(postOverview.id)) }

    override val observable = bloc.toObservable()

}
