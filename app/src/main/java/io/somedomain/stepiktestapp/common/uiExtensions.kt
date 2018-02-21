package io.somedomain.stepiktestapp.common

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import io.somedomain.stepiktestapp.StepikTestApp
import io.somedomain.stepiktestapp.repository.RepositoryProvider

// from Moxy
// https://github.com/Arello-Mobile/Moxy/blob/master/moxy-android/src/main/java/com/arellomobile/mvp/MvpFragment.java#L72
val Fragment.isDisappearing: Boolean
    get() {
        if (activity.isFinishing)
            return true

        var anyParentIsRemoving = false
        if (Build.VERSION.SDK_INT >= 17) {
            var parent: Fragment? = parentFragment
            while (!anyParentIsRemoving && parent != null) {
                anyParentIsRemoving = parent.isRemoving
                parent = parent.parentFragment
            }
        }
        return isRemoving || anyParentIsRemoving
    }

val Context.app: StepikTestApp
    get() {
        return applicationContext as StepikTestApp
    }

val Fragment.app: StepikTestApp
    get() {
        return activity.applicationContext as StepikTestApp
    }

val Context.repositoryProvider: RepositoryProvider
    get() {
        return app.repositoryProvider
    }

val Fragment.repositoryProvider: RepositoryProvider
    get() {
        return app.repositoryProvider
    }

fun Fragment.colour(@ColorRes id: Int) = ContextCompat.getColor(activity, id)
fun Context.colour(@ColorRes id: Int) = ContextCompat.getColor(this, id)
fun View.colour(@ColorRes id: Int) = ContextCompat.getColor(context, id)

fun Fragment.loadDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(activity, id)
fun Context.loadDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)
fun View.loadDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(context, id)

@Suppress("NOTHING_TO_INLINE")
inline fun Context.vector(@DrawableRes vectorRes: Int) =
        VectorDrawableCompat.create(resources, vectorRes, null)!!

@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.vector(@DrawableRes vectorRes: Int) =
        VectorDrawableCompat.create(resources, vectorRes, null)!!

@Suppress("NOTHING_TO_INLINE")
inline fun Resources.vector(@DrawableRes vectorRes: Int) =
        VectorDrawableCompat.create(this, vectorRes, null)!!

fun Context.tintedVector(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable {
    return DrawableCompat.wrap(resources.vector(drawableId).mutate()).also {
        DrawableCompat.setTint(it, colour(colorId))
    }
}

fun Fragment.tintedVector(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable {
    return context.tintedVector(drawableId, colorId)
}

fun View.tintedVector(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable {
    return context.tintedVector(drawableId, colorId)
}