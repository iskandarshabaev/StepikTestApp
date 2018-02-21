package io.somedomain.stepiktestapp.base

import io.somedomain.stepiktestapp.model.PageResponse

interface BaseListView<T> : BaseView {
    fun showContent(data: PageResponse<MutableList<T>>)
    fun showError(e: Throwable)
}