package app.syam.twitter.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.item.DividerItem
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.item.HeaderItem
import app.syam.twitter.profile.ProfileActivity
import app.syam.twitter.tweet.item.TweetBodyImage
import app.syam.twitter.tweet.item.TweetBodyText
import app.syam.twitter.tweet.item.TweetFooter
import app.syam.twitter.tweet.item.TweetHeader
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val tweetsAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val user = SharedPreferenceManager.getLoggedInUser(this)

        fab.setOnClickListener {
            MaterialDialog(this).show {
                input(maxLength = 60)
                title(R.string.compose)
                cornerRadius(10f)
                positiveButton(R.string.tweet)
            }
        }

        tweetsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tweetsAdapter
            isNestedScrollingEnabled = false
        }

        tweetsAdapter.apply {
            add(HeaderItem(storedUser = user) {
                startActivity(
                    Intent(
                        this@HomeActivity,
                        ProfileActivity::class.java
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
