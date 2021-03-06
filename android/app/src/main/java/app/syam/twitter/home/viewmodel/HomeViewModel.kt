package app.syam.twitter.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.model.CreateTweet
import app.syam.twitter.home.state.HomeCallState
import app.syam.twitter.home.state.GeneralCallState
import app.syam.twitter.tweet.model.LightWeightUser
import app.syam.twitter.tweet.model.UpdateTweet
import app.syam.twitter.tweet.network.TweetService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val context = getApplication<Application>().applicationContext
    val tweetListLiveData: MutableLiveData<HomeCallState> = MutableLiveData()
    val generalLiveData: MutableLiveData<GeneralCallState> = MutableLiveData()

    fun tweets() {
        val token = SharedPreferenceManager.getLoggedInUser(context)?.token.orEmpty()
        compositeDisposable.add(
            TweetService.Creator.service.getTweets(token = "Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { HomeCallState.Success(it) as HomeCallState }
                .startWith(HomeCallState.InFlight as HomeCallState)
                .onErrorReturn { HomeCallState.Failed }
                .subscribe { tweetListLiveData.postValue(it) }
        )
    }

    fun likeTweet(tweetId: String, likeList: List<LightWeightUser>) {
        val token = SharedPreferenceManager.getLoggedInUser(context)?.token.orEmpty()
        compositeDisposable.add(
            TweetService.Creator.service.updateTweet(
                token = "Bearer $token",
                tweetId = tweetId,
                body = UpdateTweet(likeList)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { GeneralCallState.Success as GeneralCallState }
                .startWith(GeneralCallState.InFlight as GeneralCallState)
                .onErrorReturn { GeneralCallState.Failed }
                .subscribe { generalLiveData.postValue(it) }
        )
    }

    fun createTweet(newTweet: CreateTweet) {
        val token = SharedPreferenceManager.getLoggedInUser(context)?.token.orEmpty()
        compositeDisposable.add(
            TweetService.Creator.service.createTweet(
                token = "Bearer $token",
                body = newTweet
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { GeneralCallState.Success as GeneralCallState }
                .startWith(GeneralCallState.InFlight as GeneralCallState)
                .onErrorReturn { GeneralCallState.Failed }
                .subscribe { generalLiveData.postValue(it) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}