package app.syam.twitter.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.item.DividerItem
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.item.HeaderItem
import app.syam.twitter.tweet.item.TweetBodyImage
import app.syam.twitter.tweet.item.TweetBodyText
import app.syam.twitter.tweet.item.TweetFooter
import app.syam.twitter.tweet.item.TweetHeader
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val tweetsAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val user = SharedPreferenceManager.getLoggedInUser(this)

        tweetsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tweetsAdapter
            isNestedScrollingEnabled = false
        }

        tweetsAdapter.apply {
            add(HeaderItem(storedUser = user) {
                Toast.makeText(this@HomeActivity, "Profile Clicked", Toast.LENGTH_SHORT).show()
            })
            add(DividerItem())
            add(TweetHeader(
                user = user,
                optionsVisibility = View.VISIBLE,
                profileClicked = {

                },
                optionsClicked = {

                }
            ))
            add(TweetBodyText(
                textContent = "I have this application that I am working on and the user can mark some items as a favorite. I want to use a heart shaped button for this functionality instead of the casual one is it possible?"
            ))
            add(TweetBodyImage(
                url = "https://fdn2.gsmarena.com/vv/pics/apple/apple-iphone-x-new-1.jpg"
            ))
            add(TweetFooter(
                isLiked = true,
                likedList = listOf(),
                likeClicked = {

                },
                viewLikedClicked = {

                }
            ))
            add(DividerItem())
            add(TweetHeader(
                user = user,
                optionsVisibility = View.VISIBLE,
                profileClicked = {

                },
                optionsClicked = {

                }
            ))
            add(TweetBodyText(
                textContent = "I have this application that I am working on and the user can mark some items as a favorite. I want to use a heart shaped button for this functionality instead of the casual one is it possible?"
            ))
            add(TweetBodyImage(
                url = "https://fdn2.gsmarena.com/vv/pics/apple/apple-iphone-x-new-1.jpg"
            ))
            add(TweetFooter(
                isLiked = true,
                likedList = listOf(),
                likeClicked = {

                },
                viewLikedClicked = {

                }
            ))
            add(DividerItem())
            add(TweetHeader(
                user = user,
                optionsVisibility = View.VISIBLE,
                profileClicked = {

                },
                optionsClicked = {

                }
            ))
            add(TweetBodyText(
                textContent = "I have this application that I am working on and the user can mark some items as a favorite. I want to use a heart shaped button for this functionality instead of the casual one is it possible?"
            ))
            add(TweetBodyImage(
                url = "https://fdn2.gsmarena.com/vv/pics/apple/apple-iphone-x-new-1.jpg"
            ))
            add(TweetFooter(
                isLiked = true,
                likedList = listOf(),
                likeClicked = {

                },
                viewLikedClicked = {

                }
            ))
            add(DividerItem())
            add(TweetHeader(
                user = user,
                optionsVisibility = View.VISIBLE,
                profileClicked = {

                },
                optionsClicked = {

                }
            ))
            add(TweetBodyText(
                textContent = "I have this application that I am working on and the user can mark some items as a favorite. I want to use a heart shaped button for this functionality instead of the casual one is it possible?"
            ))
            add(TweetBodyImage(
                url = "https://fdn2.gsmarena.com/vv/pics/apple/apple-iphone-x-new-1.jpg"
            ))
            add(TweetFooter(
                isLiked = true,
                likedList = listOf(),
                likeClicked = {

                },
                viewLikedClicked = {

                }
            ))
            add(DividerItem())
        }

    }
}
