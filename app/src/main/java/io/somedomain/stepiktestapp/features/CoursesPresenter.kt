package io.somedomain.stepiktestapp.features

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.somedomain.stepiktestapp.base.BaseListView
import io.somedomain.stepiktestapp.base.CacheableListPresenter
import io.somedomain.stepiktestapp.common.applySchedulers
import io.somedomain.stepiktestapp.data.CoursesRepository
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.features.CoursesFragment.Companion.typeFavourites

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