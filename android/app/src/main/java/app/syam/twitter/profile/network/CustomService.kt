package app.syam.twitter.profile.network

import app.syam.twitter.util.NetworkUtil
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CustomService {

    @Multipart
    @Headers("Accept: */*", "Access-Control-Allow-Origin: *")
    @PUT("{endPoint}")
    fun uploadImage(
        @Path("endPoint") endPoint: String,
        @Part photo: MultipartBody.Part,
        @Query("X-Amz-Algorithm") algorithm: String,
        @Query("X-Amz-Credential") cred: String,
        @Query("X-Amz-Date") date: String,
        @Query("X-Amz-Expires") expire: String,
        @Query("X-Amz-Security-Token") security: String,
        @Query("X-Amz-Signature") sign: String,
        @Query("X-Amz-SignedHeaders") signHeader: String
    ): Observable<Void>

    object Creator {
        private val placeHolderUrl: String
            get() = "https://twitter-images-dev.s3.amazonaws.com/"
        val service: CustomService
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
                return retrofit.create(CustomService::class.java)
            }
    }

}