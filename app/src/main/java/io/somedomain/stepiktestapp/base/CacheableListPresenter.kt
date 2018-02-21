package io.somedomain.stepiktestapp.base

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.somedomain.stepiktestapp.common.applySchedulers
import io.somedomain.stepiktestapp.model.PageResponse

open class CacheableListPresenter<T, V : BaseListView<T>> : BaseListPresenter<T, V>() {

    open protected var cacheSubject: BehaviorSubject<PageResponse<List<T>>> = BehaviorSubject.create()

    override fun attachView(v: V) {
        super.attachView(v)
        subscribeToSubject()
    }

    open protected fun  subscribeToSubject() {
        cacheSubject.applySchedulers()
                .subscribe(this::onData, this::onError)
    }

    open protected fun Observable<PageResponse<List<T>>>.mergeWithCache(): Observable<PageResponse<List<T>>> =
            flatMap { result ->
                if (cacheSubject.value != null) {
                    val page = cacheSubject.value
                    val data = page.data.toMutableList()
                    data.addAll(result.data)
                    Observable.just(PageResponse<List<T>>(result.meta, data))
                } else {
                    Observable.just(result)
                }
            }

    open protected fun Observable<PageResponse<List<T>>>.deliverToSubject(): Disposable =
            subscribe({ cacheSubject.onNext(it) }, { cacheSubject.onError(it) })
}