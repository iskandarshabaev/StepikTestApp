package io.somedomain.stepiktestapp.base

import io.somedomain.stepiktestapp.data.model.PageResponse

abstract class BaseListPresenter<T, V : BaseListView<T>> : BaseDefaultPresenter<V>() {

    fun onData(data: PageResponse<MutableList<T>>) {
        view()?.showContent(data)
    }

    fun onError(t: Throwable) {
        view()?.showError(t)
    }
}