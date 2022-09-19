package com.onegravity.bloc.sample.posts.data.posts.network

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.onegravity.bloc.sample.posts.data.posts.model.CommentData
import com.onegravity.bloc.sample.posts.data.posts.model.PostData
import com.onegravity.bloc.sample.posts.data.posts.model.UserData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PostNetworkDataSource : KoinComponent {

    private val baseURL = "https://jsonplaceholder.typicode.com"

    private val httClient by inject<HttpClient>()

    suspend fun getPost(postId: Int): Result<PostData, Throwable> = runCatching {
        delay(1000)
        httClient.get("$baseURL/posts/$postId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getPosts(): Result<List<PostData>, Throwable> = runCatching {
        httClient.get("$baseURL/posts") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getUsers(): Result<List<UserData>, Throwable> = runCatching {
        httClient.get("$baseURL/users") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getUser(userId: Int): Result<UserData, Throwable> = runCatching {
        delay(5000)
        httClient.get("$baseURL/users/$userId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getComments(postId: Int): Result<List<CommentData>, Throwable> = runCatching {
        delay(2000)
        httClient.get("$baseURL/posts/$postId/comments") {
            contentType(ContentType.Application.Json)
        }.body()
    }

}
