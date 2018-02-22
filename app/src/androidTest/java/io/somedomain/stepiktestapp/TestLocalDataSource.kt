package io.somedomain.stepiktestapp

import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import io.somedomain.stepiktestapp.data.CoursesLocalDataSource
import io.somedomain.stepiktestapp.data.CoursesLocalDataSourceDefault
import io.somedomain.stepiktestapp.util.Courses
import io.somedomain.stepiktestapp.util.RxSchedulersHooks
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
public class TestLocalDataSource {

    private lateinit var coursesLocalDataSource: CoursesLocalDataSource

    @Before
    public fun setup() {
        RxSchedulersHooks.setUp()
        coursesLocalDataSource = CoursesLocalDataSourceDefault()
        coursesLocalDataSource.clear().blockingFirst()
    }

    @Test
    public fun testLoadFavourites() {
        val favouritesObserver = coursesLocalDataSource.loadFavourites().test()
        favouritesObserver.assertValue { it.data.size == 0 }
    }

    @Test
    public fun testAddToFavourites() {
        val course = Courses.getCourse(true)
        coursesLocalDataSource.addToFavourites(course).blockingFirst()
        val favouritesObserver = coursesLocalDataSource.loadFavourites().test()
        favouritesObserver.assertValue {
            Log.e("lol", "" + it.data.size)
            it.data.size == 1
        }
    }

    @Test
    public fun testRemoveFromFavourites() {
        val course = Courses.getCourse(false)
        coursesLocalDataSource.removeFromFavourites(course).blockingFirst()
        val favouritesObserver = coursesLocalDataSource.loadFavourites().test()
        favouritesObserver.assertValue { it.data.size == 0 }
    }
}