package io.somedomain.stepiktestapp.repository

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.somedomain.stepiktestapp.api.StepikApiController
import io.somedomain.stepiktestapp.model.Course
import io.somedomain.stepiktestapp.model.Meta
import io.somedomain.stepiktestapp.model.PageResponse
import io.somedomain.stepiktestapp.repository.db.LocalDB

interface CoursesRepository {

    fun search(query: String): Observable<PageResponse<List<Course>>>

    fun addToFavourites(course: Course): Observable<Course>

    fun removeFromFavourites(course: Course): Observable<Course>

    fun loadFavourites(): Observable<PageResponse<List<Course>>>

    fun subscribeToCourseChanges(): Observable<Course>
}

class DefaultCoursesRepository(val stepikApi: StepikApiController) : CoursesRepository {

    var courseChangeSubject = PublishSubject.create<Course>()

    override fun search(query: String): Observable<PageResponse<List<Course>>> {
        return stepikApi.searchCourses(query)
    }

    override fun loadFavourites(): Observable<PageResponse<List<Course>>> {
        return Observable.fromCallable { LocalDB.getInstance().loadCourses() }
                .map {
                    PageResponse(Meta(1, false, false), it)
                }
    }

    override fun addToFavourites(course: Course): Observable<Course> {
        return Observable.fromCallable { LocalDB.getInstance().saveCourse(course) }
                .map { course }
                .doOnNext { courseChangeSubject.onNext(it) }
    }

    override fun removeFromFavourites(course: Course): Observable<Course> {
        return Observable.fromCallable { LocalDB.getInstance().removeCourse(course.id) }
                .map { course }
                .doOnNext { courseChangeSubject.onNext(it) }
    }

    override fun subscribeToCourseChanges(): Observable<Course> {
        return courseChangeSubject
    }
}