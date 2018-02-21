package io.somedomain.stepiktestapp.api

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class StepikApi(
        private val apiEndpoint: String,
        private val okHttpClient: OkHttpClient,
        private val gson: Gson
) {

    private var stepikApi: StepikApiController? = null

    fun api(): StepikApiController {
        var instance = stepikApi
        if (instance == null) {
            synchronized(this) {
                instance = stepikApi
                if (instance == null) {
                    stepikApi = create(StepikApiController::class.java)
                    instance = stepikApi
                }
            }
        }
        return instance!!
    }

    private fun <T> create(tClass: Class<T>): T {
        return Retrofit.Builder()
                .baseUrl(apiEndpoint)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(getConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(tClass)
    }

    private fun getConverterFactory(): Converter.Factory {
        return object : Converter.Factory() {
            override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>, retrofit: Retrofit?): Converter<ResponseBody, *> {
                val unwrapKey = annotations.getAnnotation<Unwrap>()?.key
                val adapter = gson.getAdapter(TypeToken.get(type))
                return Converter<ResponseBody, Any?> { value ->
                    val jsonReader = gson.newJsonReader(value.charStream())
                    val root = value.use { gson.fromJson<JsonElement>(jsonReader, JsonElement::class.java) }
                    adapter.fromJsonTree(if (unwrapKey == null) root else {
                        val obj = root.asJsonObject
                        val data = obj.get(unwrapKey)
                        obj.remove(unwrapKey)
                        obj.add("data", data)
                        obj
                    })
                }
            }
        }
    }

}