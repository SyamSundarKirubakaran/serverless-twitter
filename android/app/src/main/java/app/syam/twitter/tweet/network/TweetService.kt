package app.syam.twitter.tweet.network

import app.syam.twitter.home.model.HomeResult
import app.syam.twitter.tweet.model.Tweet
import app.syam.twitter.util.NetworkUtil
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TweetService {

    @Headers("Accept: application/json", "content-type: application/json")
    @GET("tweet")
    fun getTweets(
        @Header("Authorization") token: String
    ): Observable<HomeResult>

    object Creator{
        private val placeHolderUrl: String
            get() = "https://xdx4zzvrmg.execute-api.us-east-1.amazonaws.com/dev/"
        val service: TweetService
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
                return retrofit.create(TweetService::class.java)
            }
    }

}