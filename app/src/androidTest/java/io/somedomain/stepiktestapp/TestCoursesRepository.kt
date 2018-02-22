package io.somedomain.stepiktestapp

import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import io.reactivex.Observable
import io.somedomain.stepiktestapp.api.StepikApiController
import io.somedomain.stepiktestapp.data.*
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.data.model.PageResponse
import io.somedomain.stepiktestapp.util.Courses
import io.somedomain.stepiktestapp.util.RxSchedulersHooks
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
public class TestCoursesRepository {

    private lateinit var coursesRepository: CoursesRepository
    private lateinit var coursesRemoteDataSource: CoursesRemoteDataSource
    private lateinit var coursesLocalDataSource: CoursesLocalDataSource
    val results = Courses.getValidCourses(5)

    @Before
    public fun setup() {
        RxSchedulersHooks.setUp()
        coursesRemoteDataSource = CoursesRemoteDataSourceDefault(object : StepikApiController {
            override fun searchCourses(query: String, page: Int): Observable<PageResponse<MutableList<Course>>> {
                return Observable.just(results)
            }
        })
        coursesLocalDataSource = CoursesLocalDataSourceDefault()
        coursesRepository = CoursesRepositoryDefault(coursesRemoteDataSource, coursesLocalDataSource)
        coursesLocalDataSource.clear().blockingFirst()
    }

    @Test
    public fun testLoadEmptyFavourites() {
        val favouritesObserver = coursesRepository.loadFavourites().test()
        favouritesObserver.assertValue {
            it.data.size == 0
        }
    }

    @Test
    public fun testLoadFavourites() {
        val courses = Courses.getValidCourses(5, true).data
        for (course in courses) {
            coursesRepository.addToFavourites(course).blockingFirst()
        }
        val favouritesObserver = coursesRepository.loadFavourites().test()
        favouritesObserver.assertValue {
            it.data.size == 5
        }
    }

    @Test
    public fun testAddToFavourites() {
        val course = Courses.getCourse(true)
        coursesRepository.addToFavourites(course).blockingFirst()
        coursesRepository.loadFavourites().test().assertValue { it.data.size == 1 }
    }

    @Test
    public fun testRemoveFromFavourites() {
        val course = Courses.getCourse(true)
        coursesRepository.addToFavourites(course).blockingFirst()
        coursesRepository.loadFavourites().test().assertValue { it.data.size == 1 }
        coursesRepository.removeFromFavourites(course).blockingFirst()
        coursesRepository.loadFavourites().test().assertValue { it.data.size == 0 }
    }

    @Test
    public fun tesSearch() {
        val query = "python"
        val page = 0
        val favouritesObserver = coursesRepository.search(query, page).test()
        favouritesObserver.assertValue {
            it.data.size == results.data.size
        }
    }


    @Test
    public fun testCourseCahngesSubscription() {
        val course = Courses.getCourse(true)
        val observer = coursesRepository.subscribeToCourseChanges().test()
        val favouritesObserver = coursesRepository.addToFavourites(course).test()
        observer.assertValue(course)
    }
}