package com.onegravity.bloc.sample.posts.data.posts.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
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

    suspend fun getPost(id: Int) =
        try {
            val response: PostData = httClient.get("$baseURL/posts/$id") {
                contentType(ContentType.Application.Json)
            }
            Ok(response)
        } catch (e: Exception) {
            Err(e)
        }

    suspend fun getPosts() =
        try {
            val response: List<PostData> = httClient.get("$baseURL/posts") {
                contentType(ContentType.Application.Json)
            }
            Ok(response)
        } catch (e: Exception) {
            Err(e)
        }

    suspend fun getUsers() =
        try {
            val response: List<UserData> = httClient.get("$baseURL/users") {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
            }
            Ok(response)
        } catch (e: Exception) {
            Err(e)
        }

    suspend fun getComments() =
        try {
            val response: List<CommentData> = httClient.get("$baseURL/comments") {
                contentType(ContentType.Application.Json)
            }
            Ok(response)
        } catch (e: Exception) {
            Err(e)
        }

}
