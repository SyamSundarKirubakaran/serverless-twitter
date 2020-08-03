package app.syam.twitter.profile.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignedUrl(
    @SerializedName("uploadUrl")
    val url: String?
): Parcelable