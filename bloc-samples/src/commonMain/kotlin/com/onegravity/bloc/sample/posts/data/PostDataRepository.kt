package com.onegravity.bloc.sample.posts.data

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.binding.binding
import com.onegravity.bloc.sample.posts.data.posts.network.AvatarUrlGenerator
import com.onegravity.bloc.sample.posts.data.posts.network.PostNetworkDataSource
import com.onegravity.bloc.sample.posts.domain.repositories.PostComment
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository

class PostDataRepository(
    private val networkDataSource: PostNetworkDataSource,
    private val avatarUrlGenerator: AvatarUrlGenerator
) : PostRepository {
    override suspend fun getOverviews(): Result<List<Post>, Throwable> =
        binding {
            val posts = networkDataSource.getPosts().bind()
            val users = networkDataSource.getUsers().bind()
            posts.map { post ->
                val user = users.first { it.id == post.userId }
                Post(
                    id = post.id,
                    avatarUrl = avatarUrlGenerator.generateUrl(user.email),
                    title = post.title,
                    body = post.body,
                    username = user.name
                )
            }.sortedBy { it.title }
        }

    override suspend fun getDetail(id: Int): Result<Post, Throwable> =
        binding {
            val post = networkDataSource.getPost(id).bind()
            val user = networkDataSource.getUser(post.userId).bind()
            val comments = networkDataSource.getComments(post.id).bind()
            Post(
                id = post.id,
                avatarUrl = avatarUrlGenerator.generateUrl(user.email),
                title = post.title,
                body = post.body,
                username = user.name,
                comments = comments.map { PostComment(it.id, it.name, it.email, it.body) }
            )
        }

}
