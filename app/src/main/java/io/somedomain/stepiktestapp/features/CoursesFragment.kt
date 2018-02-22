package io.somedomain.stepiktestapp.features

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.*
import io.somedomain.stepiktestapp.R
import io.somedomain.stepiktestapp.base.ListAdapter
import io.somedomain.stepiktestapp.base.ListFragment
import io.somedomain.stepiktestapp.base.ViewHolder
import io.somedomain.stepiktestapp.common.redSnackbar
import io.somedomain.stepiktestapp.data.model.Course
import io.somedomain.stepiktestapp.data.model.PageResponse
import io.somedomain.stepiktestapp.di.Injection
import io.somedomain.stepiktestapp.features.search.CoursesSearchActivity
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
        const val queryKey = "query"
        public const val typeFavourites = 1
        public const val typeSearch = 2

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

    protected var currentQuerry = ""

    override fun onCreatePresenter(savedInstanceState: Bundle?): CoursesPresenter {
        return CoursesPresenter(type, Injection.coursesRepository(context))
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
        savedInstanceState?.getString(queryKey)?.let {
            currentQuerry = it
        }
    }

    override fun initViews() {
        super.initViews()
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        if (type == typeFavourites) {
            emptyViewText.text = getString(R.string.empty_favourites)
        } else if (type == typeSearch) {
            emptyViewText.text = getString(R.string.empty_search)
        }
    }

    override fun onStart() {
        super.onStart()
        if (type == 1 && isFirstRun) {
            isFirstRun = false
            presenter.loadFavourites()
        }
    }

    override fun showContent(data: PageResponse<MutableList<Course>>) {
        if (data.meta.page > 1) {
            adapter.clear()
            adapter.update(data)
        } else {
            adapter.clear()
            adapter.update(data)
        }
        showEmptyView(adapter.itemCount > 0)
    }

    override fun onLoadingMore(page: Int) {
        presenter.searchCourses(currentQuerry, page)
    }

    override fun showError(e: Throwable) {
        redSnackbar(R.string.something_went_wrong)
    }

    override fun onAddCourseToFavourites(course: Course) {
        presenter.addCourseToFavourites(course)
    }

    override fun onRemoveCourseFromFavourites(course: Course) {
        presenter.removeCourseFromFavourites(course)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            startActivityForResult(Intent(activity, CoursesSearchActivity::class.java), 89)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (type == typeFavourites) {
            inflater.inflate(R.menu.menu_favourites, menu)
        } else {
            inflater.inflate(R.menu.menu_search, menu)
            val menuItem = menu.findItem(R.id.action_search)
            val rxSearchView = MenuItemCompat.getActionView(menuItem) as RxSearchView
            if (!TextUtils.isEmpty(currentQuerry)) {
                menuItem.expandActionView()
                rxSearchView.setQuery(currentQuerry, true)
                rxSearchView.clearFocus()
            }
            menuItem.expandActionView()
            MenuItemCompat.setOnActionExpandListener(menuItem, OnActionExpandListenerImpl())
            rxSearchView.setOnRxQueryTextListener { newText ->
                currentQuerry = newText
                presenter.searchCourses(newText, 1)
            }
        }
    }

    override fun onItemChanged(course: Course) {
        val foundCourse = adapter.getDataList().firstOrNull { it.course == course.course }
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
        if (adapter.itemCount == 0) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(queryKey, currentQuerry)
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