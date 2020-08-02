package app.syam.twitter.tweet.item

import app.syam.twitter.R
import app.syam.twitter.tweet.model.LightWeightUser
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_tweet_footer.*

class TweetFooter(
    private val isLiked: Boolean,
    private val likedList: List<LightWeightUser>,
    private val likeClicked: () -> Unit,
    private val viewLikedClicked: () -> Unit
) : Item() {

    override fun bind(vh: ViewHolder, position: Int) {

        vh.likeIcon.setOnClickListener { likeClicked.invoke() }
        vh.likedCount.setOnClickListener { viewLikedClicked.invoke() }
    }

    override fun getLayout(): Int = R.layout.item_tweet_footer
}