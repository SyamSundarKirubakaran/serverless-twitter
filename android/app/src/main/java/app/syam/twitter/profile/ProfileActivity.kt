package app.syam.twitter.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.item.DividerItem
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.HomeActivity
import app.syam.twitter.listing.ListingActivity
import app.syam.twitter.profile.item.ProfileHeader
import app.syam.twitter.tweet.item.TweetBodyImage
import app.syam.twitter.tweet.item.TweetBodyText
import app.syam.twitter.tweet.item.TweetFooter
import app.syam.twitter.tweet.item.TweetHeader
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val profileAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val user = SharedPreferenceManager.getLoggedInUser(this)

        profileRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = profileAdapter
            isNestedScrollingEnabled = false
        }

        profileAdapter.apply {
            add(ProfileHeader(
                storedUser = user
            ){
                startActivity(
                    Intent(
                        this@ProfileActivity,
                        ListingActivity::class.java
                    )
                )
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
            add(
                TweetBodyText(
                    textContent = "I have this application that I am working on and the user can mark some items as a favorite. I want to use a heart shaped button for this functionality instead of the casual one is it possible?"
                )
            )
            add(
                TweetBodyImage(
                    url = "https://fdn2.gsmarena.com/vv/pics/apple/apple-iphone-x-new-1.jpg"
                )
            )
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
