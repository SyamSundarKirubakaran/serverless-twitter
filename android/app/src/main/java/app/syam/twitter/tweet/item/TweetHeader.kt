package app.syam.twitter.tweet.item

import app.syam.twitter.R
import app.syam.twitter.tweet.model.LightWeightUser
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_home_header.profileIcon
import kotlinx.android.synthetic.main.item_tweet_header.*

class TweetHeader(
    private val user: LightWeightUser?,
    private val uploadVisibility: Int,
    private val createdAt: String,
    private val profileClicked: () -> Unit,
    private val uploadClicked: () -> Unit
) : Item() {

    override fun bind(vh: ViewHolder, position: Int) {
        Picasso.get()
            .load(user?.imageUrl)
            .placeholder(R.drawable.twitter_png)
            .error(R.drawable.twitter_png)
            .into(vh.profileIcon)

        vh.imageUpload.visibility = uploadVisibility

        vh.tweetTime.text = createdAt

        vh.profileIcon.setOnClickListener { profileClicked.invoke() }
        vh.imageUpload.setOnClickListener { uploadClicked.invoke() }
    }

    override fun getLayout(): Int = R.layout.item_tweet_header
}