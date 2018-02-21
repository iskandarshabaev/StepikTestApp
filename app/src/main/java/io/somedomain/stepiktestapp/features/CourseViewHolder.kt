package io.somedomain.stepiktestapp.features

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import io.somedomain.stepiktestapp.R
import io.somedomain.stepiktestapp.base.ViewHolder
import io.somedomain.stepiktestapp.common.ImageHelper
import io.somedomain.stepiktestapp.common.tintedVector
import io.somedomain.stepiktestapp.model.Course

class CourseViewHolder(
        private val view: View
) : ViewHolder<Course>(view) {

    var callbacks: CourseItemCallbacks? = null
    private val favouriteActiveDrawable = view.tintedVector(R.drawable.ic_favorite_black_24dp, R.color.colorAccent)
    private val favouritePassiveDrawable = view.tintedVector(R.drawable.ic_favorite_black_24dp, R.color.colorHintText)

    private val imageView = view.findViewById<ImageView>(R.id.image)
    private val titleView = view.findViewById<TextView>(R.id.title)
    private val favouriteView = view.findViewById<ImageView>(R.id.favourite)
    private val scoreView = view.findViewById<TextView>(R.id.score)

    init {
        favouriteView.setOnClickListener {
            (favouriteView.tag as? Course)?.let {
                it.isFavourite = !it.isFavourite
                if (it.isFavourite) {
                    callbacks?.onAddCourseToFavourites(it)
                } else {
                    callbacks?.onRemoveCourseFromFavourites(it)
                }
            }
        }
    }

    override fun bind(position: Int, data: Course) {
        ImageHelper.picasso.load(data.courseCover).into(imageView)
        titleView.text = data.courseTitle
        scoreView.text = view.resources.getString(R.string.score, data.score)
        if (data.isFavourite) {
            favouriteView.setImageDrawable(favouriteActiveDrawable)
        } else {
            favouriteView.setImageDrawable(favouritePassiveDrawable)
        }
        favouriteView.tag = data
    }

    interface CourseItemCallbacks {
        fun onAddCourseToFavourites(course: Course)
        fun onRemoveCourseFromFavourites(course: Course)
    }
}
