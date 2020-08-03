package app.syam.twitter.profile.model

import android.os.Parcelable
import app.syam.twitter.tweet.model.Tweet
import app.syam.twitter.tweet.model.User
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileResult(
    @SerializedName("result")
    val result: User?
): Parcelable