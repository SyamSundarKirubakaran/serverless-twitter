package app.syam.twitter.tweet.item

import app.syam.twitter.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_tweet_body_text.*

class TweetBodyText(
    private val textContent: String
) : Item() {

    override fun bind(vh: ViewHolder, position: Int) {
        vh.contents.text = textContent
    }

    override fun getLayout(): Int = R.layout.item_tweet_body_text
}