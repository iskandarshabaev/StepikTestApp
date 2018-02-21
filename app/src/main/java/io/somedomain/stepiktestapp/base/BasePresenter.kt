package io.somedomain.stepiktestapp.base

import java.lang.ref.WeakReference

public interface BasePresenter<V : BaseView> {
    fun attachView(v: V)
    fun detachView()
    fun onDestroy()
}

open class BaseDefaultPresenter<V : BaseView> : BasePresenter<V> {

    var view: WeakReference<V>? = null

    override fun attachView(v: V) {
        view = WeakReference(v)
    }

    override fun detachView() {
        view?.clear()
        view = null
    }

    override fun onDestroy() {
    }

    protected fun view(): V? {
        return view?.get()
    }
}