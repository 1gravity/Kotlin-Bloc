package com.onegravity.bloc.util

import com.onegravity.bloc.sample.posts.data.PostDataRepository
import com.onegravity.bloc.sample.posts.data.posts.network.AvatarUrlGenerator
import com.onegravity.bloc.sample.posts.data.posts.network.PostNetworkDataSource
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.utils.Logger
import com.onegravity.bloc.utils.LoggerImpl
import com.onegravity.bloc.utils.logger
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

// called by Android and iOS
fun KoinApplication.initKoin() {
    modules(commonModule)
}

inline fun <reified T> getKoinInstance() =
    object : KoinComponent {
        val value: T by inject()
    }.value

private val commonModule = module {
    // we can either inject a Logger or just use the static Logger.x(msg)
    single<Logger> {
        co.touchlab.kermit.Logger.setTag("bloc")
        co.touchlab.kermit.Logger.setLogWriters(logger())
        LoggerImpl
    }

    single {
        HttpClient {
            install(Logging)
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { PostNetworkDataSource() }

    single<PostRepository> { PostDataRepository(get(), get()) }

    single { AvatarUrlGenerator() }
}
