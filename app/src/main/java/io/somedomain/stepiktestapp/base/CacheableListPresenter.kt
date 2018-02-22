package io.somedomain.stepiktestapp.base

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.somedomain.stepiktestapp.common.applySchedulers
import io.somedomain.stepiktestapp.data.model.PageResponse

open class CacheableListPresenter<T, V : BaseListView<T>> : BaseListPresenter<T, V>() {

    open protected var cacheSubject: BehaviorSubject<PageResponse<MutableList<T>>> = BehaviorSubject.create()

    override fun attachView(v: V) {
        super.attachView(v)
        subscribeToSubject()
    }

    open protected fun subscribeToSubject() {
        cacheSubject.applySchedulers()
                .subscribe(this::onData, this::onError)
    }

    open protected fun Observable<PageResponse<MutableList<T>>>.mergeWithCache(): Observable<PageResponse<MutableList<T>>> =
            flatMap { result ->
                if (cacheSubject.value != null) {
                    val page = cacheSubject.value
                    val data = page.data
                    data.addAll(result.data)
                    Observable.just(PageResponse(result.meta, data))
                } else {
                    Observable.just(result)
                }
            }

    open protected fun Observable<PageResponse<MutableList<T>>>.deliverToSubject(): Disposable =
            subscribe({ cacheSubject.onNext(it) }, { view()?.showError(it) })
}