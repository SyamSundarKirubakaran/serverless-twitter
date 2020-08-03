package app.syam.twitter.tweet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tweet(
    @SerializedName("user")
    val user: LightWeightUser?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("likeList")
    val likeList: List<LightWeightUser>?,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("tweet")
    val tweet: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?
): Parcelable