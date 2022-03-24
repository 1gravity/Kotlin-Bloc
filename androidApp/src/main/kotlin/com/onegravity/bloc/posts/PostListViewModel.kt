package com.onegravity.bloc.posts

import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.sample.posts.bloc.PostList
import com.onegravity.bloc.sample.posts.bloc.PostListState
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import com.onegravity.bloc.sideEffect
import com.onegravity.bloc.toObservable
import com.onegravity.bloc.utils.BlocObservableOwner
import com.onegravity.bloc.utils.BlocOwner
import org.koin.core.component.KoinComponent

class PostListViewModel(context: ActivityBlocContext) :
    BaseViewModel(context),
    BlocOwner<PostListState, PostList.Action, PostList.OpenPost, PostListState>,
    BlocObservableOwner<PostListState, PostList.OpenPost>,
    KoinComponent {

    override val bloc = PostList.bloc(viewModelContext)

    override val observable = bloc.toObservable()

    fun onPostClicked(post: PostOverview) = sideEffect {
        PostList.OpenPost(post)
    }

      // todo clean this up, this is just test code
//    fun onPostClicked(post: PostOverview) {
//
//        thunk {
//            dispatch(PostList.Action.Loading)
//            val result = repository.getOverviews()
//            dispatch(PostList.Action.Loaded(result))
//        }
//
//        reduce {
//            state.copy(loading = true)
//        }
//
//        sideEffect {
//            PostList.OpenPost(post)
//        }
//
//        reduceAnd {
//            state and PostList.OpenPost(state.overviews.component1()?.first()!!)
//        }
//
//        bloc.reduceAnd {
//            state and PostList.OpenPost(state.overviews.component1()?.first()!!)
//        }
//
//        bloc.send(PostList.Action.Clicked(post))
//    }

}