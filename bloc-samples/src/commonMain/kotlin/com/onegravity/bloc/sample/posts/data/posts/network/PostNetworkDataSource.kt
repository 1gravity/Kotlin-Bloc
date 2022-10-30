package com.onegravity.bloc.sample.posts.data.posts.network

import com.github.michaelbull.result.Result
import com.onegravity.bloc.sample.posts.data.posts.model.CommentData
import com.onegravity.bloc.sample.posts.data.posts.model.PostData
import com.onegravity.bloc.sample.posts.data.posts.model.UserData

interface PostNetworkDataSource {

    suspend fun getPost(postId: Int): Result<PostData, Throwable>

    suspend fun getPosts(): Result<List<PostData>, Throwable>

    suspend fun getUsers(): Result<List<UserData>, Throwable>

    suspend fun getUser(userId: Int): Result<UserData, Throwable>

    suspend fun getComments(postId: Int): Result<List<CommentData>, Throwable>

}
