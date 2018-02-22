package io.somedomain.stepiktestapp.data

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.data.model.PageResponse

public class CoursesRepositoryDefault(
        private val coursesRemoteDataSource: CoursesRemoteDataSource,
        private val coursesLocalDataSource: CoursesLocalDataSource
) : CoursesRepository {

    private var courseChangeSubject = PublishSubject.create<Course>()

    override fun search(query: String, page: Int): Observable<PageResponse<MutableList<Course>>> {
        return coursesRemoteDataSource.search(query, page)
                .mergeWithFavourites()
    }

    private fun Observable<PageResponse<MutableList<Course>>>.mergeWithFavourites(): Observable<PageResponse<MutableList<Course>>> =
            flatMap { result ->
                Observable.zip(Observable.just(result),
                        loadFavourites(),
                        BiFunction { t1: PageResponse<MutableList<Course>>, t2: PageResponse<MutableList<Course>> ->
                            val list = t1.data
                            for (favouriteItem in t2.data) {
                                for (i in 0 until list.size) {
                                    if (list[i].course == favouriteItem.course) {
                                        list[i] = favouriteItem
                                    }
                                }
                            }
                            PageResponse(result.meta, list)
                        })
            }

    override fun loadFavourites(): Observable<PageResponse<MutableList<Course>>> {
        return coursesLocalDataSource.loadFavourites()
    }

    override fun addToFavourites(course: Course): Observable<Course> {
        return coursesLocalDataSource.addToFavourites(course)
                .doOnNext { courseChangeSubject.onNext(it) }
    }

    override fun removeFromFavourites(course: Course): Observable<Course> {
        return coursesLocalDataSource.removeFromFavourites(course)
                .doOnNext { courseChangeSubject.onNext(it) }
    }

    override fun subscribeToCourseChanges(): Observable<Course> {
        return courseChangeSubject
    }
}