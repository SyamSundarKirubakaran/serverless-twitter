package app.syam.twitter.auth.network

import app.syam.twitter.auth.model.AuthBody
import app.syam.twitter.home.model.TweetResult
import app.syam.twitter.util.NetworkUtil
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AuthService {

    @Headers("Accept: application/json", "content-type: application/json")
    @POST("user")
    fun createUser(
        @Header("Authorization") token: String,
        @Body body: AuthBody
    ): Observable<TweetResult>

    object Creator{
        private val placeHolderUrl: String
            get() = "https://xdx4zzvrmg.execute-api.us-east-1.amazonaws.com/dev/"
        val service: AuthService
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
                return retrofit.create(AuthService::class.java)
            }
    }

}