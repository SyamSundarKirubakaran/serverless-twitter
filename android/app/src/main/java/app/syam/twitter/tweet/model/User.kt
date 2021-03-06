package app.syam.twitter.tweet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("isVerified")
    val isVerified: Boolean?,
    @SerializedName("followingList")
    val followingList: List<LightWeightUser>?,
    @SerializedName("followerList")
    val followerList: List<LightWeightUser>?
): Parcelable