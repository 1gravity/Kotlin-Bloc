package org.orbitmvi.orbit.sample.posts.data.posts.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.sample.posts.data.posts.model.CommentData
import org.orbitmvi.orbit.sample.posts.data.posts.model.PostData
import org.orbitmvi.orbit.sample.posts.data.posts.model.UserData

class PostNetworkDataSource : KoinComponent {

    private val baseURL = "https://jsonplaceholder.typicode.com"

    private val httClient by inject<HttpClient>()

    suspend fun getPost(id: Int) =
        try {
            val response = httClient.request("$baseURL/posts/$id") {
                contentType(ContentType.Application.Json)
                method = HttpMethod.Get
            }
            Ok(response.body<PostData>())
        } catch (e: Exception) {
            Err(e)
        }

    suspend fun getPosts() =
        try {
            val response = httClient.request("$baseURL/posts") {
                contentType(ContentType.Application.Json)
                method = HttpMethod.Get
            }
            Ok(response.body<List<PostData>>())
        } catch (e: Exception) {
            Err(e)
        }

    suspend fun getUsers() =
        try {
            val response = httClient.request("$baseURL/users") {
                contentType(ContentType.Application.Json)
                method = HttpMethod.Get
            }
            Ok(response.body<List<UserData>>())
        } catch (e: Exception) {
            Err(e)
        }

    suspend fun getComments() =
        try {
            val response = httClient.request("$baseURL/comments") {
                contentType(ContentType.Application.Json)
                method = HttpMethod.Get
            }
            Ok(response.body<List<CommentData>>())
        } catch (e: Exception) {
            Err(e)
        }

}
