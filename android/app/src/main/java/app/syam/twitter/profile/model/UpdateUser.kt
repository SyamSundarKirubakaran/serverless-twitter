package app.syam.twitter.profile.model

import android.os.Parcelable
import app.syam.twitter.tweet.model.LightWeightUser
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateUser(
    @SerializedName("targetUserId")
    val targetUserId: String?,
    @SerializedName("followingList")
    val followingList: MutableList<LightWeightUser>?,
    @SerializedName("followerList")
    val followerList: MutableList<LightWeightUser>?
): Parcelable