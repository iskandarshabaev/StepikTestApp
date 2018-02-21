package io.somedomain.stepiktestapp.repository

import android.util.Log
import io.somedomain.stepiktestapp.base.BasePresenter
import io.somedomain.stepiktestapp.base.BaseView

class PresenterRepository<V : BaseView, P : BasePresenter<V>> {
    private val map = HashMap<String, P>()

    fun peek(tag: String): P? = map[tag]
    fun add(tag: String, presenter: P) {
        map[tag] = presenter
    }

    fun destroy(tag: String) {
        val removed = map.remove(tag)
        if (removed == null) Log.e("PresenterRepository", "attempt to remove null presenter", Exception())
        else removed.onDestroy()
    }
}