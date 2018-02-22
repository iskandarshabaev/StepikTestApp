package io.somedomain.stepiktestapp.data

import io.reactivex.Observable
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.data.model.PageResponse

interface CoursesRepository {

    fun search(query: String, page: Int): Observable<PageResponse<MutableList<Course>>>

    fun addToFavourites(course: Course): Observable<Course>

    fun removeFromFavourites(course: Course): Observable<Course>

    fun loadFavourites(): Observable<PageResponse<MutableList<Course>>>

    fun subscribeToCourseChanges(): Observable<Course>
}