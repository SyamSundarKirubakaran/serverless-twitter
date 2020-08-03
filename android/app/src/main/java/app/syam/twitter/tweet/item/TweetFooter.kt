package app.syam.twitter.tweet.item

import android.widget.Toast
import androidx.core.content.ContextCompat
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

        vh.likeIcon.setImageDrawable(
            ContextCompat.getDrawable(
                vh.root.context,
                if (isLiked) R.drawable.ic_heart else R.drawable.ic_open_heart
            )
        )

        vh.likedCount.text = "${likedList.size} ${if(likedList.size == 1) "Like" else "Likes"}"

        vh.likeIcon.setOnClickListener {
            if (!isLiked)
                likeClicked.invoke()
            else
                Toast.makeText(vh.root.context, "Already Liked", Toast.LENGTH_SHORT).show()
        }
        vh.likedCount.setOnClickListener { viewLikedClicked.invoke() }
    }

    override fun getLayout(): Int = R.layout.item_tweet_footer
}