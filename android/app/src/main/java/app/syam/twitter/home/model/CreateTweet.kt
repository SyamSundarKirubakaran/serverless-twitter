package app.syam.twitter.home.model

import android.os.Parcelable
import app.syam.twitter.tweet.model.LightWeightUser
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateTweet(
    @SerializedName("tweet")
    val tweet: String,
    @SerializedName("user")
    val user: LightWeightUser
): Parcelable