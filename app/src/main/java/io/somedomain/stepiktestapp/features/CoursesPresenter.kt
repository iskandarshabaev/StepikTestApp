package io.somedomain.stepiktestapp.features

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.somedomain.stepiktestapp.base.BaseListView
import io.somedomain.stepiktestapp.base.CacheableListPresenter
import io.somedomain.stepiktestapp.common.applySchedulers
import io.somedomain.stepiktestapp.features.CoursesFragment.Companion.typeFavourites
import io.somedomain.stepiktestapp.model.Course
import io.somedomain.stepiktestapp.model.PageResponse
import io.somedomain.stepiktestapp.repository.CoursesRepository

class CoursesPresenter(
        private var type: Int,
        private var coursersRepository: CoursesRepository
) : CacheableListPresenter<Course, CoursesPresenter.View>() {

    private var disposable: Disposable? = null

    init {
        disposable = coursersRepository.subscribeToCourseChanges()
                .applySchedulers()
                .subscribe({
                    view()?.onItemChanged(it)
                    updateCacheSubject(it)
                }, this::onError)
    }

    fun updateCacheSubject(course: Course) {
        if (cacheSubject.value == null) {
            return
        }
        val list = cacheSubject.value.data
        if (type == typeFavourites) {
            if (course.isFavourite) {
                list.add(course)
            } else {
                list.removeAll { it.course == course.course }
            }
        }
    }

    fun loadFavourites() {
        coursersRepository.loadFavourites()
                .mergeWithCache()
                .applySchedulers()
                .deliverToSubject()
    }

    fun searchCourses(query: String, page: Int) {
        coursersRepository.search(query, page)
                .mergeWithFavourites()
                .flatMap {
                    if (page > 1) {
                        Observable.just(it).mergeWithCache()
                    } else {
                        Observable.just(it)
                    }
                }
                .applySchedulers()
                .deliverToSubject()
    }

    open protected fun Observable<PageResponse<MutableList<Course>>>.mergeWithFavourites(): Observable<PageResponse<MutableList<Course>>> =
            flatMap { result ->
                Observable.zip(Observable.just(result),
                        coursersRepository.loadFavourites(),
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

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()

    }
}