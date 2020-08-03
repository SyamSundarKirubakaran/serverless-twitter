package app.syam.twitter.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.item.DividerItem
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.item.HeaderItem
import app.syam.twitter.home.state.HomeCallState
import app.syam.twitter.home.viewmodel.HomeViewModel
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

        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        val viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        val user = SharedPreferenceManager.getLoggedInUser(this)

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
        }

        fab.setOnClickListener {
            MaterialDialog(this).show {
                input(maxLength = 60)
                title(R.string.compose)
                cornerRadius(10f)
                positiveButton(R.string.tweet)
            }
        }

        viewModel.tweets()

        viewModel.tweetListLiveData.observe(this, Observer {
            when (it) {
                is HomeCallState.Success -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    it.result.result?.forEach {
                        tweetsAdapter.apply {
                            add(TweetHeader(
                                user = it.user,
                                optionsVisibility = View.VISIBLE,
                                profileClicked = {

                                },
                                optionsClicked = {

                                }
                            ))
                            add(
                                TweetBodyText(
                                    textContent = it.tweet.orEmpty()
                                )
                            )
                            it.imageUrl?.let { imageUrl ->
                                add(
                                    TweetBodyImage(
                                        url = imageUrl
                                    )
                                )
                            }
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
                HomeCallState.InFlight -> {
                    Toast.makeText(this, "InFlight", Toast.LENGTH_SHORT).show()
                }
                HomeCallState.Failed -> {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}
