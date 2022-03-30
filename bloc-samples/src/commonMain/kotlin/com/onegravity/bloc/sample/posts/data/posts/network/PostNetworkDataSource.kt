package com.onegravity.bloc.sample.posts.data.posts.network

import com.github.michaelbull.result.runCatching
import com.onegravity.bloc.sample.posts.data.posts.model.CommentData
import com.onegravity.bloc.sample.posts.data.posts.model.PostData
import com.onegravity.bloc.sample.posts.data.posts.model.UserData
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PostNetworkDataSource : KoinComponent {

    private val baseURL = "https://jsonplaceholder.typicode.com"

    private val httClient by inject<HttpClient>()

    suspend fun getPost(postId: Int) = runCatching {
        httClient.get<PostData>("$baseURL/posts/$postId") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun getPosts() = runCatching {
        httClient.get<List<PostData>>("$baseURL/posts") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun getUsers() = runCatching {
        httClient.get<List<UserData>>("$baseURL/users") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun getUser(userId: Int) = runCatching {
        httClient.get<UserData>("$baseURL/users/$userId") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun getComments(postId: Int) = runCatching {
        httClient.get<List<CommentData>>("$baseURL/posts/$postId/comments") {
            contentType(ContentType.Application.Json)
        }
    }

}
