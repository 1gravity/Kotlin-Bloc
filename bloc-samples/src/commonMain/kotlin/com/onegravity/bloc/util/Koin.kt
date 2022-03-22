package com.onegravity.bloc.util

import com.onegravity.bloc.utils.Logger
import com.onegravity.bloc.utils.LoggerImpl
import com.onegravity.bloc.utils.logger
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module
import org.orbitmvi.orbit.sample.posts.data.PostDataRepository
import org.orbitmvi.orbit.sample.posts.data.posts.network.AvatarUrlGenerator
import org.orbitmvi.orbit.sample.posts.data.posts.network.PostNetworkDataSource
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostRepository

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

    // todo
//    single {
//        Retrofit.Builder()
//            .addConverterFactory(JacksonConverterFactory.create(get()))
//            .baseUrl("https://jsonplaceholder.typicode.com").build()
//    }
    // single { get<Retrofit>().create(TypicodeService::class.java) }

    single {
        HttpClient() {
            install(Logging)
            install(ContentNegotiation) {
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            }
        }
    }

    single { PostNetworkDataSource() }

    single<PostRepository> { PostDataRepository(get(), get()) }

    single { AvatarUrlGenerator() }
}
