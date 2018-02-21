package io.somedomain.stepiktestapp.base

import io.somedomain.stepiktestapp.model.PageResponse

abstract class BaseListPresenter<T, V : BaseListView<T>> : BaseDefaultPresenter<V>() {

    fun onData(data: PageResponse<List<T>>) {
        view()?.showContent(data)
    }

    fun onError(t: Throwable) {
        view()?.showError(t)
    }
}