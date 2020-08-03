package app.syam.twitter.tweet.model

import com.google.gson.annotations.SerializedName

data class UpdateTweet(
    @SerializedName("likeList")
    val likeList: List<LightWeightUser>
)