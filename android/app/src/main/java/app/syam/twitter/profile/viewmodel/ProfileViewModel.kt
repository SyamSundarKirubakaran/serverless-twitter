package app.syam.twitter.profile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.model.TweetResult
import app.syam.twitter.home.state.HomeCallState
import app.syam.twitter.home.state.LikeCallState
import app.syam.twitter.profile.model.ProfileResult
import app.syam.twitter.profile.network.ProfileService
import app.syam.twitter.profile.state.ProfileState
import app.syam.twitter.tweet.model.LightWeightUser
import app.syam.twitter.tweet.model.UpdateTweet
import app.syam.twitter.tweet.network.TweetService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val context = getApplication<Application>().applicationContext
    val profileLiveData: MutableLiveData<ProfileState> = MutableLiveData()
    val profileTweetLikeLiveData: MutableLiveData<LikeCallState> = MutableLiveData()

    fun initProfileLoad(userId: String) {
        val (authType, authId) = userId.split('|')
        val token = SharedPreferenceManager.getLoggedInUser(context)?.token.orEmpty()
        compositeDisposable.add(
            ProfileService.Creator.service.getTweets(
                token = "Bearer $token",
                authType = authType,
                authId = authId
            )
                .subscribeOn(Schedulers.io())
                .flatMap {
                    Observable.zip(
                        Observable.just(it),
                        ProfileService.Creator.service.getUser(
                            token = "Bearer $token",
                            authType = authType,
                            authId = authId
                        ),
                        BiFunction { t1: TweetResult, t2: ProfileResult -> Pair(t1, t2) })
                }
                .observeOn(AndroidSchedulers.mainThread())
                .map { ProfileState.Success(it) as ProfileState }
                .startWith(ProfileState.InFlight as ProfileState)
                .onErrorReturn { ProfileState.Failed }
                .subscribe { profileLiveData.postValue(it) }
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
                .map { LikeCallState.Success as LikeCallState }
                .startWith(LikeCallState.InFlight as LikeCallState)
                .onErrorReturn { LikeCallState.Failed }
                .subscribe { profileTweetLikeLiveData.postValue(it) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}