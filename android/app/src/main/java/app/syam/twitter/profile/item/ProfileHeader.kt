package app.syam.twitter.profile.item

import android.view.View
import app.syam.twitter.R
import app.syam.twitter.tweet.model.LightWeightUser
import app.syam.twitter.tweet.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_profile_header.*

class ProfileHeader(
    private val user: User?,
    private val following: Boolean,
    private val followersClicked: () -> Unit,
    private val followingClicked: () -> Unit,
    private val followClicked: () -> Unit
) : Item() {

    override fun bind(vh: ViewHolder, position: Int) {
        Picasso.get()
            .load("https://c4.wallpaperflare.com/wallpaper/500/442/354/outrun-vaporwave-hd-wallpaper-preview.jpg")
            .placeholder(R.drawable.twitter_png)
            .error(R.drawable.twitter_png)
            .into(vh.topBackground)

        Picasso.get()
            .load(user?.imageUrl)
            .placeholder(R.drawable.twitter_png)
            .error(R.drawable.twitter_png)
            .into(vh.profilePicture)

        vh.profileName.text = user?.name

        vh.followersCount.text = "${user?.followerList?.size} Followers"
        vh.followingCount.text = "${user?.followingList?.size} Following"

        vh.followButton.visibility = if(following) View.GONE else View.VISIBLE

        vh.followersCount.setOnClickListener { followersClicked.invoke() }
        vh.followingCount.setOnClickListener { followingClicked.invoke() }
        vh.followButton.setOnClickListener { followClicked.invoke() }
    }

    override fun getLayout(): Int = R.layout.item_profile_header
}