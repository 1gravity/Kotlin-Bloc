package com.onegravity.bloc.util

import com.onegravity.bloc.sample.posts.compose.PostState
import com.onegravity.bloc.sample.posts.compose.PostsState
import com.onegravity.bloc.sample.posts.compose.PostsComponent
import com.onegravity.bloc.sample.posts.compose.PostsComponentImpl
import com.onegravity.bloc.sample.posts.compose.PostsRootState
import com.onegravity.bloc.sample.posts.data.PostDataRepository
import com.onegravity.bloc.sample.posts.data.posts.network.AvatarUrlGenerator
import com.onegravity.bloc.sample.posts.data.posts.network.PostNetworkDataSource
import com.onegravity.bloc.sample.posts.data.posts.network.PostNetworkDataSourceImpl
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.blocState
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val commonModule = module {
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

    factory<PostsComponent> { PostsComponentImpl(it.get(), get(), get()) }

    factory {
        blocState(PostsRootState(postsState = PostsState(), postState = PostState()))
    }

    single<PostNetworkDataSource> { PostNetworkDataSourceImpl() }

    singleOf(::PostDataRepository) bind PostRepository::class

    single { ::AvatarUrlGenerator { email -> "https://robohash.org/$email?set=set1" } }

}
