package io.somedomain.stepiktestapp

import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import io.reactivex.Observable
import io.somedomain.stepiktestapp.api.StepikApiController
import io.somedomain.stepiktestapp.data.CoursesRemoteDataSource
import io.somedomain.stepiktestapp.data.CoursesRemoteDataSourceDefault
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.data.model.PageResponse
import io.somedomain.stepiktestapp.util.Courses
import io.somedomain.stepiktestapp.util.RxSchedulersHooks
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
public class TestRemoteDataSource {

    private lateinit var courseRemoteDataSource: CoursesRemoteDataSource
    val results = Courses.getValidCourses(5)

    @Before
    public fun setup() {
        RxSchedulersHooks.setUp()
        courseRemoteDataSource = CoursesRemoteDataSourceDefault(object : StepikApiController {
            override fun searchCourses(query: String, page: Int): Observable<PageResponse<MutableList<Course>>> {
                return Observable.just(results)
            }
        })
    }

    @Test
    public fun testLoadFavourites() {
        val favouritesObserver = courseRemoteDataSource.search("python", 1).test()
        favouritesObserver.assertValue(results)
    }
}