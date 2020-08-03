package app.syam.twitter.home.model

import android.os.Parcelable
import app.syam.twitter.tweet.model.Tweet
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TweetResult(
    @SerializedName("result")
    val result: List<Tweet>?
): Parcelable