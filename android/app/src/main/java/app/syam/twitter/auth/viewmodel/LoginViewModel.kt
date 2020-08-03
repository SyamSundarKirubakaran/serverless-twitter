package app.syam.twitter.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.syam.twitter.auth.model.AuthBody
import app.syam.twitter.auth.network.AuthService
import app.syam.twitter.common.model.StoredUser
import app.syam.twitter.common.storage.SharedPreferenceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val context = getApplication<Application>().applicationContext
    val createUserLiveData: MutableLiveData<Unit> = MutableLiveData()

    fun createUser(user: StoredUser) {
        val token = SharedPreferenceManager.getLoggedInUser(context)?.token.orEmpty()
        compositeDisposable.add(
            AuthService.Creator.service.createUser(
                token = "Bearer $token",
                body = AuthBody(
                    name = user.name,
                    email = user.email,
                    imageUrl = user.imageUrl,
                    isVerified = user.isVerified
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    createUserLiveData.postValue(Unit)
                }, {
                    createUserLiveData.postValue(Unit)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}