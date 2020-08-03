package app.syam.twitter.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.tweet.item.TweetHeader
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_listing.*

class ListingActivity : AppCompatActivity() {

    private val listingAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)

        val user = SharedPreferenceManager.getLoggedInUser(this)

        listingRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listingAdapter
            isNestedScrollingEnabled = false
        }

    }
}
