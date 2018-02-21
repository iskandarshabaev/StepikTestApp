package io.somedomain.stepiktestapp.features

import io.somedomain.stepiktestapp.base.BaseListView
import io.somedomain.stepiktestapp.base.CacheableListPresenter
import io.somedomain.stepiktestapp.common.applySchedulers
import io.somedomain.stepiktestapp.model.Course
import io.somedomain.stepiktestapp.repository.CoursesRepository

class CoursesPresenter(
        private var coursersRepository: CoursesRepository
) : CacheableListPresenter<Course, CoursesPresenter.View>() {

    init {
        coursersRepository.subscribeToCourseChanges()
                .applySchedulers()
                .subscribe({ view()?.onItemChanged(it) }, this::onError)
    }

    fun loadFavourites() {
        coursersRepository.loadFavourites()
                .mergeWithCache()
                .applySchedulers()
                .deliverToSubject()
    }

    fun searchCourses(query: String) {
        coursersRepository.search(query)
                .applySchedulers()
                .subscribe({ onData(it) }, { onError(it) })
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