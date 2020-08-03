package app.syam.twitter.auth.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthBody(
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("isVerified")
    val isVerified: Boolean?
): Parcelable