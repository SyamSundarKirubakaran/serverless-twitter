package app.syam.twitter.home.item

import app.syam.twitter.R
import app.syam.twitter.common.model.StoredUser
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_home_header.*

class HeaderItem(private val storedUser: StoredUser?, private val profileClicked: () -> Unit) : Item() {

    override fun bind(vh: ViewHolder, position: Int) {
        Picasso.get()
            .load(storedUser?.imageUrl)
            .placeholder(R.drawable.twitter_png)
            .error(R.drawable.twitter_png)
            .into(vh.profileIcon)

        vh.profileIcon.setOnClickListener { profileClicked.invoke() }
    }

    override fun getLayout(): Int = R.layout.item_home_header
}