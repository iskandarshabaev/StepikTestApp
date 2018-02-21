package io.somedomain.stepiktestapp.features

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.text.TextUtils
import android.view.*
import io.somedomain.stepiktestapp.R
import io.somedomain.stepiktestapp.base.ListAdapter
import io.somedomain.stepiktestapp.base.ListFragment
import io.somedomain.stepiktestapp.base.ViewHolder
import io.somedomain.stepiktestapp.common.repositoryProvider
import io.somedomain.stepiktestapp.features.search.CoursesSearchActivity
import io.somedomain.stepiktestapp.model.Course
import io.somedomain.stepiktestapp.model.PageResponse
import io.somedomain.stepiktestapp.widget.RxSearchView


class CoursesFragment : ListFragment<CoursesPresenter.View, CoursesPresenter, Course>,
        CoursesPresenter.View, CourseViewHolder.CourseItemCallbacks {

    @Deprecated(message = "Only Bundle constructor", level = DeprecationLevel.ERROR)
    constructor()

    @SuppressLint("ValidFragment")
    private constructor(args: Bundle) {
        arguments = args
    }

    companion object {

        const val typeKey = "type"
        const val typeFavourites = 1
        const val typeSearch = 2

        public fun favourites(): CoursesFragment {
            return CoursesFragment(Bundle().also {
                it.putInt(typeKey, typeFavourites)
            })
        }

        public fun search(): CoursesFragment {
            return CoursesFragment(Bundle().also {
                it.putInt(typeKey, typeSearch)
            })
        }
    }

    private val type: Int get() = arguments.getInt(typeKey)

    override val fragmentTag: String
        get() = "CoursesFragment:$type"

    override val title: String
        get() = ""

    override fun onCreatePresenter(savedInstanceState: Bundle?): CoursesPresenter {
        return CoursesPresenter(repositoryProvider.coursesRepository)
    }

    override fun onCreateListAdapter(): ListAdapter<Course> {
        return object : ListAdapter<Course>(mutableListOf()) {
            override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewHolder<Course> {
                val view = inflater.inflate(R.layout.item_course, parent, false)
                return CourseViewHolder(view).also {
                    it.callbacks = this@CoursesFragment
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        if (type == 1 && isFirstRun) {
            presenter.loadFavourites()
        }
    }

    override fun showContent(data: PageResponse<List<Course>>) {
        if (type == typeFavourites) {
            adapter.update(data)
            showEmptyView(adapter.itemCount > 0)
        } else {
            adapter.clear()
            adapter.update(data)
        }
    }

    override fun showError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onAddCourseToFavourites(course: Course) {
        presenter.addCourseToFavourites(course)
    }

    override fun onRemoveCourseFromFavourites(course: Course) {
        presenter.removeCourseFromFavourites(course)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            startActivity(Intent(activity, CoursesSearchActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (type == typeFavourites) {
            inflater.inflate(R.menu.menu_favourites, menu)
        } else {
            inflater.inflate(R.menu.menu_search, menu)
            val menuitem = menu.findItem(R.id.action_search)
            val rxSearchView = MenuItemCompat.getActionView(menuitem) as RxSearchView
            if (!TextUtils.isEmpty("")) {
                menuitem.expandActionView()
                rxSearchView.setQuery("", true)
                rxSearchView.clearFocus()
            }

            menuitem.expandActionView()
            MenuItemCompat.setOnActionExpandListener(menuitem, OnActionExpandListenerImpl())

            rxSearchView.setOnRxQueryTextListener { newText ->
                presenter.searchCourses(newText)
            }
        }
    }

    override fun onItemChanged(course: Course) {
        val foundCourse = adapter.getDataList().firstOrNull { it.id == course.id }
        if (type == typeFavourites) {
            if (foundCourse != null) {
                foundCourse.isFavourite = course.isFavourite
                if (!foundCourse.isFavourite) adapter.remove(foundCourse)
            } else {
                adapter.getDataList().add(course)
                adapter.notifyDataSetChanged()
            }
        } else {
            if (foundCourse != null) {
                foundCourse.isFavourite = course.isFavourite
                adapter.notifyItemChanged(foundCourse)
            }
        }
    }

    private inner class OnActionExpandListenerImpl : MenuItemCompat.OnActionExpandListener {

        override fun onMenuItemActionExpand(item: MenuItem): Boolean {
            return false
        }

        override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
            activity.onBackPressed()
            return true
        }
    }

}