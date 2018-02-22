package io.somedomain.stepiktestapp.di

import android.content.Context
import io.somedomain.stepiktestapp.common.app
import io.somedomain.stepiktestapp.data.*

object Injection {

    @Volatile
    private var _coursesRemoteDataSource: CoursesRemoteDataSource? = null

    fun coursesRemoteDataSource(context: Context): CoursesRemoteDataSource {
        var local = _coursesRemoteDataSource
        if (local == null) {
            synchronized(this) {
                if (local == null) {
                    local = CoursesRemoteDataSourceDefault(context.app.stepikApi.api())
                    _coursesRemoteDataSource = local
                }
            }
        }
        return local!!
    }

    @Volatile
    private var _coursesLocalDataSource: CoursesLocalDataSource? = null

    fun coursesLocalDataSource(context: Context): CoursesLocalDataSource {
        var local = _coursesLocalDataSource
        if (local == null) {
            synchronized(this) {
                if (local == null) {
                    local = CoursesLocalDataSourceDefault()
                    _coursesLocalDataSource = local
                }
            }
        }
        return local!!
    }

    @Volatile
    private var _coursesRepository: CoursesRepository? = null

    fun coursesRepository(context: Context): CoursesRepository {
        var local = _coursesRepository
        if (local == null) {
            synchronized(this) {
                if (local == null) {
                    local = CoursesRepositoryDefault(
                            coursesRemoteDataSource(context),
                            coursesLocalDataSource(context))
                    _coursesRepository = local
                }
            }
        }
        return local!!

    }
}