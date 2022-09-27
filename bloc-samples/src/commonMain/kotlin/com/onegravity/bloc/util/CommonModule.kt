package com.onegravity.bloc.util

import com.onegravity.bloc.sample.posts.compose.PostState
import com.onegravity.bloc.sample.posts.compose.PostsRootState
import com.onegravity.bloc.sample.posts.compose.PostsState
import com.onegravity.bloc.sample.posts.data.PostDataRepository
import com.onegravity.bloc.sample.posts.data.posts.network.AvatarUrlGenerator
import com.onegravity.bloc.sample.posts.data.posts.network.PostNetworkDataSource
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.blocState
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal val commonModule = module {
    // we can either inject a Logger or just use the static Logger.x(msg)
    single { com.onegravity.bloc.utils.logger }

    single {
        HttpClient {
            expectSuccess = true
            install(Logging)
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    single { PostNetworkDataSource() }

    single<PostRepository> { PostDataRepository(get(), get()) }

    single {
        blocState(PostsRootState(postsState = PostsState(), postState = PostState()))
    }

    single { AvatarUrlGenerator() }
}
