package io.somedomain.stepiktestapp

import android.app.Application
import com.google.gson.Gson
import io.somedomain.stepiktestapp.api.OkHttpProvider
import io.somedomain.stepiktestapp.api.StepikApi
import io.somedomain.stepiktestapp.base.BasePresenter
import io.somedomain.stepiktestapp.base.BaseView
import io.somedomain.stepiktestapp.common.ImageHelper
import io.somedomain.stepiktestapp.repository.PresenterRepository
import io.somedomain.stepiktestapp.repository.RepositoryProvider
import io.somedomain.stepiktestapp.repository.db.LocalDB

class StepikTestApp : Application() {

    val presenterRepository = PresenterRepository<BaseView, BasePresenter<BaseView>>()

    private var _stepikApi: StepikApi? = null
    val stepikApi: StepikApi
        get() {
            if (_stepikApi == null) {
                _stepikApi = StepikApi(BuildConfig.API_ENDPOINT, OkHttpProvider.provideClient(), Gson())
            }
            return _stepikApi!!
        }

    private var _repositoryProvider: RepositoryProvider? = null
    val repositoryProvider: RepositoryProvider
        get() {
            if (_repositoryProvider == null) {
                _repositoryProvider = RepositoryProvider(stepikApi)
            }
            return _repositoryProvider!!
        }

    override fun onCreate() {
        super.onCreate()
        LocalDB.initDB(this)
        ImageHelper.init(this)
    }
}
