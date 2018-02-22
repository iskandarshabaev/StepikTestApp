package io.somedomain.stepiktestapp.repository

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.somedomain.stepiktestapp.api.StepikApiController
import io.somedomain.stepiktestapp.model.Course
import io.somedomain.stepiktestapp.model.Meta
import io.somedomain.stepiktestapp.model.PageResponse
import io.somedomain.stepiktestapp.repository.db.LocalDB

interface CoursesRepository {

    fun search(query: String, page: Int): Observable<PageResponse<MutableList<Course>>>

    fun addToFavourites(course: Course): Observable<Course>

    fun removeFromFavourites(course: Course): Observable<Course>

    fun loadFavourites(): Observable<PageResponse<MutableList<Course>>>

    fun subscribeToCourseChanges(): Observable<Course>
}

class DefaultCoursesRepository(val stepikApi: StepikApiController) : CoursesRepository {

    var courseChangeSubject = PublishSubject.create<Course>()

    override fun search(query: String, page: Int): Observable<PageResponse<MutableList<Course>>> {
        return stepikApi.searchCourses(query, page).doOnNext {
            it.data = it.data.distinctBy { it.id }.toMutableList()
            it.data = it.data.filter {
                !it.courseTitle.isNullOrEmpty() &&
                        !it.courseCover.isNullOrEmpty()
            }.toMutableList()
        }
    }

    override fun loadFavourites(): Observable<PageResponse<MutableList<Course>>> {
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