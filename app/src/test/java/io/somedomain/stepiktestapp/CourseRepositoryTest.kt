package io.somedomain.stepiktestapp

import io.reactivex.Observable
import io.somedomain.stepiktestapp.data.CoursesLocalDataSource
import io.somedomain.stepiktestapp.data.CoursesRemoteDataSource
import io.somedomain.stepiktestapp.data.CoursesRepository
import io.somedomain.stepiktestapp.data.CoursesRepositoryDefault
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.util.Courses
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class CourseRepositoryTest {

    private lateinit var coursesRepository: CoursesRepository

    @Mock
    private lateinit var coursesRemoteDataSource: CoursesRemoteDataSource

    @Mock
    private lateinit var coursesLocalDataSource: CoursesLocalDataSource

    @Before
    fun setupTasksRepository() {
        MockitoAnnotations.initMocks(this)
        coursesRepository = CoursesRepositoryDefault(coursesRemoteDataSource, coursesLocalDataSource)
    }

    @Test
    fun loadFavouriteCourses() {
        val result = Courses.getValidCourses(4)
        `when`(coursesLocalDataSource.loadFavourites()).thenReturn(Observable.just(result))
        val testObserver = coursesRepository.loadFavourites().test()
        verify(coursesLocalDataSource).loadFavourites()
        testObserver.assertValue(result)
    }

    @Test
    fun searchCourses() {
        val query = ""
        val page = 1
        val searchResult = Courses.getValidCourses(4)
        val result = Courses.getValidCourses(2, true)

        `when`(coursesRemoteDataSource.search(query, page)).thenReturn(Observable.just(searchResult))
        `when`(coursesLocalDataSource.loadFavourites()).thenReturn(Observable.just(result))

        val testObserver = coursesRepository.search(query, page).test()

        verify(coursesRemoteDataSource).search(query, page)
        verify(coursesLocalDataSource).loadFavourites()

        testObserver.assertValue {
            it.data.size == 4
        }
        testObserver.assertValue {
            it.data.containsAll(result.data)
        }
        testObserver.assertValue {
            it.data.filter { it.isFavourite }.size == 2
        }
    }

    @Test
    fun addToFavourites() {
        val testObserver = coursesRepository.subscribeToCourseChanges().test()

        val course = Course().also {
            it.id = 9
            it.course = 9
            it.courseTitle = "some_title"
            it.courseCover = "some_cover"
            it.isFavourite = true
        }

        `when`(coursesLocalDataSource.addToFavourites(course)).thenReturn(Observable.just(course))
        val addFavouriteObserver = coursesRepository.addToFavourites(course).test()

        verify(coursesLocalDataSource).addToFavourites(course)

        addFavouriteObserver.assertValue(course)
        testObserver.assertValue(course)
        testObserver.assertValue { it.isFavourite }
    }

    @Test
    fun removeFromFavourites() {
        val testObserver = coursesRepository.subscribeToCourseChanges().test()

        val course = Course().also {
            it.id = 9
            it.course = 9
            it.courseTitle = "some_title"
            it.courseCover = "some_cover"
            it.isFavourite = false
        }

        `when`(coursesLocalDataSource.removeFromFavourites(course)).thenReturn(Observable.just(course))
        val addFavouriteObserver = coursesRepository.removeFromFavourites(course).test()

        verify(coursesLocalDataSource).removeFromFavourites(course)

        addFavouriteObserver.assertValue(course)
        testObserver.assertValue(course)
        testObserver.assertValue { !it.isFavourite }
    }
}