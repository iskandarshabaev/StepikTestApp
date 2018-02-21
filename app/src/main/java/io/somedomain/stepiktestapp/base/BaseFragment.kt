package io.somedomain.stepiktestapp.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.somedomain.stepiktestapp.common.app
import io.somedomain.stepiktestapp.common.isDisappearing

/**
 * A Fragment with a presenter.
 */
abstract class BaseFragment<V : BaseView, out P : BasePresenter<V>> internal constructor() : Fragment() {

    private var _presenter: P? = null
    protected val presenter get() = _presenter!!
    protected var isFirstRun: Boolean = true

    protected abstract val fragmentTag: String
    protected abstract val title: String

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirstRun = savedInstanceState == null
        _presenter = (app.presenterRepository.peek(fragmentTag) as P?)
                ?: onCreatePresenter(savedInstanceState).also {
            app.presenterRepository.add(fragmentTag, it as BasePresenter<BaseView>)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = createView(inflater, container, savedInstanceState)
        findViews(view)
        initViews()
        return view
    }

    open abstract protected fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    open protected fun findViews(root: View) {
    }

    open protected fun initViews() {
    }

    @CallSuper
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _presenter!!.attachView(this as V)
    }

    @CallSuper
    override fun onDestroyView() {
        _presenter!!.detachView()
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        if (isDisappearing) app.presenterRepository.destroy(fragmentTag)
        super.onDestroy()
    }

    abstract fun onCreatePresenter(savedInstanceState: Bundle?): P

}