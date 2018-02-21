package io.somedomain.stepiktestapp.repository

import io.somedomain.stepiktestapp.api.StepikApi

class RepositoryProvider(val api: StepikApi) {

    @Volatile
    private var _coursesRepository: CoursesRepository? = null
    val coursesRepository: CoursesRepository
        get() {
            if (_coursesRepository == null) {
                _coursesRepository = DefaultCoursesRepository(api.api())
            }
            return _coursesRepository!!

        }

}