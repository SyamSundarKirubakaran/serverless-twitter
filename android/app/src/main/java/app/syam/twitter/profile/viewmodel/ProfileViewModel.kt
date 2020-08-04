package app.syam.twitter.profile.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.model.TweetResult
import app.syam.twitter.home.state.GeneralCallState
import app.syam.twitter.profile.io.ImageRequestBody
import app.syam.twitter.profile.model.ProfileResult
import app.syam.twitter.profile.model.SignedUrl
import app.syam.twitter.profile.model.UpdateUser
import app.syam.twitter.profile.network.CustomService
import app.syam.twitter.profile.network.ProfileService
import app.syam.twitter.profile.state.ProfileState
import app.syam.twitter.profile.state.SignedUrlState
import app.syam.twitter.tweet.model.LightWeightUser
import app.syam.twitter.tweet.model.UpdateTweet
import app.syam.twitter.tweet.network.TweetService
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val context = getApplication<Application>().applicationContext
    val profileLiveData: MutableLiveData<ProfileState> = MutableLiveData()
    val updateLiveData: MutableLiveData<GeneralCallState> = MutableLiveData()
    val signedUrlLiveData: MutableLiveData<SignedUrlState> = MutableLiveData()
    val uploadLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var storageDir: File? = null
    var currentPhotoPath: String = ""

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
                .map { GeneralCallState.Success as GeneralCallState }
                .startWith(GeneralCallState.InFlight as GeneralCallState)
                .onErrorReturn { GeneralCallState.Failed }
                .subscribe { updateLiveData.postValue(it) }
        )
    }

    fun updateUser(userId: String, updateUser: UpdateUser, lightWeightUser: LightWeightUser) {
        val (authType, authId) = userId.split('|')
        val token = SharedPreferenceManager.getLoggedInUser(context)?.token.orEmpty()
        compositeDisposable.add(
            ProfileService.Creator.service.getUser(
                token = "Bearer $token",
                authType = authType,
                authId = authId
            )
                .subscribeOn(Schedulers.io())
                .map {
                    val tempList = it.result?.followingList as MutableList<LightWeightUser>
                    tempList.add(lightWeightUser)
                    val tempUpdateUser = updateUser.copy(
                        followingList = tempList
                    )
                    tempUpdateUser
                }
                .flatMap {
                    Observable.zip(
                        Observable.just(true),
                        ProfileService.Creator.service.updateUser(
                            token = "Bearer $token",
                            updateUser = it
                        ),
                        BiFunction { t1: Boolean, t2: Boolean -> t1 to t2 })
                }
                .observeOn(AndroidSchedulers.mainThread())
                .map { GeneralCallState.Success as GeneralCallState }
                .startWith(GeneralCallState.InFlight as GeneralCallState)
                .onErrorReturn { GeneralCallState.Failed }
                .subscribe { updateLiveData.postValue(it) }
        )
    }

    fun reverseUpdateUser(userId: String, updateUser: UpdateUser, receivedUserId: String) {
        val (authType, authId) = userId.split('|')
        val token = SharedPreferenceManager.getLoggedInUser(context)?.token.orEmpty()
        compositeDisposable.add(
            ProfileService.Creator.service.getUser(
                token = "Bearer $token",
                authType = authType,
                authId = authId
            )
                .subscribeOn(Schedulers.io())
                .map {
                    val tempList = it.result?.followingList as MutableList<LightWeightUser>
                    tempList.remove(tempList.find { it.userId == receivedUserId })
                    val tempUpdateUser = updateUser.copy(
                        followingList = tempList
                    )
                    tempUpdateUser
                }
                .flatMap {
                    Observable.zip(
                        Observable.just(true),
                        ProfileService.Creator.service.updateUser(
                            token = "Bearer $token",
                            updateUser = it
                        ),
                        BiFunction { t1: Boolean, t2: Boolean -> t1 to t2 })
                }
                .observeOn(AndroidSchedulers.mainThread())
                .map { GeneralCallState.Success as GeneralCallState }
                .startWith(GeneralCallState.InFlight as GeneralCallState)
                .onErrorReturn { GeneralCallState.Failed }
                .subscribe { updateLiveData.postValue(it) }
        )
    }

    fun getUploadImageUrl(tweetId: String) {
        val token = SharedPreferenceManager.getLoggedInUser(context)?.token.orEmpty()
        compositeDisposable.add(
            ProfileService.Creator.service.getUploadUrl(
                token = "Bearer $token",
                tweetId = tweetId
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { SignedUrlState.Success(it) as SignedUrlState }
                .startWith(SignedUrlState.InFlight as SignedUrlState)
                .onErrorReturn { SignedUrlState.Failed }
                .subscribe { signedUrlLiveData.postValue(it) }
        )
    }

    fun createImageDir(cache: File) {
        compositeDisposable.add(
            Observable.just(true)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    val mainImageDir = File(cache, "img_collection")
                    if (!mainImageDir.exists())
                        mainImageDir.mkdir()
                    storageDir = mainImageDir
                }
        )
    }

    fun createImageFile(): File {
        val random = Math.random() * 10000000
        val image = File.createTempFile(
            "${random.toInt()}",
            ".jpg",
            storageDir
        )
        currentPhotoPath = image.absolutePath
        return image
    }

    fun processLocalImage(contentResolver: ContentResolver?, token: String, uri: Uri?) {
        val file = File(currentPhotoPath)
        val bitmap = MediaStore.Images.Media
            .getBitmap(contentResolver, uri)
        if (bitmap != null) {
            try {
                compositeDisposable.add(
                    Observable.just(true)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val stream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                            val mediaByteArray = stream.toByteArray()
                            val fos = FileOutputStream(file)
                            fos.write(mediaByteArray)
                            fos.flush()
                            fos.close()
                            val body = MultipartBody.Part.createFormData(
                                "file",
                                ".jpg",
                                ImageRequestBody(file)
                            )
                            upload(
                                data = body
                            )
                        }
                )
            } catch (e: IOException) {
            }
        }
    }

    private fun upload(data: MultipartBody.Part) {

        val url = (signedUrlLiveData.value as SignedUrlState.Success).result.url

        val mainContext = url?.substring(44).orEmpty()
        val strCollection = mainContext.split("?")
        val queryList = strCollection[1].split("&")

        compositeDisposable.add(
            CustomService.Creator.service.uploadImage(
                endPoint = strCollection[0],
                photo = data,
                algorithm = queryList[0].substring(16),
                cred = queryList[1].substring(17),
                date = queryList[2].substring(11),
                expire = queryList[3].substring(14),
                security = queryList[4].substring(21),
                sign = queryList[5].substring(16),
                signHeader = queryList[6].substring(20)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    uploadLiveData.postValue(true)
                }, {
                    uploadLiveData.postValue(false)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}