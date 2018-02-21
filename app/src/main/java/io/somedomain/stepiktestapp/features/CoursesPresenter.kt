package io.somedomain.stepiktestapp.features

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.somedomain.stepiktestapp.base.BaseListView
import io.somedomain.stepiktestapp.base.CacheableListPresenter
import io.somedomain.stepiktestapp.common.applySchedulers
import io.somedomain.stepiktestapp.model.Course
import io.somedomain.stepiktestapp.model.PageResponse
import io.somedomain.stepiktestapp.repository.CoursesRepository

class CoursesPresenter(
        private var coursersRepository: CoursesRepository
) : CacheableListPresenter<Course, CoursesPresenter.View>() {

    init {
        coursersRepository.subscribeToCourseChanges()
                .applySchedulers()
                .subscribe({
                    view()?.onItemChanged(it)
                }, this::onError)
    }

    fun reloadFavourites() {
        coursersRepository.loadFavourites()
                .applySchedulers()
                .deliverToSubject()
    }

    fun loadFavourites() {
        coursersRepository.loadFavourites()
                .mergeWithCache()
                .applySchedulers()
                .deliverToSubject()
    }

    fun searchCourses(query: String) {
        coursersRepository.search(query)
                .mergeWithFavourites()
                .applySchedulers()
                .subscribe({ onData(it) }, { onError(it) })
    }

    open protected fun Observable<PageResponse<List<Course>>>.mergeWithFavourites(): Observable<PageResponse<List<Course>>> =
            flatMap { result ->
                Observable.zip(Observable.just(result),
                        coursersRepository.loadFavourites(),
                        BiFunction { t1: PageResponse<List<Course>>, t2: PageResponse<List<Course>> ->
                            val list = t1.data.toMutableList()
                            for (favouriteItem in t2.data) {
                                for (i in 0 until list.size) {
                                    if (list[i].course == favouriteItem.course) {
                                        list[i] = favouriteItem
                                    }
                                }
                            }
                            PageResponse<List<Course>>(result.meta, list)
                        })
            }

    fun addCourseToFavourites(course: Course) {
        coursersRepository.addToFavourites(course)
                .applySchedulers()
                .subscribe({}, this::onError)
    }

    fun removeCourseFromFavourites(course: Course) {
        coursersRepository.removeFromFavourites(course)
                .applySchedulers()
                .subscribe({}, this::onError)
    }

    interface View : BaseListView<Course> {
        fun onItemChanged(course: Course)
    }
}