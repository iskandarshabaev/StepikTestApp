package io.somedomain.stepiktestapp.data

import io.reactivex.Observable
import io.somedomain.stepiktestapp.data.db.LocalDB
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.data.model.Meta
import io.somedomain.stepiktestapp.data.model.PageResponse

interface CoursesLocalDataSource {

    fun loadFavourites(): Observable<PageResponse<MutableList<Course>>>

    fun addToFavourites(course: Course): Observable<Course>

    fun removeFromFavourites(course: Course): Observable<Course>

    fun clear(): Observable<Unit>
}


class CoursesLocalDataSourceDefault : CoursesLocalDataSource {

    override fun loadFavourites(): Observable<PageResponse<MutableList<Course>>> {
        return Observable.fromCallable { LocalDB.getInstance().loadCourses() }
                .map {
                    PageResponse(Meta(1, false, false), it)
                }
    }

    override fun addToFavourites(course: Course): Observable<Course> {
        return Observable.fromCallable { LocalDB.getInstance().saveCourse(course) }
                .map { course }
    }

    override fun removeFromFavourites(course: Course): Observable<Course> {
        return Observable.fromCallable { LocalDB.getInstance().removeCourse(course.id) }
                .map { course }
    }

    override fun clear(): Observable<Unit> {
        return Observable.fromCallable { LocalDB.getInstance().removeAllCourses() }
    }
}