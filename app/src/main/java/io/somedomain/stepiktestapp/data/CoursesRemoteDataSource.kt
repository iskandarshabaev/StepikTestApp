package io.somedomain.stepiktestapp.data

import io.reactivex.Observable
import io.somedomain.stepiktestapp.api.StepikApi
import io.somedomain.stepiktestapp.api.StepikApiController
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.data.model.PageResponse

interface CoursesRemoteDataSource {

    fun search(query: String, page: Int): Observable<PageResponse<MutableList<Course>>>
}


class CoursesRemoteDataSourceDefault(
        private val stepikApi: StepikApiController
) : CoursesRemoteDataSource {

    override fun search(query: String, page: Int): Observable<PageResponse<MutableList<Course>>> {
        return stepikApi.searchCourses(query, page).doOnNext {
            it.data = it.data.distinctBy { it.id }.toMutableList()
            it.data = it.data.filter {
                !it.courseTitle.isNullOrEmpty() &&
                        !it.courseCover.isNullOrEmpty()
            }.toMutableList()
        }
    }
}