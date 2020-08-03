package app.syam.twitter.listing

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.tweet.fragment.SOURCE
import app.syam.twitter.tweet.fragment.TARGET
import app.syam.twitter.tweet.fragment.TWEET
import app.syam.twitter.tweet.fragment.USER
import app.syam.twitter.tweet.item.TweetHeader
import app.syam.twitter.tweet.model.Tweet
import app.syam.twitter.tweet.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_listing.*

class ListingActivity : AppCompatActivity() {

    private val listingAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)

        val source = intent.extras?.getString(SOURCE)

        val tweet = intent.extras?.get(TWEET) as Tweet?

        val user = intent.extras?.get(USER) as User?

        val target = intent.extras?.getString(TARGET)

        val storedUser = SharedPreferenceManager.getLoggedInUser(this)

        listingRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listingAdapter
            isNestedScrollingEnabled = false
        }

        when(source){
            "tweet" -> {
                tweet?.likeList?.forEach {
                    listingAdapter.apply {
                        add(
                            TweetHeader(
                                user = it,
                                uploadVisibility = View.GONE,
                                profileClicked = {},
                                createdAt = "Serverless Twitter User",
                                uploadClicked = {}
                            )
                        )
                    }
                }
            }
            "profile" -> {
                when(target){
                    "follower" -> {
                        user?.followerList?.forEach {
                            listingAdapter.apply {
                                add(
                                    TweetHeader(
                                        user = it,
                                        uploadVisibility = View.GONE,
                                        profileClicked = {},
                                        createdAt = "Serverless Twitter User",
                                        uploadClicked = {}
                                    )
                                )
                            }
                        }
                    }
                    "following" -> {
                        user?.followingList?.forEach {
                            listingAdapter.apply {
                                add(
                                    TweetHeader(
                                        user = it,
                                        uploadVisibility = View.GONE,
                                        profileClicked = {},
                                        createdAt = "Serverless Twitter User",
                                        uploadClicked = {}
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}
