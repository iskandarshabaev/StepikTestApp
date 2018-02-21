package io.somedomain.stepiktestapp.common

import android.annotation.SuppressLint
import android.app.Application
import com.squareup.picasso.Picasso
import java.io.File


object ImageHelper {

    private val MAX_WIDTH = 512
    private val MAX_HEIGHT = 384
    internal var size = Math.ceil(Math.sqrt((MAX_WIDTH * MAX_HEIGHT).toDouble())).toInt()
    private lateinit var cacheDir: File
    @SuppressLint("StaticFieldLeak")
    lateinit var picasso: Picasso
        private set

    /**
     * Инициализация хелпера, нужно вызвать в @see[Application.onCreate]
     */
    fun init(application: Application) {
        cacheDir = application.applicationContext.cacheDir
        picasso = Picasso.Builder(application)
                .build()
    }

}