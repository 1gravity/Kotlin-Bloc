package com.onegravity.bloc.util

import com.onegravity.bloc.sample.posts.data.PostDataRepository
import com.onegravity.bloc.sample.posts.data.posts.network.AvatarUrlGenerator
import com.onegravity.bloc.sample.posts.data.posts.network.PostNetworkDataSource
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.utils.Logger
import com.onegravity.bloc.utils.LoggerImpl
import com.onegravity.bloc.utils.logger
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module
import co.touchlab.kermit.Logger as KermitLogger

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
        KermitLogger.setTag("bloc")
        KermitLogger.setLogWriters(logger())
        LoggerImpl
    }

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
