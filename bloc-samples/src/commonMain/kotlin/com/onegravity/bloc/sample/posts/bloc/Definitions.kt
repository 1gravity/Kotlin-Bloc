package com.onegravity.bloc.sample.posts.bloc

import com.onegravity.bloc.Bloc

typealias PostsBloc = Bloc<PostsState, Posts.Action, Posts.OpenPost, PostsState>

typealias PostBloc = Bloc<PostState, Post.Action, Unit, PostState>
