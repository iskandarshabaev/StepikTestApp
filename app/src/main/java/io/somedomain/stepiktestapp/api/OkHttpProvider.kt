package io.somedomain.stepiktestapp.api

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class OkHttpProvider {
    companion object {

        @Volatile
        private var sClient: OkHttpClient? = null

        fun provideClient(): OkHttpClient {
            var client = sClient
            if (client == null) {
                synchronized(OkHttpClient::class.java) {
                    client = sClient
                    if (client == null) {
                        sClient = buildClient()
                        client = sClient
                    }
                }
            }
            return client!!
        }

        private fun buildClient(): OkHttpClient {
            val builder = makeBaseBuilder()
            builder.addInterceptor(LoggingInterceptor.create())
            return builder.build()
        }

        private fun makeBaseBuilder(): OkHttpClient.Builder {
            val builder = OkHttpClient.Builder()
            return builder.connectTimeout(10, TimeUnit.SECONDS)
        }
    }
}
