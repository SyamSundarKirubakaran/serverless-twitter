package app.syam.twitter.profile.network

import app.syam.twitter.BuildConfig
import app.syam.twitter.home.model.TweetResult
import app.syam.twitter.profile.model.ProfileResult
import app.syam.twitter.profile.model.SignedUrl
import app.syam.twitter.profile.model.UpdateUser
import app.syam.twitter.util.NetworkUtil
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ProfileService {

    @Headers("Accept: application/json", "content-type: application/json")
    @GET("tweet/specific/{authType}/{authId}")
    fun getTweets(
        @Path("authType") authType: String,
        @Path("authId") authId: String,
        @Header("Authorization") token: String
    ): Observable<TweetResult>

    @Headers("Accept: application/json", "content-type: application/json")
    @GET("user/specific/{authType}/{authId}")
    fun getUser(
        @Path("authType") authType: String,
        @Path("authId") authId: String,
        @Header("Authorization") token: String
    ): Observable<ProfileResult>

    @Headers("Accept: application/json", "content-type: application/json")
    @PATCH("user")
    fun updateUser(
        @Header("Authorization") token: String,
        @Body updateUser: UpdateUser
    ): Observable<Boolean>

    @Headers("Accept: application/json", "content-type: application/json")
    @GET("tweet/signedUrl/{tweetId}")
    fun getUploadUrl(
        @Path("tweetId") tweetId: String,
        @Header("Authorization") token: String
    ): Observable<SignedUrl>

    object Creator{
        private val placeHolderUrl: String
            get() = "https://${BuildConfig.APP_ID}.execute-api.us-east-1.amazonaws.com/dev/"
        val service: ProfileService
            get() {
                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create()
                val retrofit = Retrofit.Builder()
                    .baseUrl(placeHolderUrl)
                    .client(NetworkUtil.createClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                return retrofit.create(ProfileService::class.java)
            }
    }

}