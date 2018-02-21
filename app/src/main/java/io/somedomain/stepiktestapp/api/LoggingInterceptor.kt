package io.somedomain.stepiktestapp.api

import io.somedomain.stepiktestapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException


class LoggingInterceptor private constructor() : Interceptor {

    private val mLoggingInterceptor: Interceptor

    init {
        mLoggingInterceptor = HttpLoggingInterceptor()
                .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return mLoggingInterceptor.intercept(chain)
    }

    companion object {
        fun create(): Interceptor {
            return LoggingInterceptor()
        }
    }

}