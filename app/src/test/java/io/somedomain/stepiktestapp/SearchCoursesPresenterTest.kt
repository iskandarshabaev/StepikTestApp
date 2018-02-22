package io.somedomain.stepiktestapp

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.atLeast
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.somedomain.stepiktestapp.data.CoursesRepository
import io.somedomain.stepiktestapp.features.CoursesFragment.Companion.typeSearch
import io.somedomain.stepiktestapp.features.CoursesPresenter
import io.somedomain.stepiktestapp.util.Courses
import io.somedomain.stepiktestapp.util.RxSchedulersHooks
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SearchCoursesPresenterTest {

    @Mock
    private lateinit var coursesRepository: CoursesRepository

    @Mock
    private lateinit var view: CoursesPresenter.View

    private lateinit var presenter: CoursesPresenter

    @Before
    fun setupTasksRepository() {
        MockitoAnnotations.initMocks(this)
        RxSchedulersHooks.setUp()
        `when`(coursesRepository.subscribeToCourseChanges()).thenReturn(Observable.empty())
        presenter = CoursesPresenter(typeSearch, coursesRepository)
        presenter.attachView(view)
    }

    @Test
    fun testPresenterInitialization() {
        assert(presenter.view != null)
        assert(presenter.view?.get() != null)
    }

    @Test
    fun testPresenterReattaching() {
        val data = Courses.getValidCourses(5)
        `when`(coursesRepository.loadFavourites()).thenReturn(Observable.just(data))
        assert(presenter.view != null)
        assert(presenter.view?.get() != null)
        presenter.loadFavourites()
        verify(view).showContent(data)
        presenter.detachView()
        presenter.attachView(view)
        verify(view, atLeast(2)).showContent(data)
    }

    @Test
    fun loadFavourites() {
        val data = Courses.getValidCourses(5)
        `when`(coursesRepository.loadFavourites()).thenReturn(Observable.just(data))
        presenter.loadFavourites()
        verify(view).showContent(data)
    }

    @Test
    fun loadFavourites_withException() {
        val exception = Throwable("TestException")
        `when`(coursesRepository.loadFavourites()).thenReturn(Observable.error(exception))
        presenter.loadFavourites()
        verify(view, never()).showContent(any())
        verify(view).showError(exception)
    }

    @Test
    fun searchCourses() {
        val query = ""
        val page = 1
        val data = Courses.getValidCourses(5)
        `when`(coursesRepository.search(query, page)).thenReturn(Observable.just(data))
        presenter.searchCourses(query, page)
        verify(view).showContent(data)
    }

    @Test
    fun searchCourses_withException() {
        val exception = Throwable("TestException")
        val query = ""
        val page = 1
        `when`(coursesRepository.search(query, page)).thenReturn(Observable.error(exception))
        presenter.searchCourses(query, page)
        verify(view, never()).showContent(any())
        verify(view).showError(exception)
    }

    @Test
    fun addToFavourite() {
        val data = Courses.getCourse(true)
        `when`(coursesRepository.addToFavourites(data)).thenReturn(Observable.just(data))
        presenter.addCourseToFavourites(data)
    }

    @Test
    fun addToFavourite_withException() {
        val exception = Throwable("TestException")
        val data = Courses.getCourse(true)
        `when`(coursesRepository.addToFavourites(data)).thenReturn(Observable.error(exception))
        presenter.addCourseToFavourites(data)
        verify(view).showError(exception)
    }

    @Test
    fun removeFromFavourite() {
        val data = Courses.getCourse(false)
        `when`(coursesRepository.removeFromFavourites(data)).thenReturn(Observable.just(data))
        presenter.removeCourseFromFavourites(data)
    }

    @Test
    fun removeFromFavourite_withException() {
        val exception = Throwable("TestException")
        val data = Courses.getCourse(true)
        `when`(coursesRepository.removeFromFavourites(data)).thenReturn(Observable.error(exception))
        presenter.removeCourseFromFavourites(data)
        verify(view).showError(exception)
    }

}