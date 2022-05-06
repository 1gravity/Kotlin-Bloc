package com.onegravity.bloc.util

import com.onegravity.bloc.sample.posts.data.PostDataRepository
import com.onegravity.bloc.sample.posts.data.posts.network.AvatarUrlGenerator
import com.onegravity.bloc.sample.posts.data.posts.network.PostNetworkDataSource
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import com.onegravity.bloc.utils.logger as blocLogger

// called by Android and iOS
fun initKoin(koinAppDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        koinAppDeclaration()
        modules(commonModule)
    }
}

inline fun <reified T> getKoinInstance() =
    object : KoinComponent {
        val value: T by inject()
    }.value

private val commonModule = module {
    // we can either inject a Logger or just use the static Logger.x(msg)
    single { blocLogger }

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

    single { AvatarUrlGenerator() }
}
