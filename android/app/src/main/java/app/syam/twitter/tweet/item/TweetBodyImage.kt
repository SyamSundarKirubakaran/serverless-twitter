package app.syam.twitter.tweet.item

import app.syam.twitter.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_tweet_body_image.*

class TweetBodyImage(
    private val url: String
) : Item() {

    override fun bind(vh: ViewHolder, position: Int) {
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.twitter_png)
            .error(R.drawable.twitter_png)
            .into(vh.tweetImage)
    }

    override fun getLayout(): Int = R.layout.item_tweet_body_image
}